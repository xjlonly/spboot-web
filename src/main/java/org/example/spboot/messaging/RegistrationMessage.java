package org.example.spboot.messaging;

public class RegistrationMessage extends AbstractMessage{
    public static RegistrationMessage of(String email,String name){
        var message = new RegistrationMessage();
        message.email = email;
        message.name =name;
        message.timestamp = System.currentTimeMillis();
        return message;
    }

    @Override
    public String toString() {
        return String.format("[RegistrationMessage: email=%s, name=%s, timestamp=%s]", email, name, timestamp);
    }
}
