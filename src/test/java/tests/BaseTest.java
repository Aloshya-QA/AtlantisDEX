package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import pages.SwapPage;
import pages.WalletPage;
import utils.PropertyReader;
import utils.WindowFocusUtil;

import java.awt.*;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static utils.PropertyReader.getProperty;

@Log4j2
public class BaseTest {

    SwapPage swapPage;
    WalletPage walletPage;

    static String email,
            mon,
            wmon;

    String seed = System.getProperty("SEED_PHRASE", PropertyReader.getProperty("SEED_PHRASE"));
    String password = System.getProperty("PASSWORD", PropertyReader.getProperty("PASSWORD"));
    String newPassword = System.getProperty("NEW_PASSWORD", PropertyReader.getProperty("NEW_PASSWORD"));

    private static final String
            EXTENSION_PATH = Paths.get("src/test/resources/extensions/HaHaWallet").toAbsolutePath().toString();

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setup(@Optional("chrome") String browser) throws AWTException {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false));

        email = getProperty("email");
        mon = getProperty("mon");
        wmon = getProperty("wmon");

        if (browser.equalsIgnoreCase("chrome")) {
            Configuration.browser = "chrome";
            Configuration.baseUrl = "https://app.atlantisdex.xyz";
            Configuration.timeout = 15000;
            Configuration.browserSize = null;
            Configuration.headless = false;
            Configuration.browserCapabilities = getChromeOptions();

            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("linux")) {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                WindowFocusUtil.focusWindowByName("Chrome");
            }
        }

        swapPage = new SwapPage();
        walletPage = new WalletPage();
    }

    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions-except=" + EXTENSION_PATH);
        options.addArguments("--load-extension=" + EXTENSION_PATH);
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("credentials_enable_service", false);
        chromePrefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--lang=en");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-features=TabGroups");
        options.addArguments("--new-window");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches",
                Collections.singletonList("enable-automation"));
        options.addArguments("--start-maximized");
        return options;
    }

    @AfterMethod
    public void TearDawn() {
        getWebDriver().quit();
    }
}
