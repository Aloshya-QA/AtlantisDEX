package pages;

import com.codeborne.selenide.WebDriverRunner;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.testng.Assert;

import java.awt.*;
import java.awt.event.KeyEvent;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.String.format;

@Log4j2
public class SwapPage extends BasePage {

    private static final String
            BALANCE_SELL_TOKEN = "//div[text()='Sell:']/parent::div/parent::div//span[2]",
            SELL_TOKEN = "//div[text()='Sell:']/parent::div/parent::div//button[not(text()='Max')]",
            BUY_TOKEN = "//div[text()='Buy:']/parent::div/parent::div//button",
            MODAL_PLACEHOLDER = "//input[@placeholder='Search name or paste address']",
            SELECT_TOKEN = "//div[@class='text-sm']",
            SELL_INPUT = "//div[text()='Sell:']/parent::div//input",
            SWAP_BUTTON = "//button[text()='Cross-Chain Swap']/following::div/button[text()='Swap']";

    public SwapPage() throws AWTException {
        super();
    }

    public SwapPage openPage() {
        log.info("Opening SwapPage");
        open("/swap/v4/");
        return this;
    }

    public SwapPage isOpened() {
        try {
            $(byText("Cross-Chain Swap")).shouldBe(visible);
            log.info("SwapPage is opened");
        } catch (TimeoutException e) {
            log.error(e.getMessage());
            Assert.fail("SwapPage isn't opened");
        }
        return this;
    }


    public SwapPage closeAnotherTabs() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_W);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_W);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.waitForIdle();

        return this;
    }

    public SwapPage connectWallet() throws InterruptedException {
        $(byText("Connect")).click();
        $(byText("HaHa Wallet")).shouldBe(visible).click();
        Thread.sleep(2000);
//        open("chrome-extension://adaahjnbncfhalpbfifmklimghhecgep/popup.html");
        open("chrome-extension://baickakiacddlihiafkokkdklhnplgaj/popup.html");
        $(byText("Accept")).shouldBe(visible).click();
        return this;
    }

    public SwapPage walletConnectSuccessful() {
        $(byText("Accept")).shouldNotBe(visible);
        return this;
    }

    public double getTokenBalance(String token) {
        log.info("Getting the token balance");
        $x(SELL_TOKEN).click();
        $x(MODAL_PLACEHOLDER).should(visible);
        $x(MODAL_PLACEHOLDER).setValue(token);
        $x(SELECT_TOKEN).click();

        return Double.parseDouble($x(BALANCE_SELL_TOKEN).getText());
    }

    public SwapPage swapTokens(String sellToken, String buyToken, String amount, int totalTransactions) throws InterruptedException {
        log.info("Start swap tokens: ");
        log.info("Balance MON: {}", getTokenBalance(sellToken));
        if (getTokenBalance(sellToken) < 3) {
            log.info("Switch to WMON token");
            if (getTokenBalance(buyToken) > 3) {
                log.info("Swapping WMON tokens...");
                $x(SELL_TOKEN).click();
                $x(MODAL_PLACEHOLDER).should(visible);
                $x(MODAL_PLACEHOLDER).setValue(buyToken);
                $x(SELECT_TOKEN).click();

                $x(BUY_TOKEN).click();
                $x(MODAL_PLACEHOLDER).should(visible);
                $x(MODAL_PLACEHOLDER).setValue(sellToken);
                $x(SELECT_TOKEN).click();

                while (totalTransactions > 0) {
                    log.info("Transaction #{}", totalTransactions);
                    $x(SELL_INPUT).setValue(amount);
                    $x(SWAP_BUTTON).should(visible);
                    $x(SWAP_BUTTON).click();

                    Thread.sleep(6000);

                    robot.keyPress(KeyEvent.VK_TAB);
                    robot.delay(50);
                    robot.keyRelease(KeyEvent.VK_TAB);
                    robot.delay(50);

                    robot.keyRelease(KeyEvent.VK_TAB);
                    robot.delay(50);
                    robot.keyRelease(KeyEvent.VK_TAB);
                    robot.delay(50);

                    robot.keyRelease(KeyEvent.VK_TAB);
                    robot.delay(50);
                    robot.keyRelease(KeyEvent.VK_TAB);

                    robot.delay(100);
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.delay(50);
                    robot.keyRelease(KeyEvent.VK_ENTER);

                    robot.waitForIdle();

                    $x(SELL_INPUT).should(attribute("value", ""));

                    totalTransactions--;
                }


            } else {
                log.warn("Not enough tokens");
                Assert.fail("Not enough tokens");
            }
        } else {
            log.info("Swapping MON tokens...");
            open(format("/swap/v4/?inputCurrency=%s&outputCurrency=%s", sellToken, buyToken));
            $(byText("Cross-Chain Swap")).shouldBe(visible);

            while (totalTransactions > 0) {
                log.info("Transaction #{}", totalTransactions);
                $x(SELL_INPUT).sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
                $x(SELL_INPUT).should(attribute("value", ""));
                $x(SELL_INPUT).setValue(amount);
                $x(SWAP_BUTTON).should(enabled);
                $x(SWAP_BUTTON).click();

                Thread.sleep(7000);

                WebDriver driver = WebDriverRunner.getWebDriver();
                WebDriver newTab = driver.switchTo().newWindow(WindowType.TAB);
                newTab.get("chrome-extension://baickakiacddlihiafkokkdklhnplgaj/popup.html");
//                newTab.get("chrome-extension://adaahjnbncfhalpbfifmklimghhecgep/popup.html");
                $(byText("Confirm")).shouldBe(visible).click();
                $(byText("Confirm")).shouldNotBe(visible);

                newTab.close();

                var tabs = driver.getWindowHandles().stream().toList();
                driver.switchTo().window(tabs.get(0));
                $(byText("Cross-Chain Swap")).shouldBe(visible);
                Thread.sleep(5000);


                totalTransactions--;
            }
        }

        return this;
    }
}
