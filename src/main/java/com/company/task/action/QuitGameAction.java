package com.company.task.action;

import com.company.task.common.io.InputReader;
import com.company.task.common.io.Writer;
import com.company.task.common.io.WriterFactory;
import com.company.task.common.module.Module;
import com.company.task.common.util.AppConstants;

/**
 * implements execute in a way to exit the game successfully
 *
 * @see CommandAction
 */
public class QuitGameAction implements CommandAction {

    private static final Writer writer = WriterFactory.getWriter();


    /**
     * @see CommandAction#execute(InputReader, Module)
     */
    @Override
    public Module execute(InputReader reader, Module currentModule) {
        writer.writeMessage("Do you want to exit the game (y/n)");
        return confirmationBarResult(reader,  currentModule);
    }

    /**
     * shows confirmation bar for exit with (y/n)
     *
     * @param reader
     * @param currentModule
     */
    private Module confirmationBarResult(InputReader reader, Module currentModule) {
        Module module = null;
        String input = reader.read();
        if (input.equalsIgnoreCase(AppConstants.NO_REJECT)) {
            module = currentModule;
        } else if (!input.equalsIgnoreCase(AppConstants.YES_CONFIRM)) {
            writer.writeMessage("Please enter '" + AppConstants.YES_CONFIRM + "' OR '" + AppConstants.NO_REJECT + "'");
            module = confirmationBarResult(reader, currentModule);
        }
        return module;
    }
}
