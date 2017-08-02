/**
 * Created by jackg on 8/1/2017.
 */
import javax.swing.*;

public class BoxPopperUpper implements Runnable {

    private String message = "null";
    private String title = "OPEN TABLE REFRESHER";

    BoxPopperUpper(String string){
        message = string;
    }

    BoxPopperUpper(String title, String message){
        this.title = title;
        this.message = message;
    }


    public void run(){
        JOptionPane pane = new JOptionPane();
        pane.setMessage(message);
        JDialog dialog = pane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.show();
    }

}
