package com.company.task.common.io;

import java.util.Scanner;

/**
 * implementation of a scanner based reader
 *
 * @see Scanner
 */
public class ScannerReaderImpl implements InputReader {

    private Scanner sc;

    public ScannerReaderImpl() {
        sc = new Scanner(System.in);
    }

    /**
     * @see InputReader#read()
     */
    @Override
    public String read() {
        return sc.next();
    }
}
