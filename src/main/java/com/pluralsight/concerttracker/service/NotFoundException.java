package com.pluralsight.concerttracker.service;

// Thrown when something is looked up by id but doesn't exist. The menu catches
// this once per screen and prints the message, so a bad id never crashes the app.
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}