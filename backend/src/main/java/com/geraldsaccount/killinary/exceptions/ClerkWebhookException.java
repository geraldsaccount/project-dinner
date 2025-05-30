package com.geraldsaccount.killinary.exceptions;

public class ClerkWebhookException extends Exception {

    public ClerkWebhookException(String message) {
        super(message);
    }

    public ClerkWebhookException(String message, Throwable cause) {
        super(message, cause);
    }

}
