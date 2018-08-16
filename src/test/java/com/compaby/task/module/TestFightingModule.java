package com.compaby.task.module;

import com.company.task.GameCommand;
import com.company.task.action.CommandAction;
import com.company.task.action.FightingAction;
import com.company.task.common.model.Fighter;
import com.company.task.common.model.FighterType;
import com.company.task.common.model.GameData;
import com.company.task.common.model.Weapon;
import com.company.task.common.module.Module;
import com.company.task.common.util.AppConstants;
import com.company.task.module.ExplorerModule;
import com.company.task.module.FightingModule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.Random;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PrepareForTest(Random.class)
public class TestFightingModule extends BaseTestConfig {

    private Module fightingModule;

    private Fighter opponentFighter;


    @Before
    public void beforeTest() {
        super.beforeTest();

        GameData gameData = new GameData();
        gameData.setFighter(new Fighter.FighterBuilder()
                .withLevel(AppConstants.DEFAULT_LEVEL_ON_CREATION)
                .withUsername("username")
                .withFighterType(FighterType.ATTACKER)
                .withEnergy(AppConstants.DEFAULT_ENERGY_ON_CREATION)
                .withFightEnergy(AppConstants.DEFAULT_ENERGY_ON_CREATION)
                .build()); // any fighter
        gameData.getFighter().calculatePowers();
        this.fightingModule = new FightingModule(gameData);

        this.opponentFighter = new Fighter.FighterBuilder()
                .withLevel(AppConstants.DEFAULT_LEVEL_ON_CREATION)
                .withUsername("adversary")
                .withFighterType(FighterType.ATTACKER)
                .withEnergy(AppConstants.DEFAULT_ENERGY_ON_CREATION)
                .withFightEnergy(AppConstants.DEFAULT_ENERGY_ON_CREATION)
                .build(); // any fighter
        this.opponentFighter.calculatePowers();

        fightingModule.getGameData().setRandom(mock(Random.class));
    }

    @Test
    public void testFightingActionMainCharacterWins() {
        CommandAction action = new FightingAction(GameCommand.ATTACK_ON_CHEST_DEFEND_CHEST, opponentFighter);

        opponentFighter.setFightEnergy(2); // after attack this energy will become negative


        when(fightingModule.getGameData().getRandom().nextInt(Mockito.anyInt()))
                .thenReturn(FightingAction.ATTACK_ON_CHEST_DEFEND_LEGS); // opponent not defending chest while attack on chest

        Module module = action.execute(reader, this.fightingModule);

        assertEquals(module.getClass(), ExplorerModule.class);
        assertThat(opponentFighter.getFightEnergy(), lessThan(AppConstants.LOWEST_ENERGY_TO_FIGHT));
        assertThat(module.getGameData().getFighter().getFightEnergy(), greaterThan(AppConstants.LOWEST_ENERGY_TO_FIGHT));
        assertThat(module.getGameData().getFighter().getLevel(), is(AppConstants.DEFAULT_LEVEL_ON_CREATION + 1)); // one level is up
    }


    @Test
    public void testFightingActionDraw() {
        CommandAction action = new FightingAction(GameCommand.ATTACK_ON_CHEST_DEFEND_CHEST, opponentFighter);

        opponentFighter.setFightEnergy(2); // after attack this energy will become negative
        fightingModule.getGameData().getFighter().setFightEnergy(2); // after attack this energy will become negative


        when(fightingModule.getGameData().getRandom().nextInt(Mockito.anyInt()))
                .thenReturn(FightingAction.ATTACK_ON_LEGS_DEFEND_LEGS); // both sides energy will become 0

        Module module = action.execute(reader, this.fightingModule);

        assertEquals(module.getClass(), ExplorerModule.class);
        assertThat(opponentFighter.getFightEnergy(), lessThan(AppConstants.LOWEST_ENERGY_TO_FIGHT));
        assertThat(module.getGameData().getFighter().getFightEnergy(), lessThan(AppConstants.LOWEST_ENERGY_TO_FIGHT));
        assertThat(module.getGameData().getFighter().getLevel(), is(AppConstants.DEFAULT_LEVEL_ON_CREATION)); // level is not changed due to draw
    }

    @Test
    public void testFightingActionOpponentWins() {
        CommandAction action = new FightingAction(GameCommand.ATTACK_ON_CHEST_DEFEND_CHEST, opponentFighter);

        fightingModule.getGameData().getFighter().setFightEnergy(2); // after attack this energy will become negative


        when(fightingModule.getGameData().getRandom().nextInt(Mockito.anyInt()))
                .thenReturn(FightingAction.ATTACK_ON_LEGS_DEFEND_CHEST); // main character will lose as it is not defending legs

        Module module = action.execute(reader, this.fightingModule);

        assertEquals(module.getClass(), ExplorerModule.class);
        assertThat(module.getGameData().getFighter().getLevel(), is(AppConstants.DEFAULT_LEVEL_ON_CREATION)); // level is not changed due to draw
    }

    @Test
    public void testFightingActionContinueFight() {
        CommandAction action = new FightingAction(GameCommand.ATTACK_ON_CHEST_DEFEND_CHEST, opponentFighter);

        when(fightingModule.getGameData().getRandom().nextInt(Mockito.anyInt()))
                .thenReturn(FightingAction.ATTACK_ON_LEGS_DEFEND_LEGS); // no one will lose

        Module module = action.execute(reader, this.fightingModule);

        assertEquals(module, fightingModule);
        assertThat(module.getGameData().getFighter().getFightEnergy(), greaterThan(AppConstants.LOWEST_ENERGY_TO_FIGHT));
        assertThat(opponentFighter.getFightEnergy(), greaterThan(AppConstants.LOWEST_ENERGY_TO_FIGHT));
        assertThat(module.getGameData().getFighter().getLevel(), is(AppConstants.DEFAULT_LEVEL_ON_CREATION)); // level is not changed due to draw
    }

    @Test
    public void testFightingModuleQuitFightAndQuitGame() {
        when(reader.read())
                .thenReturn(GameCommand.QUIT_FIGHT.getCommand())
                .thenReturn(GameCommand.QUIT_GAME.getCommand())
                .thenReturn(AppConstants.YES_CONFIRM);

        fightingModule.run(reader); // should exit the test
    }

    @Test
    public void testWeaponPowerInAttack() {
        int weaponAttackPower = 1;
        Fighter fighter = fightingModule.getGameData().getFighter();

        Weapon weapon = new Weapon("Knife", weaponAttackPower, 0);
        fightingModule.getGameData().getFighter().addWeapon(weapon);
        fighter.setHoldingWeapon(weapon);

        when(fightingModule.getGameData().getRandom().nextInt(Mockito.anyInt()))
                .thenReturn(weaponAttackPower);

        assertThat(fighter.calculateAttackPower(), is(fighter.getAttackPower() + weaponAttackPower));
    }

    @Test
    public void testWeaponPowerInDefence() {
        int weaponDefencePower = 1;
        Fighter fighter = fightingModule.getGameData().getFighter();

        Weapon weapon = new Weapon("Knife", 0, weaponDefencePower);
        fightingModule.getGameData().getFighter().addWeapon(weapon);
        fighter.setHoldingWeapon(weapon);

        when(fightingModule.getGameData().getRandom().nextInt(Mockito.anyInt()))
                .thenReturn(weaponDefencePower);

        assertThat(fighter.calculateAttackPower(), is(fighter.getDefencePower() + weaponDefencePower));
    }

}
