package com.db.migrations.models;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public List<User> getUsers() {
        return userRepository.findAll();
    }
//    public List<User> getUsersByEmail(String email) {
//        return userRepository.findAllByEmail(email);
//    }
    public List<User> getUsersByFirstName(String firstName) {
        return userRepository.findAllByFirstName(firstName);
    }
    public List<User> getUsersByLastName(String lastName) {
        return userRepository.findAllByLastName(lastName);
    }
    public List<User> fullTextSearch(String keyword) {
        return userRepository.fullTextSearch(keyword);
    }
//    public List<User> realFullTextSearch(String keyword) {
//        return userRepository.search(keyword);
//    }
}
