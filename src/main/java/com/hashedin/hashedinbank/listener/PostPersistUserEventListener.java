package com.hashedin.hashedinbank.listener;

import com.hashedin.hashedinbank.entities.User;
import com.hashedin.hashedinbank.services.CreditCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PostPersistUserEventListener {

    private final CreditCardService creditCardService;

    @Autowired
    public PostPersistUserEventListener(@Lazy CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @EventListener
    public void handleEvent(User user) {
        log.info("inside post persist");
        creditCardService.generateCreditCard(user);
    }
}
