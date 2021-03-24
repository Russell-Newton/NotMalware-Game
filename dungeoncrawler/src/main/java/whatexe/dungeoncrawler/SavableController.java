package whatexe.dungeoncrawler;

import javafx.concurrent.Task;

import java.io.*;

/**
 * A SavableController is a {@link ManagedController} that supports saving to and loading from a
 * file.
 *
 * @see SavableController#saveData
 */
public abstract class SavableController extends ManagedController {

    /**
     * Should be implemented with a static inner class that implements Serializable that contains
     * the information needed to serialize (save) and deserialize (load) the scene.
     */
    protected Serializable saveData;

    /**
     * Saves the state of this ManagedController to the specified location.
     *
     * @param location String path to save the state to.
     */
    public final void saveState(String location) {
        if (saveData == null) {
            throw new RuntimeException("You must implement saveData with a static inner subclass "
                                               + "of SceneSaveData!");
        }
        Task<Void> saveTask = new Task<>() {
            @Override
            protected Void call() throws IOException {
                try (ObjectOutputStream outputStream =
                             new ObjectOutputStream(
                                     new BufferedOutputStream(new FileOutputStream(location)))) {
                    setSaveDataFromState();
                    outputStream.writeObject(saveData);
                } catch (IOException e) {
                    System.out.println("Failed to save state!");
                    throw e;
                }
                return null;
            }
        };

        Thread saveThread = new Thread(saveTask);
        saveThread.setDaemon(true);
        saveThread.start();
    }

    /**
     * Sets the state of this ManagedController from the {@link SavableController#saveData}.
     * Called after deserialization in {@link SavableController#loadState(String)}.
     */
    protected abstract void setSaveDataFromState();

    /**
     * Loads the state of this ManagedController from the specified location.
     *
     * @param location String path to save the state to.
     */
    public final void loadState(String location) {
        if (saveData == null) {
            throw new RuntimeException("You must implement saveData with a static inner subclass "
                                               + "of SceneSaveData!");
        }
        Task<Void> loadTask = new Task<>() {
            @Override
            protected Void call() throws IOException, ClassNotFoundException {
                try (ObjectInputStream inputStream =
                             new ObjectInputStream(new FileInputStream(location))) {
                    saveData = saveData.getClass().cast(inputStream.readObject());
                    setStateFromSaveData();
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Failed to load state! " + e.getLocalizedMessage());
                    throw e;
                }
                return null;
            }
        };

        Thread loadThread = new Thread(loadTask);
        loadThread.setDaemon(true);
        loadThread.start();
    }

    /**
     * Sets the {@link SavableController#saveData} of this ManagedController from the current
     * state. Called before serialization in {@link SavableController#saveState(String)}.
     */
    protected abstract void setStateFromSaveData();

}
