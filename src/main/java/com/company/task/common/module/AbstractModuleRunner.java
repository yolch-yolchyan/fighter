package com.company.task.common.module;

import com.company.task.GameCommand;
import com.company.task.action.CommandAction;
import com.company.task.common.io.InputReader;
import com.company.task.common.io.Writer;
import com.company.task.common.io.WriterFactory;
import com.company.task.common.model.GameData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * <p>
 *     base class for module runners. For example, fighting should have a separate module to run.
 * </p>
 *
 * <p>
 *     If later on we should have more modules, like selling and buying, we can extend from this class these modules as well
 * </p>
 */
public abstract class AbstractModuleRunner implements Module {

    private static final Writer writer = WriterFactory.getWriter();


    private transient Map<GameCommand, CommandAction> actions; // serialization is not required

    protected GameData gameData;

    /**
     * initialize gameData by the given parameter and
     * actions by the returned data from a subclass
     *
     * @param gameData to assign
     */
    public AbstractModuleRunner(GameData gameData) {
        this.gameData = gameData;
        initSupportedActions();
    }

    /**
     * initialize actions by the provided data from a subclass
     */
    protected void initSupportedActions() {
        this.actions = moduleSupportedActions();
    }

    /**
     * @see Module#run(InputReader)
     */
    @Override
    public void run(InputReader reader) {
        writer.writeMessage("supported commands are");
        moduleSupportedActions().keySet().forEach(gameCommand -> writer.writeMessage(gameCommand));
        String command = reader.read();
        CommandAction action = actions.get(GameCommand.getByCommand(command));
        if (action != null) {
            Module moduleRunner = action.execute(reader, this);
            if (moduleRunner != null) { // if null is returned, game will be finished
                moduleRunner.run(reader);
            }
        } else {
            writer.writeError("Command " + command + " is not found or not supported at this stage");
            this.run(reader);
        }
    }

    /**
     * @see Module#getGameData()
     */
    @Override
    public GameData getGameData() {
        return gameData;
    }

    /**
     * returns map of supported commands with actions for the particular module
     *
     * @return supported commands for current module
     */
    public abstract Map<GameCommand, CommandAction> moduleSupportedActions();

    /**
     * calls when deserialization of the object happens.
     *
     * https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html
     *
     * @param in object input stream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        initSupportedActions();
    }

}
