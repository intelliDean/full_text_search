package com.db.migrations.models;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/view")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("users")
    @Operation(summary = "to get all users")
    public ResponseEntity<List<User>> allUsers() {
        return ResponseEntity.ok(
                userService.getUsers()
        );
    }
//    @GetMapping("email/{email}")
//    @Operation(summary = "to get all users by email")
//    public ResponseEntity<List<User>> allByEmail(@PathVariable String email) {
//        return ResponseEntity.ok(
//                userService.getUsersByEmail(email)
//        );
//    }
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
//    @GetMapping("search/{keyword}")
//    @Operation(summary = "Real Full text search")
//    public ResponseEntity<List<User>> realFullTextSearch(@PathVariable String keyword) {
//        return ResponseEntity.ok(
//                userService.realFullTextSearch(keyword)
//        );
//    }

}
