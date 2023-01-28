package me.mourjo.exceptions;

public class TooManyUsersException extends RuntimeException {
    long users;
    public TooManyUsersException(long n) {
        super();
        users = n;
    }
}
