/**
 * Created by jackg on 8/1/2017.
 */
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.List;
import java.util.concurrent.TimeUnit;


class Connection {

    private WebDriver driver;
    private final String url = Param.URL;
    private final List<String> allowedTimes = Param.ALLOWED_TIMES;

    Connection(){

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        driver = new ChromeDriver(capabilities);
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
    }

    boolean goToPage(){
        try{
            driver.get(url);
            return waitForPageLoad("reservation");
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    boolean refreshPage(){
        try{
            driver.navigate().refresh();
            return waitForPageLoad("reservation");
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    boolean reservationsAvailable(){
        try{
            WebElement element = driver.findElement(By.id("dtp-results"));
            String text  = element.getText();
            if(text.contains("No tables are available")){
                return false;
            }else{
                return checkAgainstTimeList(text);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private boolean waitForPageLoad(String stringToWatchFor){
        boolean pageLoaded = false;
        int count = 0;
        while(!pageLoaded && count<20){
            try{
                driver.findElement(By.id(stringToWatchFor));
                pageLoaded = true;
            }catch(Exception e){
                waitForMillis(50);
                count = count+1;
            }
        }
        waitForMillis(50);
        return pageLoaded;
    }

    private boolean checkAgainstTimeList(String text){
        for(String time : allowedTimes){
            if(text.contains(time)){
                return true;
            }
        }
        return false;
    }

    void waitForMillis(int t){
        try{
            Thread.sleep(t);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void reset(){
        driver.close();
        driver.quit();
    }



}
