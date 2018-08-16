package com.company.task.module;

import com.company.task.GameCommand;
import com.company.task.action.*;
import com.company.task.common.model.GameData;
import com.company.task.common.module.AbstractModuleRunner;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * runs the game. Supports all the commands to module a new or resume game
 *
 * @see AbstractModuleRunner
 */
public class GameStartingModule extends AbstractModuleRunner {

    public GameStartingModule(GameData gameData) {
        super(gameData);
    }

    /**
     * @see AbstractModuleRunner#moduleSupportedActions()
     */
    @Override
    public Map<GameCommand, CommandAction> moduleSupportedActions() {
        Map<GameCommand, CommandAction> actions = new LinkedHashMap<>();
        actions.put(GameCommand.COMMAND_HELP, new CommandHelpAction());
        actions.put(GameCommand.QUIT_GAME, new QuitGameAction());
        actions.put(GameCommand.NEW_GAME, new NewGameAction());
        actions.put(GameCommand.RESUME_SAVED_GAME, new ResumeSavedGameAction());
        return actions;
    }

}
