package com.company.task.module;

import com.company.task.GameCommand;
import com.company.task.action.CommandAction;
import com.company.task.action.FightingAction;
import com.company.task.action.QuitGameAction;
import com.company.task.action.SaveGameAction;
import com.company.task.common.io.Writer;
import com.company.task.common.io.WriterFactory;
import com.company.task.common.model.Fighter;
import com.company.task.common.model.FighterType;
import com.company.task.common.model.GameData;
import com.company.task.common.model.Weapon;
import com.company.task.common.module.AbstractModuleRunner;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * running fighting module of the game. Supports all the actions for fighting.
 *
 * @see AbstractModuleRunner
 * @see Serializable
 */
public class FightingModule extends AbstractModuleRunner {

    private static final long serialVersionUID = 1689204304567682791L;

    private static final Writer writer = WriterFactory.getWriter();


    private static final Map<Integer, FighterType> FIGHTER_TYPES;

    private static final Integer FIGHTER_TYPE_ATTACKER_ID = 0;
    private static final Integer FIGHTER_TYPE_DEFENDER_ID = 1;

    private static final Integer FIGHTING_TYPE_ID_RANDOM_MAX_VALUE = 2;

    static {
        FIGHTER_TYPES = new HashMap<>();
        FIGHTER_TYPES.put(FIGHTER_TYPE_ATTACKER_ID, FighterType.ATTACKER);
        FIGHTER_TYPES.put(FIGHTER_TYPE_DEFENDER_ID, FighterType.DEFENDER);
    }



    private Fighter opponentFighter;

    public FightingModule(GameData gameData) {
        super(gameData);
        initOpponentFighter(gameData);
        initSupportedActions();
        writer.writeMessage("Your opponent details are: " + this.opponentFighter);
    }


    /**
     * initialize the opponent of the main fighter for this fight
     *
     * @param gameData
     */
    private void initOpponentFighter(GameData gameData) {
        FighterType fighterType = FIGHTER_TYPES.get(gameData.getRandom().nextInt(FIGHTING_TYPE_ID_RANDOM_MAX_VALUE));

        Fighter mainCharacter = gameData.getFighter();
        mainCharacter.setFightEnergy(mainCharacter.getEnergy()); // temporary energy for fighting

        opponentFighter = new Fighter.FighterBuilder()
                .withFightEnergy(mainCharacter.getEnergy())
                .withEnergy(mainCharacter.getEnergy())
                .withFightEnergy(mainCharacter.getFightEnergy())
                .withFighterType(fighterType)
                .withLevel(mainCharacter.getLevel())
                .withUsername("adversary")
                .withHoldingWeapon(Weapon.generateRandomWeapon(mainCharacter.getLevel(), gameData.getRandom()))
                .build();
        opponentFighter.calculatePowers();
    }


    /**
     * @see AbstractModuleRunner#moduleSupportedActions()
     */
    @Override
    public Map<GameCommand, CommandAction> moduleSupportedActions() {
        Map<GameCommand, CommandAction> actions = new LinkedHashMap<>();
        actions.put(GameCommand.ATTACK_ON_CHEST_DEFEND_CHEST, new FightingAction(GameCommand.ATTACK_ON_CHEST_DEFEND_CHEST, opponentFighter));
        actions.put(GameCommand.ATTACK_ON_CHEST_DEFEND_LEGS, new FightingAction(GameCommand.ATTACK_ON_CHEST_DEFEND_LEGS, opponentFighter));
        actions.put(GameCommand.ATTACK_ON_LEGS_DEFEND_CHEST, new FightingAction(GameCommand.ATTACK_ON_LEGS_DEFEND_CHEST, opponentFighter));
        actions.put(GameCommand.ATTACK_ON_LEGS_DEFEND_LEGS, new FightingAction(GameCommand.ATTACK_ON_LEGS_DEFEND_LEGS, opponentFighter));
        actions.put(GameCommand.QUIT_FIGHT, (reader, currentModule) -> {
            writer.writeMessage("You accepted defeat: redirecting to exploring mode");
            return new ExplorerModule(gameData);
        });
        actions.put(GameCommand.SAVE_GAME, new SaveGameAction());
        actions.put(GameCommand.QUIT_GAME, new QuitGameAction());
        return actions;
    }

    /**
     * calls when deserialization of the object happens.
     *
     * https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html
     *
     * @param in object input stream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        initSupportedActions();
    }

}
