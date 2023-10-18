package com.db.migrations.user.controller;

import com.db.migrations.stripe.dto.CreateCustomerRequest;
import com.db.migrations.stripe.dto.StripeResponse;
import com.db.migrations.stripe.services.StripeService;
import com.db.migrations.user.dtos.Response;
import com.db.migrations.user.dtos.UserRequest;
import com.db.migrations.user.models.User;
import com.db.migrations.user.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final StripeService stripeService;


    @PostMapping("register")
    @Operation(summary = "register user")
    public ResponseEntity<Response> registerUser(@RequestBody UserRequest request) {
        try {
            return new ResponseEntity<>(userService.saveUser(request), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @GetMapping("get-stripe-customer/{userId}")
    @Operation(summary = "to get a user stripe customer by stripe id")
    public ResponseEntity<StripeResponse> retrieveCustomer(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(userService.retrieveCustomer(userId));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
    @GetMapping("users")
    @Operation(summary = "to get all users")
    public ResponseEntity<List<User>> allUsers() {
        return ResponseEntity.ok(
                userService.getUsers()
        );
    }

    @GetMapping("first-name/{firstName}")
    @Operation(summary = "to get all users by first name")
    public ResponseEntity<List<User>> allByFirstName(@PathVariable String firstName) {
        return ResponseEntity.ok(
                userService.getUsersByFirstName(firstName)
        );
    }

    @GetMapping("last-name/{lastName}")
    @Operation(summary = "to get all users by last name")
    public ResponseEntity<List<User>> allByLastName(@PathVariable String lastName) {
        return ResponseEntity.ok(
                userService.getUsersByLastName(lastName)
        );
    }

    @GetMapping("full-text/{keyword}")
    @Operation(summary = "Full text search")
    public ResponseEntity<List<User>> fullTestSearch(@PathVariable String keyword) {
        return ResponseEntity.ok(
                userService.fullTextSearch(keyword)
        );
    }

    @GetMapping("by-address/{keyword}")
    @Operation(summary = "user by address")
    public ResponseEntity<List<User>> usersByAddress(@PathVariable String keyword) {
        return ResponseEntity.ok(
                userService.usersByAddress(keyword)
        );
    }

    @PostMapping("create-customer")
    @Operation(summary = "To create a new customer")
    public ResponseEntity<Object> createCustomer(@RequestBody @Valid CreateCustomerRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(stripeService.createCustomer(request));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
