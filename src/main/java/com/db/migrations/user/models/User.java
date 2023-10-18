package com.db.migrations.user.models;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.CascadeType.ALL;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stripeId;

    private String firstName;

    private String lastName;

    @OneToOne(cascade = ALL)
    private Address address;

    @Column(columnDefinition = "TEXT")
    private String story;

}
