import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.*;


public class ElsevierTest {

    public static void main(String[] args) throws InterruptedException {

        String driver_executable_path = "src/test/resources/geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", driver_executable_path);

        // Launch Browser - Creating a Firefox instance
        WebDriver driver = new FirefoxDriver();

        // Open URL
        String url = "http://automationpractice.com/index.php";

        // Open URL and Maximize browser window
        driver.get(url);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);

        //Click on Main "Dresses" option
        driver.findElement(By.linkText("DRESSES")).click();

        //Click on Sub Menu "Summer-Dresses" option
        WebElement summerDresses=driver.findElement(By.cssSelector("#categories_block_left>div.block_content>ul>li.last>a"));
        summerDresses.click();

        //Get Title  as 'Summer Dresses' & Compare
        String ConfirmationText=driver.findElement(By.cssSelector(".cat-name")).getText();
        assertEquals("SUMMER DRESSES",ConfirmationText.trim());

        //Get the Count of summer dresses  displayed
        int productCount = driver.findElements(By.cssSelector("ul.product_list>li")).size();
        //System.out.println("The Product Count is:- " +productCount);

        //Add the 1st & 3rd product from the summer dresses product to the cart

         for(int i=0; i<=productCount-1; i++){
             if((i==0) || (i==2)){
                 int j= i+1;
                 WebElement printedSummerDress=driver.findElement(By.cssSelector(".product_list.grid.row>li:nth-child("+j+")>div>div.right-block>h5>a"));
                 printedSummerDress.click();
                 WebElement addToCart=driver.findElement(By.cssSelector("p#add_to_cart>.exclusive"));
                 addToCart.click();

                 WebDriverWait wait = new WebDriverWait(driver,50);
                 wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span[title='Continue shopping']")));

                 WebElement continueShopping=driver.findElement(By.cssSelector("span[title='Continue shopping']"));
                 continueShopping.click();
                 WebElement checkCartCount=driver.findElement(By.cssSelector("span.ajax_cart_quantity:nth-child(2)"));
                 String cartCountStr = checkCartCount.getText();

                 // Check the products count in the cart
                 int cartCountVal=Integer.parseInt(cartCountStr);
                 if(i==0){
                     assertEquals( 1, cartCountVal);
                 }
                 if(i==3){
                     assertEquals( 2, cartCountVal);
                 }
                // Thread.sleep(700);
                 //WebDriverWait wait = new WebDriverWait(driver,50);
                 wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".breadcrumb>a[title='Summer Dresses']")));

                 WebElement breadcrumbSummerDresses=driver.findElement(By.cssSelector(".breadcrumb>a[title='Summer Dresses']"));
                 breadcrumbSummerDresses.click();

             }

         }

        WebElement continueToCart=driver.findElement(By.cssSelector("span.ajax_cart_quantity:nth-child(2)"));
        continueToCart.click();

        //Proceed to Checkout
        WebElement proceedToCheckout=driver.findElement(By.cssSelector("p.cart_navigation>a[title='Proceed to checkout']>span"));
        proceedToCheckout.click();

        //generate radom string to pass it as email id
        String generatedString = RandomStringUtils.randomAlphabetic(5);
        String generateString = generatedString+"@gmail.com";

        driver.findElement(By.cssSelector("#email_create")).sendKeys(generateString);

        driver.findElement(By.cssSelector("#SubmitCreate>span")).click();
        WebElement personalInfo=driver.findElement(By.cssSelector("div.account_creation:nth-child(1)>h3"));
        String personalInformation = personalInfo.getText();

        //Comparing the personal information page name reached
        assertEquals("YOUR PERSONAL INFORMATION",personalInformation);

        driver.close();

    }

}

