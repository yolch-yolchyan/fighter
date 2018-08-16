package com.company.task;

import java.util.HashMap;
import java.util.Map;

/**
 * holds all the commands which are supported and executed by the game
 */
public enum GameCommand {

    NEW_GAME("N","to run a new game"),
    RESUME_SAVED_GAME("R", "to resume a saved game"),
    QUIT_GAME("E", "to exit the game"),
    COMMAND_HELP("H", "to list all the commands"),

    FIGHTER_ATTACKER("AT", "to create attacker fighter"),
    FIGHTER_DEFENDER("DE", "to create defender fighter"),

    MOVE_FORWARD("W", "to move forward"),
    MOVE_RIGHT("D", "to move right"),
    MOVE_LEFT("A", "to move left"),
    MOVE_BACK("S", "to move back"),
    ATTACK("U", "to attack"),
    LIST_WEAPONS("L", "to list weapons"),
    SELECT_WEAPON("C", "to choose weapon"),
    SHOW_MAIN_CHARACTER_CHARATERISTICS("DD", "to show your details"),
    SAVE_GAME("SS", "to save the game"),


    QUIT_FIGHT("Q", "to accept defeat and quit the fight"),
    ATTACK_ON_CHEST_DEFEND_CHEST("CC", "to attack on chest and defend it"),
    ATTACK_ON_CHEST_DEFEND_LEGS("CL", "to attack on chest and defend legs"),
    ATTACK_ON_LEGS_DEFEND_CHEST("LC", "to attack on legs and defend chest"),
    ATTACK_ON_LEGS_DEFEND_LEGS("LL", "to attack on legs and defend it");


    private String command;

    private String description;

    GameCommand(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "enter '" + command + "' " + description;
    }




    private static Map<String, GameCommand> mapper;

    static {
        mapper = new HashMap<>();
        for (GameCommand gameCommand : GameCommand.values()) {
            mapper.put(gameCommand.command, gameCommand);
        }
    }

    public static GameCommand getByCommand(String command) {
        return mapper.get(command.toUpperCase());
    }

}
