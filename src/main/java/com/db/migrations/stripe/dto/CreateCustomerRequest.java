package com.db.migrations.stripe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerRequest {

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String email;

    @NotBlank
    @NotNull
    private String street;

    @NotBlank
    @NotNull
    private String city;

    private String postCode;

    @NotBlank
    @NotNull
    private String state;

    @NotBlank
    @NotNull
    private String country;

    @NotBlank
    @NotNull
    private String phone;

    private String description;

}