package com.company.task.action;

import com.company.task.common.io.InputReader;
import com.company.task.common.io.Writer;
import com.company.task.common.io.WriterFactory;
import com.company.task.common.model.Fighter;
import com.company.task.common.model.GameData;
import com.company.task.common.module.Module;

/**
 * selects a weapon as a holding weapon
 *
 * @see CommandAction
 */
public class SelectWeaponAction implements CommandAction {

    private static final Writer writer = WriterFactory.getWriter();


    /**
     * @see CommandAction#execute(InputReader, Module)
     */
    @Override
    public Module execute(InputReader reader, Module currentModule) {
        Fighter mainFighter = currentModule.getGameData().getFighter();

        if (mainFighter.getWeapons() != null && !mainFighter.getWeapons().isEmpty()) {
            writer.writeMessage("Please enter a weapon id which you want to hold");
            selectWeapon(reader, currentModule.getGameData());
        } else {
            writer.writeMessage("You don't have any weapons, please explore to find them");
        }
        return currentModule;
    }

    /**
     * outputs message for weapon selection, and then user enters weapon id,
     * if id is correct holds the weapon otherwise shows error message and asks
     * for the right id
     *
     * @param reader
     */
    private void selectWeapon(InputReader reader, GameData gameData) {
        Fighter mainFighter = gameData.getFighter();
        for (int i = 0; i < mainFighter.getWeapons().size(); i++) {
            writer.writeMessage("id: " + i + " ," + mainFighter.getWeapons().get(i));
        }

        try {
            int index = Integer.parseInt(reader.read());
            if (index >= 0 && index < mainFighter.getWeapons().size()) {
                mainFighter.setHoldingWeapon(mainFighter.getWeapons().get(index));
                writer.writeMessage("you are successfully holding the weapon: " + mainFighter.getHoldingWeapon());
            } else {
                writer.writeError("Please select a valid weapon number");
                selectWeapon(reader, gameData);
            }
        } catch (NumberFormatException e) {
            writer.writeError("Weapon id is a number, please insert a number");
            selectWeapon(reader, gameData);
        }
    }
}
