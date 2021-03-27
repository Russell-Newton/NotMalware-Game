package whatexe.dungeoncrawler;

import java.util.Map;

/**
 * A ManagedController is a special FXML controller that provides methods to the
 * {@link SceneManager} for setting its states. Only the {@link ManagedController#init()} method
 * needs to be defined. By default, most of the other method implementations are empty. Some methods
 * introduce a {@link Map} of parameters that can be used to set the state of the extending
 * controller. If these parameters are not used at all, they can be ignored. They are useful for
 * passing non-static information between controllers, if needed. This potentially avoids the
 * need for {@link javafx.util.BuilderFactory}s and controller factories when loading FXML. It
 * also avoids the need to use static data that may have to be synchronized on.
 * <br><br>
 * <strong>There needs to be a default, no-args constructor for a ManagedController!!!</strong>
 * <br><br>
 * If additional functionality is required for the ManagedController, certain methods should be
 * implemented: <br>
 * {@link ManagedController#setState(Map)}, <br>
 * {@link ManagedController#resetState()}, <br>
 * If these methods are not overridden, an {@link UnsupportedOperationException} will be thrown
 * when the methods are called.
 */
public abstract class ManagedController {

    /**
     * Initializes the state of this ManagedController, with optional parameters to set the
     * initial state. This is unique from the constructor in that it can take in parameters. By
     * default, calls {@link ManagedController#init()}.
     *
     * @param loadParameters Parameters that can initialize the state of the scene. If the
     *                       extending controller does not need to use any passed loading
     *                       parameters, this parameter can be ignored.
     */
    public void init(Map<String, Object> loadParameters) {
        init();
    }

    /**
     * Initializes the state of this ManagedController.
     */
    public abstract void init();

    /**
     * Can be called after {@link ManagedController#init()}, if such functionality is needed.
     * Runs in {@link MainApp#switchScene(String)} after the scene is completely changed.
     */
    public void postInit() {
    }

    /**
     * Sets the state of this ManagedController, with parameters for setting the state.
     *
     * @param stateParameters Parameters that can set the state of this ManagedController. If the
     *                        extending controller does not need to use any passed state
     *                        parameters, this parameter can be ignored.
     */
    public void setState(Map<String, Object> stateParameters) {
        throw new UnsupportedOperationException("This ManagedController doesn't support setting "
                                                        + "the state!");
    }

    /**
     * Resets the state of this ManagedController.
     */
    public void resetState() {
        throw new UnsupportedOperationException("This ManagedController doesn't support resetting "
                                                        + "the state!");
    }

    /**
     * Called by the {@link SceneManager} before a scene is unloaded to execute any necessary
     * shutdown operations.
     */
    public void deinit() {
    }
}
