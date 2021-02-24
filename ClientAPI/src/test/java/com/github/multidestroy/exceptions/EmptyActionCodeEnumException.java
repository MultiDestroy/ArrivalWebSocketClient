package com.github.multidestroy.exceptions;

public class EmptyActionCodeEnumException extends RuntimeException {

    public EmptyActionCodeEnumException() {
        super("ActionCode enum is empty!");
    }

}
