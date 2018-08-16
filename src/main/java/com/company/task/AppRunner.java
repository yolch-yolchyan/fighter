package com.company.task;

import com.company.task.common.io.InputReader;
import com.company.task.common.io.ScannerReaderImpl;
import com.company.task.common.model.GameData;
import com.company.task.module.GameStartingModule;

/**
 * contains main method for running the game
 */
public class AppRunner {

    /**
     * main method to run the game
     *
     * @param args
     */
    public static void main(String[] args) {
        InputReader reader = new ScannerReaderImpl();

        GameStartingModule gameRunner = new GameStartingModule(new GameData());
        gameRunner.run(reader);
    }

}
