package com.company.task.action;

import com.company.task.GameCommand;
import com.company.task.common.io.InputReader;
import com.company.task.common.io.Writer;
import com.company.task.common.io.WriterFactory;
import com.company.task.common.model.Fighter;
import com.company.task.common.model.GameData;
import com.company.task.common.model.Weapon;
import com.company.task.common.module.Module;

import java.util.HashMap;
import java.util.Map;

/**
 * executing moves during exploring. e.g. forward, left...
 */
public class MoveAction implements CommandAction {

    private static final Writer writer = WriterFactory.getWriter();


    private static final Map<Integer, GameCommand> LUCKY_DIRECTIONS_MOVE;

    public static final Integer MOVE_FORWARD_ACTION = 0;
    public static final Integer MOVE_RIGHT_ACTION = 1;
    public static final Integer MOVE_LEFT_ACTION = 2;
    public static final Integer MOVE_BACK_ACTION = 3;

    static {
        LUCKY_DIRECTIONS_MOVE = new HashMap<>();
        LUCKY_DIRECTIONS_MOVE.put(MOVE_FORWARD_ACTION, GameCommand.MOVE_FORWARD);
        LUCKY_DIRECTIONS_MOVE.put(MOVE_RIGHT_ACTION, GameCommand.MOVE_RIGHT);
        LUCKY_DIRECTIONS_MOVE.put(MOVE_LEFT_ACTION, GameCommand.MOVE_LEFT);
        LUCKY_DIRECTIONS_MOVE.put(MOVE_BACK_ACTION, GameCommand.MOVE_BACK);
    }

    private GameCommand move;

    public MoveAction(GameCommand move) {
        this.move = move;
    }

    /**
     * @see CommandAction#execute(InputReader, Module)
     */
    @Override
    public Module execute(InputReader reader, Module currentModule) {
        GameData gameData = currentModule.getGameData();
        Fighter fighter = gameData.getFighter();

        int luckyDirection = gameData.getRandom().nextInt(LUCKY_DIRECTIONS_MOVE.size()); // not more than the size of possible actions
        GameCommand luckyMove = LUCKY_DIRECTIONS_MOVE.get(luckyDirection);

        if (luckyMove.equals(move)) {
            Weapon weapon = Weapon.generateRandomWeapon(fighter.getLevel(), gameData.getRandom());
            fighter.addWeapon(weapon);
            writer.writeMessage("This was your lucky move, you acquired a new weapon: " + weapon);
        } else {
            writer.writeMessage("Unfortunately this move wasn't a lucky one");
        }

        return currentModule;
    }
}
