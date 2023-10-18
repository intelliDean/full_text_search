package com.db.migrations.stripe.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentIntentRequest {

    private Long amount;

    private String serviceName;

    private String receiptEmail;

    private String customerId;

    private String productOwner;
}