package com.fragment.seat_reservation.exceptions;

public class NotResourceOwnerException extends RuntimeException {
    public NotResourceOwnerException(String message) {
        super(message);
    }
}
