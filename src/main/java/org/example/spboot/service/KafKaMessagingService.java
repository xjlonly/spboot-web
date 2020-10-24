package org.example.spboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.spboot.messaging.LoginMessage;
import org.example.spboot.messaging.RegistrationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Qualifier("Kafka")
public class KafKaMessagingService implements MessagingService{

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;


    @Override
    public void sendRegistrationMessage(RegistrationMessage msg) throws IOException{
        send("topic_registration",msg);
    }

    @Override
    public void sendLoginMessage(LoginMessage msg) throws IOException{
        send("topic_login", msg);
    }

    private void send(String topic, Object msg)throws IOException {
        ProducerRecord<String,String> producerRecord = new ProducerRecord<>(topic,objectMapper.writeValueAsString(msg));
        producerRecord.headers().add("type",msg.getClass().getName().getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(producerRecord);
    }
}
