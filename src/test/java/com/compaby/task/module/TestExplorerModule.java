package com.compaby.task.module;

import com.company.task.GameCommand;
import com.company.task.action.*;
import com.company.task.common.model.Fighter;
import com.company.task.common.model.FighterType;
import com.company.task.common.model.GameData;
import com.company.task.common.model.Weapon;
import com.company.task.common.module.Module;
import com.company.task.common.util.AppConstants;
import com.company.task.common.util.FileUtil;
import com.company.task.module.ExplorerModule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.Random;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PrepareForTest(FileUtil.class)
public class TestExplorerModule extends BaseTestConfig {


    private Module explorerModule;

    @Before
    public void beforeTest() {
        super.beforeTest();
        GameData gameData = new GameData();
        gameData.setFighter(new Fighter.FighterBuilder()
                .withLevel(AppConstants.DEFAULT_LEVEL_ON_CREATION)
                .withUsername("username")
                .withFighterType(FighterType.ATTACKER)
                .withEnergy(AppConstants.DEFAULT_ENERGY_ON_CREATION)
                .build()); // any fighter
        explorerModule = new ExplorerModule(gameData);
    }

    @Test
    public void testMoveActionUnluckyMove() {
        explorerModule.getGameData().setRandom(mock(Random.class));
        when(explorerModule.getGameData().getRandom().nextInt(Mockito.anyInt())).thenReturn(MoveAction.MOVE_FORWARD_ACTION); // not matching with MOVE_BACK command
        CommandAction action = new MoveAction(GameCommand.MOVE_BACK);
        action.execute(reader, explorerModule);

        assertNull(explorerModule.getGameData().getFighter().getWeapons()); // no weapons
    }

    @Test
    public void testMoveActionLuckyMove() {
        explorerModule.getGameData().setRandom(mock(Random.class));
        when(explorerModule.getGameData().getRandom().nextInt(Mockito.anyInt())).thenReturn(MoveAction.MOVE_FORWARD_ACTION); // not matching with MOVE_BACK command
        when(explorerModule.getGameData().getRandom().nextInt(Mockito.anyInt())).thenReturn(MoveAction.MOVE_BACK_ACTION); // not matching with MOVE_BACK command

        CommandAction action = new MoveAction(GameCommand.MOVE_BACK);
        action.execute(reader, explorerModule);

        assertNotNull(explorerModule.getGameData().getFighter().getWeapons());
        assertThat(explorerModule.getGameData().getFighter().getWeapons(), hasSize(1)); // one weapon is added
    }

    @Test
    public void testListWeaponsAction() {
        GameData gameData = explorerModule.getGameData();
        explorerModule.getGameData().getFighter().addWeapon(Weapon.generateRandomWeapon(gameData.getFighter().getLevel(), gameData.getRandom()));
        explorerModule.getGameData().getFighter().addWeapon(Weapon.generateRandomWeapon(gameData.getFighter().getLevel(), gameData.getRandom()));

        Module explorerModule = new ExplorerModule(gameData);

        CommandAction action = new ListWeaponsAction();
        Module returnedModule = action.execute(reader, explorerModule);

        assertEquals(explorerModule, returnedModule);
    }

    @Test
    public void testSelectWeaponAction() {
        int firstWeaponId = 0;
        when(reader.read())
                .thenReturn(UNSUPPORTED_COMMAND) // again should request for id
                .thenReturn(String.valueOf(firstWeaponId));

        GameData gameData = explorerModule.getGameData();
        gameData.getFighter().addWeapon(Weapon.generateRandomWeapon(gameData.getFighter().getLevel(), gameData.getRandom()));
        gameData.getFighter().addWeapon(Weapon.generateRandomWeapon(gameData.getFighter().getLevel(), gameData.getRandom()));

        Module explorerModule = new ExplorerModule(gameData);
        CommandAction action = new SelectWeaponAction();
        Module returnedModule = action.execute(reader, explorerModule);

        assertEquals(explorerModule, returnedModule);
        assertNotNull(gameData.getFighter().getHoldingWeapon());
        assertEquals(gameData.getFighter().getHoldingWeapon(), gameData.getFighter().getWeapons().get(firstWeaponId));
    }

    @Test
    public void testSaveGameAction() throws Exception {
        CommandAction action = new SaveGameAction();

        PowerMockito.spy(FileUtil.class);
        PowerMockito.doNothing().when(FileUtil.class);

        Module returnedModule = action.execute(reader, explorerModule);

        assertEquals(explorerModule, returnedModule);
        assertEquals(explorerModule.getGameData().getFighter().getUsername(), returnedModule.getGameData().getFighter().getUsername());
    }
}
