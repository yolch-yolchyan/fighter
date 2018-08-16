package com.company.task.common.model;

import com.company.task.common.util.AppConstants;

public enum FighterType {

    ATTACKER(AppConstants.FIGHTER_TYPE_ATTACKER_ATTACK_POWER, AppConstants.FIGHTER_TYPE_ATTACKER_DEFENCE_POWER),
    DEFENDER(AppConstants.FIGHTER_TYPE_DEFENDER_ATTACK_POWER, AppConstants.FIGHTER_TYPE_DEFENDER_DEFENCE_POWER);

    private int addToAttackPower;

    private int addToDefencePower;

    FighterType(int addToAttackPower, int addToDefencePower) {
        this.addToAttackPower = addToAttackPower;
        this.addToDefencePower = addToDefencePower;
    }

    public int getAddToAttackPower() {
        return addToAttackPower;
    }

    public int getAddToDefencePower() {
        return addToDefencePower;
    }
}
