package imagisoft.edepa;

/**
 * Created by Julian on 18/03/17.
 */

class Controller {
    private static final Controller ourInstance = new Controller();

    static Controller getInstance() {
        return ourInstance;
    }

    private Controller() {
    }
}
