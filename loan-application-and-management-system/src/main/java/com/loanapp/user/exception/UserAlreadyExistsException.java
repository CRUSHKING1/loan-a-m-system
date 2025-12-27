
package com.loanapp.user.exception;

public class UserAlreadyExistsException extends  Exception{

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}