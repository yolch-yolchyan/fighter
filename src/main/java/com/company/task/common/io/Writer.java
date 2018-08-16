package com.company.task.common.io;

/**
 * has method for showing/printing messages.
 *
 * strategy pattern usage
 */
public interface Writer {

    /**
     * prints given object as error
     *
     * @param o
     */
    void writeError(Object o);

    /**
     * prints given object as message
     *
     * @param o
     */
    void writeMessage(Object o);

}
