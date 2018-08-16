package com.company.task.common.exception;

/**
 * thrown when cannot save the game in file
 *
 * @see RuntimeException
 */
public class SaveGameException extends RuntimeException {

    public SaveGameException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
