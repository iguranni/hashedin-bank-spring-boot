package com.hashedin.hashedinbank.controllers;

import com.hashedin.hashedinbank.common.ApiResponse;
import com.hashedin.hashedinbank.entities.CreditCard;
import com.hashedin.hashedinbank.services.CreditCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/card")
public class CreditCardController {
    private final CreditCardService creditCardService;

    @Autowired
    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<CreditCard>>> fetchAllCards() {
        log.info("Inside CreditCardController_fetchAllCards..");
        Page<CreditCard> allCards = creditCardService.getAllCreditCards();
        log.info("Exiting CreditCardController_fetchAllCards..");
        return new ResponseEntity<>(new ApiResponse<>("Successfully fetched all Credit card details from HashedIn Bank : ",
                allCards.getTotalElements(), allCards.getContent()), HttpStatus.OK);
    }
}
