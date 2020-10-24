package org.example.spboot.service;

import org.example.spboot.messaging.LoginMessage;
import org.example.spboot.messaging.RegistrationMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface MessagingService {
    void sendRegistrationMessage(RegistrationMessage msg) throws IOException;

    void sendLoginMessage(LoginMessage msg) throws  IOException;
}
