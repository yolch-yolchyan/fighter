package com.company.task.common.io;

/**
 * factory class for writer
 */
public class WriterFactory {

    private static Writer writer;

    private WriterFactory() {
    }

    /**
     * returns singleton writer, if not exists
     * creates and returns
     *
     * @return writer
     */
    public static Writer getWriter() {
        if (writer == null) {
            writer = new ConsoleWriterImpl();
        }
        return writer;
    }
}
