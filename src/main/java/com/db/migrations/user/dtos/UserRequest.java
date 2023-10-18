package com.db.migrations.user.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String story;

    private String phone;

    private String houseNumber;

    private String streetName;

    private String city;

    private String State;

    private String postalCode;

    private String country;
}
