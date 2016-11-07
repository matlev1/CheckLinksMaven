import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CheckLinks
{
    public static List findAllLinks(WebDriver driver)

    {

        List<WebElement> elementList = new ArrayList();

        elementList = driver.findElements(By.tagName("a"));

        elementList.addAll(driver.findElements(By.tagName("img")));

        List finalList = new ArrayList();
        ;

        for (WebElement element : elementList)

        {

            if (element.getAttribute("href") != null)

            {

                finalList.add(element);

            }

        }

        return finalList;

    }

    public static Integer isLinkBroken(URL url) throws Exception

    {

        //url = new URL("http://yahoo.com");
        int responseStatus;

        String response = "";

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try

        {

            connection.connect();

            responseStatus = connection.getResponseCode();
            //response = connection.getResponseMessage() + " " + responseStatus;


            connection.disconnect();

            return responseStatus;

        } catch (Exception exp)

        {

            return 0;

        }

    }

    public static void main(String[] args) throws Exception
    {

       /* FirefoxProfile profile = new FirefoxProfile();
        WebDriver driver = new FirefoxDriver(new FirefoxBinary(new File("/Applications/FireFox.app/Contents/MacOS/firefox-bin")), profile);*/
        //WebDriver driver = new SafariDriver();
        //driver = new FirefoxDriver();
        System.setProperty("webdriver.chrome.driver", "/Users/levon/ChromeDriver/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("https://timeandexpense.teksystems.com/webtime/");

        driver.get("https://www.apple.com/mac/");

        //ff.get("http://www.yahoo.com/");

        List<WebElement> allImages = findAllLinks(driver);

        System.out.println("Total number of elements found " + allImages.size());

        for (WebElement element : allImages)
        {

            try

            {
                //String responseString = isLinkBroken(new URL(element.getAttribute("href"));
                JavascriptExecutor js = (JavascriptExecutor) driver;
                if (isLinkBroken(new URL(element.getAttribute("href"))) == 200)
                {
                    js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "background-color: lightgreen; border: 1px dotted solid lightgreen;");
                }
                else if (isLinkBroken(new URL(element.getAttribute("href"))) == 404)
                {
                    js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "background-color: red; border: 1px dotted solid red;");
                }

                else
                {
                    String desiredText = Integer.toString(isLinkBroken(new URL(element.getAttribute("href"))));
                    js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "background-color: lightblue; border: 1px dotted solid lightblue;");
                    //element.findElement(By)
                    //js.executeScript("document.getElementsByTagName('')).innerHTML"+ desiredText);
                    //js.executeScript("document.evaluate(html/body/footer/div/nav[2]/div[2]/div/ul/li, document, null, 9, null).singleNodeValue.innerHTML"+ desiredText);
                    //("arguments[0].innerHTML = '<h1>Set text using innerHTML</h1>'", element)
                    js.executeScript("arguments[0].innerHTML='" + desiredText + "'", element);
                }

                System.out.println("URL: " + element.getAttribute("href") + " returned " + isLinkBroken(new URL(element.getAttribute("href"))));


                //System.out.println("URL: " + element.getAttribute("outerhtml")+ " returned " + isLinkBroken(new URL(element.getAttribute("href"))));

            } catch (Exception exp)

            {

                System.out.println("At " + element.getAttribute("innerHTML") + " " + element.getAttribute("href") + " " + " Exception occured -&gt; " + exp.getMessage());

            }


        }
        //driver.close();
        //driver.quit();

    }
}