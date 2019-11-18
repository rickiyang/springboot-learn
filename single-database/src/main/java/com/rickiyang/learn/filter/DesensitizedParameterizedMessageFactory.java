package com.rickiyang.learn.filter;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;
public class DesensitizedParameterizedMessageFactory implements MessageFactory {

    private ParameterizedMessageFactory messageFactory = ParameterizedMessageFactory.INSTANCE;

    @Override
    public Message newMessage(Object message) {
        return messageFactory.newMessage(message);
    }

    @Override
    public Message newMessage(String message) {
        return new DesensitizedParameterizedMessage(message);
    }

    @Override
    public Message newMessage(String message, Object... params) {
        return new DesensitizedParameterizedMessage(message, params);
    }
}