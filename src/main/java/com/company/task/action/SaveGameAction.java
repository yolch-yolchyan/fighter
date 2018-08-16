package com.company.task.action;

import com.company.task.common.io.InputReader;
import com.company.task.common.io.Writer;
import com.company.task.common.io.WriterFactory;
import com.company.task.common.module.Module;
import com.company.task.common.util.AppConstants;
import com.company.task.common.util.FileUtil;

import java.io.File;

/**
 * action command class for saving the state of the fighter
 */
public class SaveGameAction implements CommandAction {

    private static final Writer writer = WriterFactory.getWriter();


    /**
     * @see CommandAction#execute(InputReader, Module)
     */
    @Override
    public Module execute(InputReader reader, Module currentModule) {
        String filePath = new StringBuilder()
                .append(System.getProperty("user.dir"))
                .append(File.separator)
                .append(AppConstants.SAVED_GAMES_FILES_DIRECTORY_NAME)
                .append(File.separator)
                .append(currentModule.getGameData().getFighter().getUsername())
                .append(AppConstants.SAVE_GAME_FILE_FORMAT)
                .toString();

        createSaveDirectoryIfNotExists();
        FileUtil.writeObjectIntoFile(currentModule, filePath);

        writer.writeMessage("the game state is successfully saved under '" + currentModule.getGameData().getFighter().getUsername() + "' username");
        return currentModule;
    }

    /**
     * check if save game directory doesn't exists, creates it
     */
    private void createSaveDirectoryIfNotExists() {
        String directoryPath = new StringBuilder()
                .append(System.getProperty("user.dir"))
                .append(File.separator)
                .append(AppConstants.SAVED_GAMES_FILES_DIRECTORY_NAME)
                .toString();

        File saveDirectory = new File(directoryPath);
        if (!saveDirectory.exists()) {
            saveDirectory.mkdir();
        }
    }
}
