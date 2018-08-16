package com.company.task.action;

import com.company.task.common.io.InputReader;
import com.company.task.common.io.Writer;
import com.company.task.common.io.WriterFactory;
import com.company.task.common.model.Weapon;
import com.company.task.common.module.Module;

import java.util.List;

/**
 * listing all available weapons hold bu the main character
 *
 * @see CommandAction
 */
public class ListWeaponsAction implements CommandAction {

    private static final Writer writer = WriterFactory.getWriter();


    /**
     * @see CommandAction#execute(InputReader, Module)
     */
    @Override
    public Module execute(InputReader reader, Module currentModule) {
        List<Weapon> weapons = currentModule.getGameData().getFighter().getWeapons();
        if (weapons != null && !weapons.isEmpty()) {
            for (int i = 0; i < weapons.size(); i++) {
                writer.writeMessage("ID: " + i + ", " + weapons.get(i));
            }
        } else {
            writer.writeMessage("YOU DON'T HAVE WEAPONS");
        }
        return currentModule;
    }
}
