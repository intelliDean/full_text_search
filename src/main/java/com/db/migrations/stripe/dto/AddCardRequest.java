package com.db.migrations.stripe.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCardRequest {
    private String customerId;
}
