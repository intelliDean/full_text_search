package com.db.migrations.user.services;

import com.db.migrations.stripe.dto.CreateCustomerRequest;
import com.db.migrations.stripe.dto.StripeResponse;
import com.db.migrations.stripe.services.StripeService;
import com.db.migrations.user.dtos.Response;
import com.db.migrations.user.dtos.UserRequest;
import com.db.migrations.user.models.Address;
import com.db.migrations.user.models.User;
import com.db.migrations.user.repository.AddressRepository;
import com.db.migrations.user.repository.UserRepository;
import com.stripe.exception.StripeException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final StripeService stripeService;

    @Transactional(propagation = Propagation.REQUIRED)
    public Response saveUser(UserRequest request) throws StripeException {
        String stripeId = createCustomerRequest(request);
        userRepository.save(
                User.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .story(request.getStory())
                        .address(Address.builder()
                                .houseNumber(request.getHouseNumber())
                                .streetName(request.getStreetName())
                                .city(request.getCity())
                                .state(request.getState())
                                .country(request.getCountry())
                                .build())
                        .stripeId(stripeId)
                        .build()
        );
        return Response.builder().message("User saved successfully!").build();
    }

    public StripeResponse retrieveCustomer(Long userId) throws StripeException {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User could not be found"));
        return stripeService.retrieveCustomer( user.getStripeId());
    }

    private String createCustomerRequest(UserRequest request) throws StripeException {
        String fullName = request.getFirstName() + " " + request.getLastName();
        CreateCustomerRequest customerRequest =  CreateCustomerRequest.builder()
                .name(fullName)
                .email(request.getEmail())
                .phone(request.getPhone())
                .description("Stripe Customer account for " + fullName)
                .street(request.getHouseNumber() + ", " + request.getStreetName())
                .city(request.getCity())
                .state(request.getState())
                .postCode(request.getPostalCode())
                .country(request.getCountry())
                .build();
       return stripeService.createCustomer(customerRequest);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByFirstName(String firstName) {
        return userRepository.findAllByFirstName(firstName);
    }

    public List<User> getUsersByLastName(String lastName) {
        return userRepository.findAllByLastName(lastName);
    }

    public List<User> fullTextSearch(String keyword) {
        return userRepository.fullTextSearch(keyword);
    }

    public List<User> usersByAddress(String address) {
        List<Address> addresses = addressRepository.fullTextSearchAddress(address);
        List<User> users = new ArrayList<User>();
        addresses.stream().
                map(add -> userRepository.findAllByAddress_Id(add.getId()))
                .forEach(users::addAll);
        return users;
    }
}
