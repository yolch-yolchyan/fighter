package com.company.task.common.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * holds data about a weapon.
 *
 * Although we can use the builder patter to build this object,
 * I think current three parameters are easy to pass through constructor without much complication
 *
 * @see Serializable
 */
public class Weapon implements Serializable {

    private static final long serialVersionUID = -4848429975883766234L;

    public static final List<String> GUN_NAMES = Arrays.asList("Knife", "Sword", "Spear", "Jian", "Katana");


    /**
     * initialize weapon parameters by the given parameters
     *
     * @param name to assign
     * @param addToAttack to assign
     * @param addToDefence to assign
     */
    public Weapon(String name, Integer addToAttack, Integer addToDefence) {
        this.name = name;
        this.addToAttack = addToAttack;
        this.addToDefence = addToDefence;
    }

    private String name;

    private Integer addToAttack;

    private Integer addToDefence;

    public Integer getAddToAttack() {
        return addToAttack;
    }

    public void setAddToAttack(Integer addToAttack) {
        this.addToAttack = addToAttack;
    }

    public Integer getAddToDefence() {
        return addToDefence;
    }

    public void setAddToDefence(Integer addToDefence) {
        this.addToDefence = addToDefence;
    }

    public String getName() {
        return name;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "name: " + name + ", addToAttack: " + addToAttack + ", addToDefence: " + addToDefence;
    }

    /**
     * generates a random weapon and returns
     *
     * @param level the level for which a weapon should be generated
     * @param r to use for generating a random weapon
     * @return generated weapon
     */
    public static Weapon generateRandomWeapon(Integer level, Random r) {
        return new Weapon(GUN_NAMES.get(r.nextInt(GUN_NAMES.size())), r.nextInt(level + 1), r.nextInt(level + 1)); // generates a weapon with a power depends on a fighter level;
    }
}
