package com.company.task.action;

import com.company.task.GameCommand;
import com.company.task.common.io.InputReader;
import com.company.task.common.io.Writer;
import com.company.task.common.io.WriterFactory;
import com.company.task.common.model.Fighter;
import com.company.task.common.model.GameData;
import com.company.task.common.module.Module;
import com.company.task.common.util.AppConstants;
import com.company.task.module.ExplorerModule;

import java.util.*;

/**
 * handles the action in a fight
 */
public class FightingAction implements CommandAction {

    private static final Writer writer = WriterFactory.getWriter();


    private static final Map<Integer, GameCommand> SUPPORTED_COMMANDS;

    public static final int ATTACK_ON_CHEST_DEFEND_CHEST = 0;
    public static final int ATTACK_ON_CHEST_DEFEND_LEGS = 1;
    public static final int ATTACK_ON_LEGS_DEFEND_CHEST = 2;
    public static final int ATTACK_ON_LEGS_DEFEND_LEGS = 3;

    static {
        SUPPORTED_COMMANDS = new HashMap<>();
        SUPPORTED_COMMANDS.put(ATTACK_ON_CHEST_DEFEND_CHEST, GameCommand.ATTACK_ON_CHEST_DEFEND_CHEST);
        SUPPORTED_COMMANDS.put(ATTACK_ON_CHEST_DEFEND_LEGS, GameCommand.ATTACK_ON_CHEST_DEFEND_LEGS);
        SUPPORTED_COMMANDS.put(ATTACK_ON_LEGS_DEFEND_CHEST, GameCommand.ATTACK_ON_LEGS_DEFEND_CHEST);
        SUPPORTED_COMMANDS.put(ATTACK_ON_LEGS_DEFEND_LEGS, GameCommand.ATTACK_ON_LEGS_DEFEND_LEGS);
    }

    // defence attack combinations
    private static final Set<GameCommand> CHEST_ATTACK_COMMANDS = new HashSet<>(Arrays.asList(GameCommand.ATTACK_ON_CHEST_DEFEND_CHEST, GameCommand.ATTACK_ON_CHEST_DEFEND_LEGS));
    private static final Set<GameCommand> CHEST_DEFENCE_COMMANDS = new HashSet<>(Arrays.asList(GameCommand.ATTACK_ON_CHEST_DEFEND_CHEST, GameCommand.ATTACK_ON_LEGS_DEFEND_CHEST));

    private static final Set<GameCommand> LEGS_ATTACK_COMMANDS = new HashSet<>(Arrays.asList(GameCommand.ATTACK_ON_LEGS_DEFEND_CHEST, GameCommand.ATTACK_ON_LEGS_DEFEND_LEGS));
    private static final Set<GameCommand> LEGS_DEFENCE_COMMANDS = new HashSet<>(Arrays.asList(GameCommand.ATTACK_ON_CHEST_DEFEND_LEGS, GameCommand.ATTACK_ON_LEGS_DEFEND_LEGS));


    private GameCommand mainCharacterAction;

    private Fighter opponentFighter;

    /**
     * initialize class properties by the given parameters
     *
     * @param mainCharacterAction action by a user
     * @param opponentFighter opponent fighter to fight
     */
    public FightingAction(GameCommand mainCharacterAction, Fighter opponentFighter) {
        this.mainCharacterAction = mainCharacterAction;
        this.opponentFighter = opponentFighter;
    }

    /**
     * @see CommandAction#execute(InputReader, Module)
     */
    @Override
    public Module execute(InputReader reader, Module currentModule) {
        GameData gameData = currentModule.getGameData();
        Fighter mainCharacter = gameData.getFighter();

        GameCommand opponentAction = SUPPORTED_COMMANDS.get(gameData.getRandom().nextInt(SUPPORTED_COMMANDS.size())); // not more than the size of existing supported fight actions
        writer.writeMessage("you did action: " + mainCharacterAction);
        writer.writeMessage("opponent did action: " + opponentAction);

        initDamageToDefender(opponentAction, mainCharacterAction, opponentFighter, mainCharacter);
        initDamageToDefender(mainCharacterAction, opponentAction, mainCharacter, opponentFighter);

        return nextStep(currentModule);
    }

