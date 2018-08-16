package com.compaby.task.module;

import com.company.task.GameCommand;
import com.company.task.action.CommandAction;
import com.company.task.action.CommandHelpAction;
import com.company.task.action.NewGameAction;
import com.company.task.action.ResumeSavedGameAction;
import com.company.task.common.model.FighterType;
import com.company.task.common.model.GameData;
import com.company.task.common.module.Module;
import com.company.task.common.util.AppConstants;
import com.company.task.common.util.FileUtil;
import com.company.task.module.ExplorerModule;
import com.company.task.module.GameStartingModule;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


@PrepareForTest(FileUtil.class)
public class TestGameStartingModule extends BaseTestConfig {

    private static final String UNSUPPORTED_COMMAND = "unsupported_command_";

    @Test
    public void testNewGameAction() {
        String username = "player_new";

        when(reader.read())
                .thenReturn(username)
                .thenReturn(UNSUPPORTED_COMMAND)
                .thenReturn(GameCommand.FIGHTER_ATTACKER.getCommand());

        GameData gameData = new GameData();
        CommandAction action = new NewGameAction();
        action.execute(reader, new GameStartingModule(gameData));

        assertNotNull(gameData.getFighter());
        assertEquals(gameData.getFighter().getFighterType(), FighterType.ATTACKER);
        assertEquals(gameData.getFighter().getUsername(), username);
        assertEquals(gameData.getFighter().getEnergy(), AppConstants.DEFAULT_ENERGY_ON_CREATION);
        assertNull(gameData.getFighter().getWeapons()); // new user doesn't have weapons
    }

    @Test
    public void testResumeSavedGame() {
        PowerMockito.mockStatic(FileUtil.class);
        String anyUsername = "any_username";

        when(reader.read())
                .thenReturn(anyUsername);

        GameData gameData = new GameData();
        Module explorerModule = new ExplorerModule(gameData);

        PowerMockito.when(FileUtil.readObjectFromFile(Mockito.anyString())).thenReturn(new ExplorerModule(new GameData()));
        PowerMockito.when(FileUtil.isFileExists(Mockito.anyString())).thenReturn(true);

        CommandAction action = new ResumeSavedGameAction();
        Module retrievedModule = action.execute(reader, explorerModule);

        assertNotNull(retrievedModule);
        assertNotEquals(retrievedModule, explorerModule); // a new state of the game, module data changed
        assertNotEquals(retrievedModule.getGameData(), explorerModule.getGameData());
    }

    @Test
    public void testResumeNonExistSaveGame() {
        PowerMockito.mockStatic(FileUtil.class);
        String anyUsername = "any_username";

        when(reader.read())
                .thenReturn(anyUsername);

        GameData gameData = new GameData();
        Module explorerModule = new ExplorerModule(gameData);

        PowerMockito.when(FileUtil.readObjectFromFile(Mockito.anyString())).thenReturn(new ExplorerModule(new GameData()));
        PowerMockito.when(FileUtil.isFileExists(Mockito.anyString())).thenReturn(false);

        CommandAction action = new ResumeSavedGameAction();
        Module retrievedModule = action.execute(reader, explorerModule);

        assertNotNull(retrievedModule);
        assertEquals(retrievedModule, explorerModule); // the same state of the game, nothing loaded
        assertEquals(retrievedModule.getGameData(), explorerModule.getGameData());
    }

    @Test
    public void testGameStartingModuleQuitGame() {
        when(reader.read())
                .thenReturn(UNSUPPORTED_COMMAND)
                .thenReturn(GameCommand.QUIT_GAME.getCommand())
                .thenReturn(UNSUPPORTED_COMMAND) // to check if it continues asking
                .thenReturn(AppConstants.YES_CONFIRM);

        Module gameStartingModule = new GameStartingModule(new GameData());
        gameStartingModule.run(reader);
    }

    @Test
    public void testGameStartingModuleQuitGameNoThenYes() {
        when(reader.read())
                .thenReturn(GameCommand.QUIT_GAME.getCommand())
                .thenReturn(AppConstants.NO_REJECT) // to check if it continues asking
                .thenReturn(GameCommand.QUIT_GAME.getCommand())
                .thenReturn(AppConstants.YES_CONFIRM);

        Module gameStartingModule = new GameStartingModule(new GameData());
        gameStartingModule.run(reader);
    }

    @Test
    public void testCommandsHelpAction() {
        Module module = new GameStartingModule(new GameData());

        CommandAction action = new CommandHelpAction();
        Module returnedModule = action.execute(reader, module);

        assertNotNull(returnedModule);
        assertEquals(module, returnedModule);
    }
}
