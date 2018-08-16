package com.company.task.common.exception;

/**
 * thrown where cannot retrieve saved game from a file
 *
 * @see RuntimeException
 */
public class RetrieveSavedGameException extends RuntimeException {

    private static final long serialVersionUID = -3321772790418116606L;


    public RetrieveSavedGameException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
