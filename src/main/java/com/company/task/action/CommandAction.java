package com.company.task.action;

import com.company.task.common.io.InputReader;
import com.company.task.common.module.Module;

/**
 * base command action, from which all commands should
 * to make them executable with views
 */
@FunctionalInterface
public interface CommandAction {

    /**
     * executable method for a particular command
     *
     * @param reader to read inputs
     * @param currentModule currently running module e.g. explore, fight, module game
     * @return next module to run, if null should finish the game
     */
    Module execute(InputReader reader, Module currentModule);

}
