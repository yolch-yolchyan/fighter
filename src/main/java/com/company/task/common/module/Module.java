package com.company.task.common.module;

import com.company.task.common.io.InputReader;
import com.company.task.common.model.GameData;

import java.io.Serializable;

public interface Module extends Serializable {

    /**
     * runs the module with the supported actions,
     * which are returned separately for each module
     *
     * @param reader to read user inputs
     */
    void run(InputReader reader);

    /**
     * returns game data current state
     *
     * @return game data current state, fighter info, etc.
     */
    GameData getGameData();

}
