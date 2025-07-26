package pages;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;

import java.awt.*;
import java.awt.event.KeyEvent;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

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

        Thread.sleep(30000);

        robot.delay(3000);

        robot.keyPress(KeyEvent.VK_ALT);
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_F4);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_F4);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_ALT);

        robot.keyPress(KeyEvent.VK_TAB);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.delay(50);

        robot.waitForIdle();

        return this;
    }

    public SwapPage walletConnectSuccessful() {
        $(byText("Select an amount for swap")).shouldBe(visible);
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
            $x(SELL_TOKEN).click();
            $x(MODAL_PLACEHOLDER).should(visible);
            $x(MODAL_PLACEHOLDER).setValue(sellToken);
            $x(SELECT_TOKEN).click();

            $x(BUY_TOKEN).click();
            $x(MODAL_PLACEHOLDER).should(visible);
            $x(MODAL_PLACEHOLDER).setValue(buyToken);
            $x(SELECT_TOKEN).click();

            while (totalTransactions > 0) {
                log.info("Transaction #{}", totalTransactions);
                $x(SELL_INPUT).setValue(amount);
                $x(SWAP_BUTTON).should(enabled);
                $x(SWAP_BUTTON).click();


                Thread.sleep(7000);

                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.delay(50);
                robot.keyPress(KeyEvent.VK_TAB);
                robot.delay(50);

                robot.keyRelease(KeyEvent.VK_TAB);
                robot.delay(50);
                robot.keyPress(KeyEvent.VK_TAB);
                robot.delay(50);

                robot.keyRelease(KeyEvent.VK_TAB);
                robot.delay(50);
                robot.keyRelease(KeyEvent.VK_SHIFT);
                robot.delay(50);
                robot.keyPress(KeyEvent.VK_SPACE);

                robot.delay(50);
                robot.keyRelease(KeyEvent.VK_SPACE);

                robot.waitForIdle();

                $x(SELL_INPUT).should(attribute("value", ""));

                totalTransactions--;
            }
        }

        return this;
    }
}
