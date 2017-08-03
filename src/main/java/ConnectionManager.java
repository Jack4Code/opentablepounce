/**
 * Created by jackg on 8/1/2017.
 */
import org.apache.commons.exec.ExecuteResultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

class ConnectionManager {

    private Connection connection = new Connection();
    private final ExecutorService executorService;
    private final TimeChecker timeChecker;
    private List<String> EMAIL_ADDRESSES = new ArrayList<String>();

    ConnectionManager(ExecutorService executorService){
        this.executorService = executorService;
        timeChecker = new TimeChecker();
        EMAIL_ADDRESSES.add("");
    }

    void runLoop(){

        if(!attemptInitialSearch()){
            System.out.println("Initial search failed.");
            new BoxPopperUpper("OpenTableRefresher: Initial Search Failed.");
            System.exit(0);
        }

        while(!timeChecker.shouldStop()){

            if(!attemptRefresh()){
                System.out.println("Refresh sequence failed after 10 tries.");
                connection.waitForMillis(10*1000);
                resetConnection();
                attemptInitialSearch();
            }

            try{

                if(connection.reservationsAvailable()){
                    String subject = "OpenTable: RESERVATIONS AVAILABLE";
                    String message = Param.URL;
                    executorService.execute(new BoxPopperUpper("RESERVATIONS AVAILABLE"));
                    executorService.execute(new Emailer(subject,EMAIL_ADDRESSES,message));
                    connection.waitForMillis(1000*3600*24);
                }else{
                    connection.waitForMillis(500);
                }

            }catch(Exception e){
                resetConnection();
                attemptInitialSearch();
            }

        }

    }

    private boolean attemptRefresh(){
        int count = 0;
        while(count<10) {
            if(connection.refreshPage()){
                return true;
            }else{
                resetConnection();
                attemptInitialSearch();
                count = count+1;
                connection.waitForMillis(50);
            }
        }
        return false;
    }

    private boolean attemptInitialSearch(){
        int count = 0;
        while(count<10) {
            if(connection.goToPage()){
                return true;
            }else{
                resetConnection();
                count = count+1;
                connection.waitForMillis(50);
            }
        }
        return false;
    }

    private void resetConnection(){
        connection.reset();
        connection = new Connection();
    }

}
