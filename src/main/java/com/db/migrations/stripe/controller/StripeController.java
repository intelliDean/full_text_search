package com.db.migrations.stripe.controller;

import com.db.migrations.stripe.dto.*;
import com.db.migrations.stripe.services.StripeService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Stripe Controller")
@RequestMapping("api/v1/stripe")
public class StripeController {

    private final StripeService stripeService;

    @PostMapping("create-customer")
    @Operation(summary = "To create a new customer")
    public ResponseEntity<String> createCustomer(@RequestBody @Valid CreateCustomerRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(stripeService.createCustomer(request));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @PostMapping("create-product")
    @Operation(summary = "To create product")
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest request) throws StripeException {
        return ResponseEntity.ok(
                stripeService.createProduct(request)
        );
    }

    @PostMapping("create-payment-intent")
    @Operation(summary = "To create payment intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentIntentRequest request) throws StripeException {
        return ResponseEntity.ok(
                stripeService.createPaymentIntent(request)
        );
    }

    @GetMapping("all-customers")
    @Operation(summary = "To get all customers")
    public ResponseEntity<List<StripeResponse>> allCustomers() {
        try {
            return ResponseEntity.ok(stripeService.allCustomers());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @PostMapping("addCard")
    @Operation(summary = "To add card")
    public ResponseEntity<String> addCard(@RequestBody AddCardRequest request) throws StripeException {
        return ResponseEntity.ok(
                stripeService.addCard(request.getCustomerId())
        );
    }

//    @PostMapping("/create-payment-intent")
//    @Operation(summary = "To create payment intent")
//    public ResponseEntity<Response> createPaymentIntent(@RequestBody Request request) throws StripeException {
//        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                .setAmount(request.getAmount() * 100L)
//                .putMetadata("productName", request.getProductName())
//                .setCurrency("usd")
//                .setAutomaticPaymentMethods(PaymentIntentCreateParams
//                        .AutomaticPaymentMethods
//                        .builder()
//                        .setEnabled(true)
//                        .build())
//                .build();
//        PaymentIntent intent = PaymentIntent.create(params);
//
//        return ResponseEntity.ok(new Response(intent.getId(), intent.getClientSecret()));
//    }
}
