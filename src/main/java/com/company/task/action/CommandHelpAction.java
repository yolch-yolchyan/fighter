package com.company.task.action;

import com.company.task.GameCommand;
import com.company.task.common.io.InputReader;
import com.company.task.common.io.Writer;
import com.company.task.common.io.WriterFactory;
import com.company.task.common.module.Module;

/**
 * implements execute in a way which prints all commands available
 *
 * @see CommandAction
 */
public class CommandHelpAction implements CommandAction {

    private static final Writer writer = WriterFactory.getWriter();

    /**
     * @see CommandAction#execute(InputReader, Module)
     */
    @Override
    public Module execute(InputReader reader, Module currentModule) {
        for (GameCommand gameCommand : GameCommand.values()) {
            writer.writeMessage(gameCommand);
        }
        return currentModule;
    }
}
