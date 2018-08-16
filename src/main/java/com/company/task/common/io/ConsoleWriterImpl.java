package com.company.task.common.io;

/**
 * implementation of a console based Writer
 *
 * @see Writer
 */
public class ConsoleWriterImpl implements Writer {

    /**
     * @see Writer#writeError(Object)
     */
    @Override
    public void writeError(Object o) {
        System.err.println(o);
    }

    /**
     * @see Writer#writeMessage(Object)
     */
    @Override
    public void writeMessage(Object o) {
        System.out.println(o);
    }
}
