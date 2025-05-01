package com.example.fruit.controller;

import com.example.fruit.model.Order;
import com.example.fruit.service.OrderService;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OrderService orderService;
    private final Dotenv dotenv = Dotenv.load();
    private final String telegramAdminId = dotenv.get("TELEGRAM_ADMIN_ID");

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getOrderImage(@PathVariable String id) {
        return orderService.getOrderById(id)
                .map(order -> ResponseEntity.ok()
                        .header("Content-Type", order.getImageType())
                        .body(order.getImageData()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<Order> createOrder(
            @RequestPart Order order,
            @RequestPart MultipartFile image,
            @RequestParam String chatId
    ) {
        if (!chatId.equals(telegramAdminId)) {
            return ResponseEntity.badRequest().build();
        }

        if (orderService.existsById(order.getId())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            return ResponseEntity.ok(orderService.createOrder(order, image));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable String id,
            @RequestPart Order order,
            @RequestPart MultipartFile image,
            @RequestParam String chatId
    ) {

        if (!chatId.equals(telegramAdminId)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            if (orderService.existsById(id)) {
                return ResponseEntity.ok(orderService.createOrder(order, image));
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable String id,
            @RequestParam String chatId
    ) {

        if (!chatId.equals(telegramAdminId)) {
            return ResponseEntity.badRequest().build();
        }

        if (orderService.existsById(id)) {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}