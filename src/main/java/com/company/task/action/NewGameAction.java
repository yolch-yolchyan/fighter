package com.company.task.action;

import com.company.task.GameCommand;
import com.company.task.common.io.InputReader;
import com.company.task.common.io.Writer;
import com.company.task.common.io.WriterFactory;
import com.company.task.common.model.Fighter;
import com.company.task.common.model.FighterType;
import com.company.task.common.module.Module;
import com.company.task.common.util.AppConstants;
import com.company.task.module.ExplorerModule;

import java.util.HashMap;
import java.util.Map;


/**
 * implements execute in a way, to ask a player to create a user before game with a valid provided data
 */
public class NewGameAction implements CommandAction {

    private static final Writer writer = WriterFactory.getWriter();


    private static final Map<GameCommand, FighterType> FIGHTER_TYPES;

    static {
        FIGHTER_TYPES = new HashMap<>();
        FIGHTER_TYPES.put(GameCommand.FIGHTER_ATTACKER, FighterType.ATTACKER);
        FIGHTER_TYPES.put(GameCommand.FIGHTER_DEFENDER, FighterType.DEFENDER);
    }

    /**
     * @see CommandAction#execute(InputReader, Module)
     */
    @Override
    public Module execute(InputReader reader, Module currentModule) {
        writer.writeMessage("please enter username");
        String username = reader.read().trim();


        writer.writeMessage("Please enter fighter type, " + GameCommand.FIGHTER_ATTACKER + " OR " + GameCommand.FIGHTER_DEFENDER);
        FighterType fighterType = enterFighterType(reader);

        Fighter fighter = new Fighter.FighterBuilder()
                .withFighterType(fighterType)
                .withAttackPower(fighterType.getAddToAttackPower())
                .withDefencePower(fighterType.getAddToDefencePower())
                .withLevel(AppConstants.DEFAULT_LEVEL_ON_CREATION)
                .withUsername(username)
                .withEnergy(AppConstants.DEFAULT_ENERGY_ON_CREATION)
                .build();

        currentModule.getGameData().setFighter(fighter);

        return new ExplorerModule(currentModule.getGameData());
    }

    /**
     * waits for fighter type command. If a wrong command is entered,
     * prints error message and asks to reenter fighter type again
     *
     * @param reader
     * @return
     */
    private FighterType enterFighterType(InputReader reader) {
        String command = reader.read();
        FighterType fighterType = FIGHTER_TYPES.get(GameCommand.getByCommand(command));
        if (fighterType == null) {
            writer.writeError("Please enter a valid fighter type, " + GameCommand.FIGHTER_ATTACKER + " OR " + GameCommand.FIGHTER_DEFENDER);
            return enterFighterType(reader);
        }

        return fighterType;
    }
}
