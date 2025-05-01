package com.example.fruit.service;

import com.example.fruit.model.Order;
import com.example.fruit.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;

    public Optional<Order> getOrderById(String id) {
        return orderRepo.findById(id);
    }

    public boolean existsById(String id) {
        return orderRepo.existsById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order createOrder(Order order, MultipartFile image) throws IOException {

        order.setImageName(image.getOriginalFilename());
        order.setImageType(image.getContentType());
        order.setImageData(image.getBytes());

        return orderRepo.save(order);
    }

    public void deleteOrder(String id) {
        orderRepo.deleteById(id);
    }
}