    /**
     *
     * @param currentModule current running module
     * @return next module for running
     */
    private Module nextStep(Module currentModule) {
        Fighter mainCharacter = currentModule.getGameData().getFighter();
        writer.writeMessage("your fighting energy is: " + mainCharacter.getFightEnergy());
        writer.writeMessage("opponent fighting energy is: " + opponentFighter.getFightEnergy());

        if (mainCharacter.getFightEnergy() <= AppConstants.LOWEST_ENERGY_TO_FIGHT && opponentFighter.getFightEnergy() <= AppConstants.LOWEST_ENERGY_TO_FIGHT) {
            return draw(currentModule);
        } else if (mainCharacter.getFightEnergy() <= AppConstants.LOWEST_ENERGY_TO_FIGHT) {
            return mainCharacterLost(currentModule);
        } else if (opponentFighter.getFightEnergy() <= AppConstants.LOWEST_ENERGY_TO_FIGHT) {
            return mainCharacterWin(currentModule);
        } else {
            return nextAttack(currentModule);
        }
    }

    /**
     *
     * @param currentModule current running module
     * @return
     */
    private Module draw(Module currentModule) {
        writer.writeMessage("No one won. You both are killed.");
        return new ExplorerModule(currentModule.getGameData());
    }

    /**
     * this method is returned when the main character lost the game, meaning that
     * the main character have negative fighting energy while the opponent has positive
     *
     * @param currentModule current running module
     * @return explore module, as the fight is over
     */
    private Module mainCharacterLost(Module currentModule) {
        writer.writeMessage("You are killed. You lost this battle.");
        return new ExplorerModule(currentModule.getGameData());
    }

    /**
     * this method is returned when the main character wins the opponent, meaning that
     * the main character still have positive fighting energy while the opponent doesn't have it
     *
     * @param currentModule current running module
     * @return explore module, as the fight is over
     */
    private Module mainCharacterWin(Module currentModule) {
        Fighter fighter = currentModule.getGameData().getFighter();
        fighter.incrementLevel();
        writer.writeMessage("You won. You gained experience and your level is " + fighter.getLevel() + " now.");
        return new ExplorerModule(currentModule.getGameData());
    }

    /**
     * this method is returned when both attacker and defender still have fighting energy to fight
     *
     * @param currentModule current running module
     * @return currentModule, should continue the fight
     */
    private Module nextAttack(Module currentModule) {
        writer.writeMessage("Please do the next attack on the opponent.");
        return currentModule;
    }

    /**
     *
     * @param attackerAction
     * @param defenderAction
     * @param attacker
     * @param defender
     */
    private void initDamageToDefender(GameCommand attackerAction, GameCommand defenderAction, Fighter attacker, Fighter defender) {
        int defenderPower = defender.calculateDefencePower();
        int attackPower = attacker.calculateAttackPower();

        boolean defend = isDefended(attackerAction, defenderAction);
        int damage = defend ? attackPower - defenderPower : attackPower;

        defender.setFightEnergy(defender.getFightEnergy() - damage);
    }

    /**
     *
     * @param attackerAction
     * @param defenderAction
     * @return should be defended
     */
    private boolean isDefended(GameCommand attackerAction, GameCommand defenderAction) {
        if (CHEST_ATTACK_COMMANDS.contains(attackerAction) && CHEST_DEFENCE_COMMANDS.contains(defenderAction)) {
            return true;
        } else if (LEGS_ATTACK_COMMANDS.contains(attackerAction) && LEGS_DEFENCE_COMMANDS.contains(defenderAction)) {
            return true;
        }
        return false;
    }

}
