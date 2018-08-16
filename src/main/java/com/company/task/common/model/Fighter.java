package com.company.task.common.model;


import com.company.task.common.util.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * holds data about a character
 *
 * @see Serializable
 */
public class Fighter implements Serializable {

    private static final long serialVersionUID = 4710343242939228985L;

    /**
     * builder pattern usage for class fighter
     */
    public static class FighterBuilder {

        private String username;

        private Integer energy;

        private Integer defencePower;

        private Integer attackPower;

        private Integer level;

        private FighterType fighterType;

        private Weapon holdingWeapon;

        private int fightEnergy;

        public FighterBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public FighterBuilder withEnergy(Integer energy) {
            this.energy = energy;
            return this;
        }

        public FighterBuilder withDefencePower(Integer defencePower) {
            this.defencePower = defencePower;
            return this;
        }

        public FighterBuilder withAttackPower(Integer attackPower) {
            this.attackPower = attackPower;
            return this;
        }

        public FighterBuilder withLevel(Integer level) {
            this.level = level;
            return this;
        }

        public FighterBuilder withFighterType(FighterType fighterType) {
            this.fighterType = fighterType;
            return this;
        }

        public FighterBuilder withHoldingWeapon(Weapon holdingWeapon) {
            this.holdingWeapon = holdingWeapon;
            return this;
        }

        public FighterBuilder withFightEnergy(int fightEnergy) {
            this.fightEnergy = fightEnergy;
            return this;
        }

        public Fighter build() {
            Fighter fighter = new Fighter();

            fighter.setAttackPower(this.attackPower);
            fighter.setDefencePower(this.defencePower);
            fighter.setFighterType(this.fighterType);
            fighter.setHoldingWeapon(this.holdingWeapon);
            fighter.setLevel(this.level);
            fighter.setUsername(this.username);
            fighter.setEnergy(this.energy);
            fighter.setFightEnergy(this.fightEnergy);

            return fighter;
        }
    }


    private String username;

    private Integer energy;

    private Integer defencePower;

    private Integer attackPower;

    private Integer level;

    private List<Weapon> weapons;

    private Weapon holdingWeapon;

    private FighterType fighterType;

    private int fightEnergy;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getEnergy() {
        return energy;
    }

    public void setEnergy(Integer energy) {
        this.energy = energy;
    }

    public Integer getDefencePower() {
        return defencePower;
    }

    public void setDefencePower(Integer defencePower) {
        this.defencePower = defencePower;
    }

    public Integer getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(Integer attackPower) {
        this.attackPower = attackPower;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
    }

    public Weapon getHoldingWeapon() {
        return holdingWeapon;
    }

    public void setHoldingWeapon(Weapon holdingWeapon) {
        this.holdingWeapon = holdingWeapon;
    }

    public FighterType getFighterType() {
        return fighterType;
    }

    public void setFighterType(FighterType fighterType) {
        this.fighterType = fighterType;
    }

    public int getFightEnergy() {
        return fightEnergy;
    }

    public void setFightEnergy(int fightEnergy) {
        this.fightEnergy = fightEnergy;
    }

    /**
     * adding a weapon into the the list, fighter
     * can't hold more weapons than it's level, and
     * if it already has weapons with it's level
     * the first weapon will be replaced with the provided weapon
     *
     * @param weapon to add
     */
    public void addWeapon(Weapon weapon) {
        if (weapons == null) {
            weapons = new ArrayList<>();
        }

        // if max number of weapons is reached, it will remove the first weapon and add a new weapon instead of it
        if (AppConstants.MAX_NUMBER_OF_WEAPONS_CAN_BE_HOLD == weapons.size()) {
            weapons.remove(0);
        }
        weapons.add(weapon);
    }

    /**
     * attack and defence power depends on level
     */
    public void calculatePowers() {
        attackPower = level * fighterType.getAddToAttackPower();
        defencePower = level * fighterType.getAddToDefencePower();
    }

    /**
     * calculates total attack power of the fighter, if weapon exists
     * adds weapon attack power to the fighter attack power and returns,
     * otherwise returns fighter attack power
     *
     * @return total attack power
     */
    public int calculateAttackPower() {
        int power = this.getAttackPower();
        if (this.getHoldingWeapon() != null) {
            power += this.getHoldingWeapon().getAddToAttack();
        }
        return power;
    }

    /**
     * calculates total defence power of fighter, if weapon exists
     * adds weapon defence power to the fighter defence power and returns,
     * otherwise returns fighter power
     *
     * @return total defence power
     */
    public int calculateDefencePower() {
        int power = this.getDefencePower();
        if (this.getHoldingWeapon() != null) {
            power += this.getHoldingWeapon().getAddToDefence();
        }
        return power;
    }

    /**
     * increments level and initialize energy, attackPower and defencePower accordingly
     */
    public void incrementLevel() {
        level = level + AppConstants.LEVEL_INCREMENT_VALUE;
        calculatePowers();
        calculateEnergy();
    }

    public void calculateEnergy() {
        energy = level * AppConstants.ENERGY_LEVEL_MULTIPLY_VALUE;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "Fighter [" +
                "username:" + username +
                ", attackPower: " + attackPower +
                ", defendPower: " + defencePower +
                ", level: " + level +
                ", energy: " + energy +
                ", holdingWeapon: " + (holdingWeapon == null ? "empty" : holdingWeapon.toString()) +
                ", fighterType: " + fighterType +
                " ]";
    }
}
