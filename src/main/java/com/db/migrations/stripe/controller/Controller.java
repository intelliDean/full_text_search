package com.db.migrations.stripe.controller;

import com.db.migrations.stripe.dto.Request;
import com.db.migrations.stripe.dto.Response;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
//@Tag(name = "Stripe Controller")
public class Controller {

    @PostMapping("/create-payment-intent")
    @Operation(summary = "To create payment intent")
    public ResponseEntity<Response> createPaymentIntent(
            @RequestBody Request request) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(request.getAmount() * 100L)
                .putMetadata("productName", request.getProductName())
                .setCurrency("usd")
                .setAutomaticPaymentMethods(PaymentIntentCreateParams
                        .AutomaticPaymentMethods
                        .builder()
                        .setEnabled(true)
                        .build())
                .build();
        PaymentIntent intent = PaymentIntent.create(params);

        return ResponseEntity.ok(new Response(intent.getId(), intent.getClientSecret()));
    }
}
