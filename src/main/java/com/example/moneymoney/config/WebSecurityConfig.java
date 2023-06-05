package com.example.moneymoney.config;

import com.example.moneymoney.jwt.JwtTokenFilter;
import com.example.moneymoney.jwt.userprincipal.CustomUserDetailService;
import com.example.moneymoney.oauth2.OAuth2AuthenticationFailureHandler;
import com.example.moneymoney.oauth2.OAuth2AuthenticationSuccessHandler;
import com.example.moneymoney.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.moneymoney.service.Impl.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
    private static final String[] UNAUTHORIZED_LIST_URLS = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/v2/api-docs/**",
            "api/v1/money-money/accounts",
            "api/v1/money-money/accounts/authentication",
            "api/v1/money-money/accounts/verification",
            "api/v1/money-money/accounts/verification/resend",
            "api/v1/money-money/accounts/password-reset",
            "api/v1/money-money/accounts/password-save",
            "/error",
            "/css/**",
            "/js/**", "/img/**", "/lib/**",
            "/favicon.ico",
            "/oauth2/**",



    };




    private static final String[] AUTHORIZED_LIST_URLS = {


            "api/v1/money-money/users/password-change",
            "api/v1/money-money/users/validation",
            "api/v1/money-money/users/accessToken",
            "api/v1/money-money/users/sessions",
            "api/v1/money-money/users/expenses/**",
            "api/v1/money-money/users/incomes/**",
            "api/v1/money-money/users/profits/**",
            "api/v1/money-money/users/interests/**"
    };
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(11);
    }


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailService();
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*")); // Cho phép truy cập từ tất cả các nguồn
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       return http
                .cors()
                .disable()
                .csrf()
                .disable()
                .formLogin()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(UNAUTHORIZED_LIST_URLS).permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers(AUTHORIZED_LIST_URLS)
                .authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();


    }

}
