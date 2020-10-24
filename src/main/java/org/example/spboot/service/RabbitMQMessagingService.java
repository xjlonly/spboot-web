package org.example.spboot.service;

import org.example.spboot.messaging.LoginMessage;
import org.example.spboot.messaging.RegistrationMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Component;

import javax.validation.executable.ValidateOnExecution;
import java.io.IOException;

@Component
@Qualifier("RabbitMQ")
public class RabbitMQMessagingService implements MessagingService{
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void sendRegistrationMessage(RegistrationMessage msg) throws IOException {
        rabbitTemplate.convertAndSend("registration","",msg);
    }

    @Override
    public void sendLoginMessage(LoginMessage msg) throws  IOException{
        String route_key = msg.success ? "" : "login_failed";
        rabbitTemplate.convertAndSend("login",route_key,msg);
    }
}
