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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class CheckLinks
{
    public static List findAllLinks(WebDriver driver)

    {

        List<WebElement> elementList = new ArrayList();

            elementList = driver.findElements(By.tagName("a"));

        elementList.addAll(driver.findElements(By.tagName("img")));

        List finalList = new ArrayList();


        for (WebElement element : elementList)

        {

            if (element.getAttribute("href") != null)

            {

                finalList.add(element);

            }

        }

        return finalList;

    }
    static String getLocation;

    public static Integer getResponse(String url)

    {

        //url = new URL("http://yahoo.com");
        int responseStatus = 0;


        String response = "";
        HttpURLConnection con = null;
        try {


            con = (HttpURLConnection) new URL(url).openConnection();
            con.setInstanceFollowRedirects(true);
            con.setRequestMethod("GET");
            con.connect();
            responseStatus = con.getResponseCode();
            getLocation = con.getHeaderField("Location");

            //System.out.println(con.getResponseMessage() + " " + responseStatus);
        } catch (IOException e) {
            e.printStackTrace();
        }
    finally {
    con.disconnect();
}

        //HttpURLConnection connection = (HttpURLConnection) url.openConnection();




           ;

            //responseStatus = connection.getResponseCode();
            //response =
        //









        return responseStatus;


    }

    public static void main(String[] args) throws Exception
    {

       /* FirefoxProfile profile = new FirefoxProfile();
        WebDriver driver = new FirefoxDriver(new FirefoxBinary(new File("/Applications/FireFox.app/Contents/MacOS/firefox-bin")), profile);*/
        //WebDriver driver = new SafariDriver();
        //driver = new FirefoxDriver();
        System.setProperty("webdriver.chrome.driver", "/Users/levon/ChromeDriver/chromedriver");
        WebDriver driver = new ChromeDriver();
        //driver.get("https://timeandexpense.teksystems.com/webtime/");

        driver.get("https://www.apple.com/today/");

        //ff.get("http://www.yahoo.com/");

        List<WebElement> allLinks = findAllLinks(driver);

        System.out.println("Total number of elements found " + allLinks.size());


        for (WebElement element : allLinks)
        {

            try

            {
                int responseStatus;
                //String responseString = isLinkBroken(new URL(element.getAttribute("href"));
                JavascriptExecutor js = (JavascriptExecutor) driver;
                if (getResponse(element.getAttribute("href")) == 200)
                {
                    js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "background-color: lightgreen; border: 1px dotted solid lightgreen;");
                }
                else if (getResponse(element.getAttribute("href")) == 404)
                {
                    js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "background-color: red; border: 1px dotted solid red;");
                }
                else if ((getResponse(element.getAttribute("href")) == 301) )
                {
                    String desiredText = Integer.toString(getResponse(element.getAttribute("href")));
                    js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "background-color: lightgrey; border: 1px dotted solid lightblue;");
                    js.executeScript("arguments[0].innerHTML='" + desiredText + "'", element);
                    System.out.println("This is where 301s are being redirected" + " " + getLocation);


                    HttpURLConnection con = (HttpURLConnection) new URL(getLocation).openConnection();
                    con.setInstanceFollowRedirects(false);
                    con.setRequestMethod("GET");
                    con.connect();
                    responseStatus = con.getResponseCode();
                    String newLocation = con.getHeaderField("Location");
                    con.disconnect();

                    System.out.println("Second Level Redirect Location and response status " + responseStatus + " " + newLocation );

                    if (responseStatus != 200 ) {
                       HttpURLConnection conec = (HttpURLConnection) new URL(newLocation).openConnection();
                       conec.setInstanceFollowRedirects(false);
                       // con.setRequestMethod("GET");
                       // con.connect();
                        //responseStatus = conec.getResponseCode();
                        String newNewlocation = conec.getHeaderField("Location");
                        //con.disconnect();
                        if (!newNewlocation.contains("http://www.apple.com"))
                        {
                            System.out.println("Third Level Redirect Location and response status " + responseStatus + " http://www.apple.com" + newNewlocation );
                        }
                        else
                        System.out.println("Third Level Redirect Location and response status " + responseStatus + " " + newNewlocation );
                    }


                }

                else
                {
                    String desiredText = Integer.toString(getResponse(element.getAttribute("href")));
                    js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "background-color: lightblue; border: 1px dotted solid lightblue;");
                    //element.findElement(By)
                    //js.executeScript("document.getElementsByTagName('')).innerHTML"+ desiredText);
                    //js.executeScript("document.evaluate(html/body/footer/div/nav[2]/div[2]/div/ul/li, document, null, 9, null).singleNodeValue.innerHTML"+ desiredText);
                    //("arguments[0].innerHTML = '<h1>Set text using innerHTML</h1>'", element)
                    js.executeScript("arguments[0].innerHTML='" + desiredText + "'", element);
                    System.out.println("URL: " + element.getAttribute("href") + " returned " + getResponse(element.getAttribute("href")));
                }




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