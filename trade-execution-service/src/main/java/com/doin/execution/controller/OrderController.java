package com.doin.execution.controller;

import com.doin.execution.payload.dto.OrderDto;
import com.doin.execution.payload.response.ApiResponse;
import com.doin.execution.payload.response.PaginatedResponse;
import com.doin.execution.payload.response.ResponseBuilder;
import com.doin.execution.service.OrderService;
import com.doin.execution.util.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<OrderDto>>> getOrders(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseBuilder.ok(
                PaginationUtils.buildResponse(orderService.getOrdersByUser(userId, page, size)),
                "Orders retrieved successfully"
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrder(@PathVariable String id, @RequestParam Long userId) {
        return ResponseBuilder.ok(orderService.getOrderByIdForUser(id, userId), "Order retrieved successfully");
    }
}