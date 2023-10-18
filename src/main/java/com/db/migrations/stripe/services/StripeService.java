package com.db.migrations.stripe.services;

import com.db.migrations.stripe.dto.CreateCustomerRequest;
import com.db.migrations.stripe.dto.PaymentIntentRequest;
import com.db.migrations.stripe.dto.ProductRequest;
import com.db.migrations.stripe.dto.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.*;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripeService {
    @Value("${stripe.apikey.sk}")
    private String stripeApiKeySK;

    @PostConstruct
    private void addStripeSecretKey() {
        Stripe.apiKey = stripeApiKeySK;
    }

    public String createCustomer(@Valid CreateCustomerRequest request) throws StripeException {
        CustomerCreateParams.Address address = CustomerCreateParams.Address.builder()
                .setLine1(request.getStreet())
                .setCity(request.getCity())
                .setState(request.getState())
                .setCountry(request.getCountry())
                .setPostalCode(request.getPostCode())
                .build();
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setName(request.getName())
                .setEmail(request.getEmail())
                .setPhone(request.getPhone())
                .setDescription(request.getDescription()) //this will tell who they are, either customer or service provider
                .setAddress(address)
                .build();
        try {
            Customer customer = Customer.create(params);
            return customer.getId();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public StripeResponse retrieveCustomer(String customerId) throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        LocalDateTime createdAt = getCreatedAt(customer.getCreated());
        return StripeResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .balance(BigDecimal.valueOf(customer.getBalance()))
                .description(customer.getDescription())
                .created(createdAt)
                .address(customer.getAddress())
                .build();
    }

    private LocalDateTime getCreatedAt(Long created) {
        Instant instant = Instant.ofEpochSecond(created);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public String updateMetaData(String customerId) throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        CustomerUpdateParams updateParams = CustomerUpdateParams.builder()
                .putMetadata("order_id", "6735")
                .build();
        customer.update(updateParams);
        return "update successfully";
//
//
//        Map<String, Object> metadata = new HashMap<>();
//        metadata.put("order_id", "6735");
//        Map<String, Object> params = new HashMap<>();
//        params.put("metadata", metadata);
//
//        Customer updatedCustomer =
//                customer.update(params);


    }

    public String deleteCustomer(String customerId) throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        Customer deletedCustomer = customer.delete();
        return deletedCustomer.toJson();
    }

    public List<StripeResponse> allCustomers() throws StripeException {

        Map<String, Object> params = new HashMap<>();
        params.put("limit", 3);

        CustomerCollection customerCollection = Customer.list(params);
        List<StripeResponse> stripeResponses = new ArrayList<>();
        for (Customer customer : customerCollection.autoPagingIterable()) {
            LocalDateTime createdAt = getCreatedAt(customer.getCreated());
            stripeResponses.add(
                    StripeResponse.builder()
                            .id(customer.getId())
                            .name(customer.getName())
                            .email(customer.getEmail())
                            .phone(customer.getPhone())
                            .balance(BigDecimal.valueOf(customer.getBalance()))
                            .description(customer.getDescription())
                            .created(createdAt)
                            .address(customer.getAddress())
                            .build()
            );
        }
        return stripeResponses;
    }

    public String makePayment() throws StripeException {
        PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                .setAmount(2000L)
                .setCurrency("usd")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build()
                )
                .setCustomer("asdsasdsfs")
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(createParams);
        return "Success";
    }

    public String createProduct(ProductRequest request) throws StripeException {
        ProductCreateParams param1 =
                ProductCreateParams.builder()
                        .setName(request.getServiceName())
                        .setDescription(request.getServiceDescription())
                        .setActive(request.isActive())
                        .putMetadata("Service Provider ID", request.getServiceProviderStripeId())
                        .setDefaultPriceData(ProductCreateParams.DefaultPriceData.builder()
                                .setCurrency("AUD")
                                .setUnitAmount(request.getServicePricePerUnit() * 100)
                                .build())
                        .build();
        Product product = Product.create(param1);
        return product.getId();
    }

    public String createPaymentIntent(PaymentIntentRequest request) throws StripeException {
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(request.getAmount() * 100L)
                        .putMetadata("Service", request.getServiceName())
                        .setCurrency("aud")
                        .setReceiptEmail(request.getReceiptEmail())
                        .setCustomer(request.getCustomerId())
                        .setDescription("This payment is for " + request.getServiceName() + " service")
                        .putMetadata("Product Owner", request.getProductOwner())
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams
                                        .AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();
        PaymentIntent intent = PaymentIntent.create(params);
        return intent.getId();
    }

    public String addCard(String customerId) throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        TokenCreateParams params = TokenCreateParams.builder()
                .setCard(TokenCreateParams.Card.builder()
                        .setNumber("4242424242424242")
                        .setExpMonth("10")
                        .setExpYear("2024")
                        .setCvc("140")
                        .build())
                .build();
        Token token = Token.create(params);

        SourceCreateParams sourceCreateParams = SourceCreateParams.builder()
                .setToken(token.getId())
                .build();
        Source source = Source.create(sourceCreateParams);
        PaymentSourceCollectionCreateParams createParams = PaymentSourceCollectionCreateParams.builder()
                .setSource(source.getId())
                .build();
        customer.getSources().create(createParams);
        System.out.println(source);

        return "Card added";
    }

    public static void main(String[] args) throws StripeException {
        Stripe.apiKey = "sk_test_51NwA5rBX3MgejENsLRQ8qo28vZLYZ1ZQnFsTAYqR1aEccqcpFLlPJgyhHwqxFqEnb2rpqmP4SGxtckiWq1L8H8DH00z1iGQ6hD";
        Customer customer = Customer.retrieve("cus_OkQNo14TNYmNvO");

        Map<String, Object> card = new HashMap<>();
        card.put("number", "4242424242424242");
        card.put("exp_month", "10");
        card.put("exp_year", "2024");
        card.put("cvc", "143");

        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("card", card);
        Token token = Token.create(tokenMap);

        Map<String, Object> sourceMap = new HashMap<>();
        sourceMap.put("source", token.getId());

        customer.getSources().create(sourceMap);
        System.out.println(customer);

    }


}
