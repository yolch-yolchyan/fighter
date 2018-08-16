package com.company.task.common.model;

import java.io.Serializable;
import java.util.Random;

/**
 * holds specific state of the game. e.g. main fighter data,
 * could be expanded when adding more functionality
 *
 * @see Serializable
 */
public class GameData implements Serializable {

    private static final long serialVersionUID = 5788835902995314195L;


    private Fighter fighter;


    private Random random = new Random();


    public Fighter getFighter() {
        return fighter;
    }

    public void setFighter(Fighter fighter) {
        this.fighter = fighter;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}
