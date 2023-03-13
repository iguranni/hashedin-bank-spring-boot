package com.hashedin.hashedinbank.services;

import com.hashedin.hashedinbank.entities.CreditCard;
import com.hashedin.hashedinbank.entities.User;
import org.springframework.data.domain.Page;

public interface CreditCardService {
    void generateCreditCard(User user);
    Page<CreditCard> getAllCreditCards();
    void validateCreditCard();

}
