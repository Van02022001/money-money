package com.example.moneymoney.service.Impl;

import com.example.moneymoney.entity.*;
import com.example.moneymoney.enums.AuthProvider;
import com.example.moneymoney.enums.Role;
import com.example.moneymoney.exception.UserNotFoundException;
import com.example.moneymoney.jwt.*;
import com.example.moneymoney.jwt.userprincipal.Principal;
import com.example.moneymoney.model.requestmodel.*;
import com.example.moneymoney.model.responsemodel.PurchaseResult;
import com.example.moneymoney.model.responsemodel.RefreshTokenResponse;
import com.example.moneymoney.repository.*;
import com.example.moneymoney.service.UserService;
import com.example.moneymoney.utils.PrincipalDTO;
import com.example.moneymoney.utils.ResponseObject;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private AssetRepository assetRepository;
    private final String companyEmail = "qvanwork@outlook.com.vn";
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private  ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private RefreshTokenProvider refreshTokenProvider;



    private final AuthenticationManager authenticationManager;

    @Autowired
    CacheManager cacheManager;
    @Override
    public User registerUser(SignUpModel userModel) {
        User user = mapper.map(userModel, User.class);
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole(Role.USER);
        user.setProvider(AuthProvider.local);
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user, token);

        verificationRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationRepository.findByToken(token);
        if (verificationToken == null){
            return "invalid";
        }
        User user = verificationToken.getUser();

        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            verificationRepository.delete(verificationToken);
            return "expired";
        }
        user.setEnable(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->
                new UserNotFoundException(email, " not found"));
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordTokenRepository.findByToken(token);
        if (passwordResetToken == null){
            return "invalid";
        }
        User user = passwordResetToken.getUser();

        Calendar calendar = Calendar.getInstance();
        if ((passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            passwordTokenRepository.delete(passwordResetToken);
            return "expired";
        }
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public ResponseEntity<ResponseObject> validateLoginForm(LoginModel loginModel) {
        if ((loginModel.getEmail().isEmpty() || loginModel.getEmail().isBlank()) && (loginModel.getPassword().isEmpty() || loginModel.getPassword().isBlank())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Empty input field!", null, null));
        } else if (loginModel.getEmail().isEmpty() || loginModel.getEmail().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Empty email!", null, null));
        } else if (loginModel.getPassword().isEmpty() || loginModel.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Empty password!", null, null));
        }
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> login(LoginModel loginModel) {
        ResponseEntity<ResponseObject> responseEntity = this.validateLoginForm(loginModel);
        if (responseEntity != null) {
            return responseEntity;
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginModel.getEmail(), loginModel.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Principal userPrinciple = (Principal) authentication.getPrincipal();
            String accessToken = jwtProvider.createToken(userPrinciple);
            String refreshToken = refreshTokenProvider.createRefreshToken(loginModel.getEmail()).getToken();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject(HttpStatus.ACCEPTED.toString(), "Login success!", null, new JwtResponse(accessToken, refreshToken)));
        } catch (AuthenticationException e) {
            if (e instanceof DisabledException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(HttpStatus.UNAUTHORIZED.toString(), "Account has been locked. Please contact " + companyEmail + " for more information", null, null));
            }
            if(e instanceof AccountExpiredException){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(HttpStatus.UNAUTHORIZED.toString(), "The account has expired. Please contact " + companyEmail + " for more information", null, null));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(HttpStatus.UNAUTHORIZED.toString(), "Invalid email or password. Please try again.", null, null));
        }
    }

    @Override
    public ResponseEntity<ResponseObject> validateAccessToken() {
        Principal userPrinciple = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PrincipalDTO principalDTO = mapper.map(userPrinciple, PrincipalDTO.class);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject(HttpStatus.ACCEPTED.toString(), "Validate access token success!", null, principalDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> refreshAccessToken(HttpServletRequest request, RefreshTokenModel refreshTokenModel) {
        String accessToken = jwtProvider.getJwt(request);
        try {
            jwtProvider.validateTokenThrowException(accessToken);
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                return refreshTokenProvider.findByToken(DigestUtils.sha3_256Hex(refreshTokenModel.getRefreshToken()))
                        .map(refreshTokenProvider::verifyExpiration)
                        .map(RefreshToken::getUser)
                        .map(user -> {
                            Principal principal = Principal.build(user);
                            String newAccessToken = jwtProvider.createToken(principal);
                            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject(HttpStatus.ACCEPTED.toString(), "Refresh token success!", null, new RefreshTokenResponse(newAccessToken, refreshTokenModel.getRefreshToken())));
                        })
                        .orElseThrow(() -> new RefreshTokenException("Refresh token is not in database!"));
            }
            throw new JwtTokenException("Error -> Unauthorized");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(HttpStatus.UNAUTHORIZED.toString(), "Cannot create new access token -> Old access token is not expired", null, null));
    }

    @Override
    public ResponseEntity<ResponseObject> logout(HttpServletRequest request, LogoutModel logoutModel) {
        //delete refresh token
        refreshTokenProvider.deleteByToken(logoutModel.getRefreshToken());
        //set access token into black list to prevent reused.
        String accessToken = jwtProvider.getJwt(request);
        Instant expiredTime = jwtProvider.getAccessTokenExpiredTime(accessToken).toInstant();


        //For cache
        this.clearRefreshTokenCache(logoutModel.getRefreshToken());
        this.clearUserDetailsCache(jwtProvider.getUsernameFromToken(accessToken));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject(HttpStatus.ACCEPTED.toString(), "Logout success!", null, new RefreshTokenResponse(null, null)));
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findUserById(id);
    }




    private void clearRefreshTokenCache(String refreshToken) {
        boolean result = cacheManager.getCache("refreshToken").evictIfPresent(refreshToken);
        if (result) {
            log.info("Clear refresh token " + refreshToken + " from cache");
        } else {
            log.error("Fail clear refresh token " + refreshToken + " from cache");
        }
    }
    private void clearUserDetailsCache(String username) {
        boolean result = cacheManager.getCache("userDetails").evictIfPresent(username);
        if (result) {
            log.info("Clear account " + username + " from cache");
        } else {
            log.error("Fail clear account " + username + " from cache");
        }
    }



}
