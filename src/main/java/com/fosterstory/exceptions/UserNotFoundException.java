package com.fosterstory.exceptions;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException(int userId)
    {
        super("Unable to find user id: " + userId);
    }
}
