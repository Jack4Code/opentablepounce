/**
 * Created by jackg on 8/1/2017.
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    //obtain all of the jar files...should be apparent by the imports
    //download chromedriver.exe
    //enter credentials in the param class
    //go to the opentable website and search for the restaurant and date.  copy and paste the url into the param class

    public static void main(String[] args) {
        final String PATH_TO_CHROMEDRIVER = System.getProperty("PATH_TO_CHROMEDRIVER","C:\\Users\\jackg\\libraries\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", PATH_TO_CHROMEDRIVER);

        final ExecutorService executorService = Executors.newFixedThreadPool(6);

        ConnectionManager connectionManager = new ConnectionManager(executorService);
        connectionManager.runLoop();
    }

}