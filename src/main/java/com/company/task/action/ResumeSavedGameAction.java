package com.company.task.action;

import com.company.task.common.io.InputReader;
import com.company.task.common.io.Writer;
import com.company.task.common.io.WriterFactory;
import com.company.task.common.module.Module;
import com.company.task.common.util.AppConstants;
import com.company.task.common.util.FileUtil;

import java.io.File;

/**
 * for resuming the saved game by the provided username
 */
public class ResumeSavedGameAction implements CommandAction {

    private static final Writer writer = WriterFactory.getWriter();

    /**
     * @see CommandAction#execute(InputReader, Module)
     */
    @Override
    public Module execute(InputReader reader, Module currentModule) {
        writer.writeMessage("please enter username");
        String username = reader.read().trim();

        String filePath = new StringBuilder()
                .append(System.getProperty("user.dir"))
                .append(File.separator)
                .append(AppConstants.SAVED_GAMES_FILES_DIRECTORY_NAME)
                .append(File.separator)
                .append(username)
                .append(AppConstants.SAVE_GAME_FILE_FORMAT)
                .toString();

        if (FileUtil.isFileExists(filePath)) {
            Module savedModule = (Module) FileUtil.readObjectFromFile(filePath);
            writer.writeMessage("The save state is retrieved");
            return savedModule;
        } else {
            writer.writeError("There is no user saved with username: " + username + " to run the game");
            return currentModule;
        }
    }
}
