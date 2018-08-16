package com.company.task.common.util;

import com.company.task.common.exception.RetrieveSavedGameException;
import com.company.task.common.exception.SaveGameException;

import java.io.*;

/**
 * utility class for working with files
 */
public class FileUtil {

    /**
     * writes an Serializable object in a file
     *
     * @see Serializable
     * @param object to write
     * @param filePath to save
     */
    public static void writeObjectIntoFile(Serializable object, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(object);
        } catch (IOException e) {
            throw new SaveGameException("Cannot save game for file: " + filePath, e);
        }
    }

    /**
     * reads a Serializable object from a file which path is passed as a parameter
     *
     * @param filePath file to read
     * @return read object
     */
    public static Object readObjectFromFile(String filePath) {
        try (FileInputStream fin = new FileInputStream(filePath); ObjectInputStream ois = new ObjectInputStream(fin)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RetrieveSavedGameException("Cannot retrieve the game for file: " + filePath, e);
        }
    }

    /**
     * checks whether the file exists by the given filePath, if so returns true, otherwise false
     *
     * @param filePath to check if file exists
     * @return
     */
    public static boolean isFileExists(String filePath) {
        return new File(filePath).exists();
    }

}
