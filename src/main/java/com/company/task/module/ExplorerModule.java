package com.company.task.module;

import com.company.task.GameCommand;
import com.company.task.action.*;
import com.company.task.common.io.Writer;
import com.company.task.common.io.WriterFactory;
import com.company.task.common.model.GameData;
import com.company.task.common.module.AbstractModuleRunner;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * running exploring mode of the game. Supports all actions for exploring and moving to other modules. e.g. fighting
 *
 * @see AbstractModuleRunner
 */
public class ExplorerModule extends AbstractModuleRunner {

    private static final long serialVersionUID = 3812287888605211653L;


    private static final Writer writer = WriterFactory.getWriter();


    public ExplorerModule(GameData gameData) {
        super(gameData);
    }


    /**
     * @see AbstractModuleRunner#moduleSupportedActions()
     */
    @Override
    public Map<GameCommand, CommandAction> moduleSupportedActions() {
        Map<GameCommand, CommandAction> actions = new LinkedHashMap<>();
        actions.put(GameCommand.MOVE_FORWARD, new MoveAction(GameCommand.MOVE_FORWARD));
        actions.put(GameCommand.MOVE_RIGHT, new MoveAction(GameCommand.MOVE_RIGHT));
        actions.put(GameCommand.MOVE_LEFT, new MoveAction(GameCommand.MOVE_LEFT));
        actions.put(GameCommand.MOVE_BACK, new MoveAction(GameCommand.MOVE_BACK));
        actions.put(GameCommand.SELECT_WEAPON, new SelectWeaponAction());
        actions.put(GameCommand.LIST_WEAPONS, new ListWeaponsAction());
        actions.put(GameCommand.ATTACK, (reader, currentModule) -> new FightingModule(currentModule.getGameData()));
        actions.put(GameCommand.SHOW_MAIN_CHARACTER_CHARATERISTICS, (reader, currentModule) -> {
            writer.writeMessage(gameData.getFighter());
            return currentModule;
        });
        actions.put(GameCommand.SAVE_GAME, new SaveGameAction());
        actions.put(GameCommand.QUIT_GAME, new QuitGameAction());
        actions.put(GameCommand.COMMAND_HELP, new CommandHelpAction());
        return actions;
    }

}
