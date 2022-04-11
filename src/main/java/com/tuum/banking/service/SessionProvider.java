package com.tuum.banking.service;

import org.springframework.stereotype.Component;

@Component
public class SessionProvider {

    public String getUserWhoSignedIn() {
        return "demo";
    }

}
