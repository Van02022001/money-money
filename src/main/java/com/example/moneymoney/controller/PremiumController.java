package com.example.moneymoney.controller;

import com.example.moneymoney.entity.*;
import com.example.moneymoney.model.requestmodel.OrderRequestDTO;
import com.example.moneymoney.model.requestmodel.PremiumSubscriptionRequestDTO;
import com.example.moneymoney.model.responsemodel.IncomeResponse;
import com.example.moneymoney.model.responsemodel.OrderDTO;
import com.example.moneymoney.model.responsemodel.OrderDetailResponseDTO;
import com.example.moneymoney.repository.AssetRepository;
import com.example.moneymoney.repository.OrderRepository;
import com.example.moneymoney.repository.PremiumSubscriptionRepository;
import com.example.moneymoney.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/money-money/users/premiums")
@Slf4j
@RequiredArgsConstructor
public class PremiumController {

    private final UserService userService;
    private final PremiumSubscriptionRepository premiumSubscriptionRepository;

    private final OrderRepository orderRepository;

    private final AssetRepository assetRepository;

    private final ModelMapper modelMapper;
    @RequestMapping(value = "/api/orders", method = RequestMethod.POST)
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO orderRequest, Principal principal) {
        try {
            // Xác định người dùng từ thông tin đăng nhập hoặc token
            String username = principal.getName();
            User user = userService.findUserByEmail(username);

            // Tạo đối tượng Order từ thông tin yêu cầu đặt hàng
            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setOrderStatus(false); // Đơn hàng chưa được thanh toán

            // Xử lý các thông tin chi tiết đơn hàng
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (PremiumSubscriptionRequestDTO subscriptionRequest : orderRequest.getSubscriptions()) {
                // Tạo đối tượng OrderDetail từ thông tin gói Premium được mua
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);

                // Lấy thông tin gói Premium từ cơ sở dữ liệu
                PremiumSubscription subscription = premiumSubscriptionRepository.findPremiumSubscriptionById(subscriptionRequest.getSubscriptionId());
                if (subscription == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid subscription ID: " + subscriptionRequest.getSubscriptionId());
                }

                orderDetail.setPremiumSubscription(subscription);
                orderDetail.setUnitPrice(subscription.getUnitPrice());
                orderDetail.setQuantity(subscriptionRequest.getQuantity());
                orderDetail.setTotalPrice(calculateTotalPriceForAloneOrderDetail(orderDetail));

                orderDetails.add(orderDetail);
            }

            BigDecimal total = BigDecimal.valueOf(0);

            for (OrderDetail orderDetail : orderDetails) {
                total = total.add(orderDetail.getTotalPrice());
            }


            // Lưu đơn hàng và chi tiết đơn hàng vào cơ sở dữ liệu
            order.setOrderDetails(orderDetails);
            orderRepository.save(order);

            List<OrderDetailResponseDTO> responseDTOS  = modelMapper.map(orderDetails, new TypeToken<List<OrderDetailResponseDTO>>() {
            }.getType());

            OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
            orderDTO.setList(responseDTOS);
            orderDTO.setTotalPrice(total);

            // Trả về thông tin đơn hàng đã được tạo
            return ResponseEntity.ok(orderDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating the order.");
        }
    }




    @RequestMapping(value = "/api/orders/{orderId}/checkout", method = RequestMethod.POST)
    public ResponseEntity<?> checkoutOrder(@PathVariable("orderId") Long orderId, @RequestParam("assetId") Long assetId, Principal principal) {
        // Lấy thông tin đơn hàng từ cơ sở dữ liệu
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            // Xác định người dùng từ thông tin đăng nhập hoặc token
            String username = principal.getName();
            User user = userService.findUserByEmail(username);

            // Kiểm tra xem người dùng có quyền thanh toán cho đơn hàng này không
            if (!user.equals(order.getUser())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to checkout this order.");
            }

            // Tính tổng tiền từ OrderDetail
            BigDecimal totalPrice = calculateTotalPrice(order.getOrderDetails());
            order.setTotalPrice(totalPrice);

            // Trừ UnitStock của Premium
            boolean stockReduced = reducePremiumUnitStock(order.getOrderDetails());

            if (!stockReduced) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient stock for one or more Premium subscriptions.");
            }

            // Xác nhận thanh toán cho đơn hàng
            order.setOrderStatus(true); // Đã thanh toán

            // Lấy thông tin Asset từ assetId
            Optional<Asset> optionalAsset = assetRepository.findById(assetId);
            if (optionalAsset.isPresent()) {
                Asset asset = optionalAsset.get();
                order.setPaymentMethod(asset.getAssetName());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Asset not found with ID: " + assetId);
            }

            // Lưu thay đổi vào cơ sở dữ liệu
            orderRepository.save(order);

            // Thực hiện các bước xử lý thêm sau khi thanh toán thành công (ví dụ: gửi email xác nhận)

            // Trả về thông báo thành công
            return ResponseEntity.ok("Order has been successfully checked out.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found with ID: " + orderId);
        }
    }



    private BigDecimal calculateTotalPrice(List<OrderDetail> orderDetails) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderDetail orderDetail : orderDetails) {
            BigDecimal unitPrice = orderDetail.getPremiumSubscription().getUnitPrice();
            int quantity = orderDetail.getQuantity();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
            totalPrice = totalPrice.add(subtotal);
        }
        return totalPrice;
    }

    private BigDecimal calculateTotalPriceForAloneOrderDetail(OrderDetail orderDetails) {
        BigDecimal totalPrice = BigDecimal.ZERO;
            BigDecimal unitPrice = orderDetails.getPremiumSubscription().getUnitPrice();
            int quantity = orderDetails.getQuantity();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
            totalPrice = totalPrice.add(subtotal);
        return totalPrice;
    }

    private boolean reducePremiumUnitStock(List<OrderDetail> orderDetails) {
        for (OrderDetail orderDetail : orderDetails) {
            PremiumSubscription premiumSubscription = orderDetail.getPremiumSubscription();
            int quantity = orderDetail.getQuantity();
            int unitStock = premiumSubscription.getUnitStock();

            if (unitStock < quantity) {
                return false; // Không đủ hàng để trừ
            }

            premiumSubscription.setUnitStock(unitStock - quantity);
        }
        return true;
    }



}
