package pages;

import com.codeborne.selenide.ElementsCollection;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;

import java.awt.*;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

@Log4j2
public class WalletPage extends BasePage{

    private static final String GET_STARTED = "//button[text()='GET STARTED']";

    public WalletPage() throws AWTException {
    }

    public WalletPage openLoginPage() {
//        open("chrome-extension://adaahjnbncfhalpbfifmklimghhecgep/home.html#onboarding/login");
        open("chrome-extension://baickakiacddlihiafkokkdklhnplgaj/home.html#onboarding/login");
        return this;
    }

    public WalletPage isLoginPageOpened() {
        try {
            $("[type='email']").shouldBe(visible);
            log.info("WalletPage is opened");
        } catch (TimeoutException e) {
            log.error(e.getMessage());
            Assert.fail("WalletPage isn't opened");
        }
        return this;
    }

    public WalletPage login(String email, String password) {
        $("[type='email']").setValue(email);
        $("[type='password']").setValue(password);
        $(byText("CONTINUE")).click();
        return this;
    }

    public WalletPage isCreatePinCodePageOpened() {
        $(byText("Create Pin Code")).shouldBe(visible);
        return this;
    }

    public WalletPage createPinCode(String pin) {
        $("[placeholder='Your Pin Code']").setValue(pin);
        $("[placeholder='Confirm Pin Code']").setValue(pin);
        $(byText("Continue")).click();
        return this;
    }

    public WalletPage isImportPageOpened() {
        $("[type='checkbox']").shouldBe(visible);
        return this;
    }

    public WalletPage importWallet(String seedPhrase) {
        String[] seedWords = seedPhrase.split(" ");
        $("[type='checkbox']").click();
        $(byText("Import Existing Wallet")).click();
        $(byText("Recover Wallet")).shouldBe(visible);
        ElementsCollection inputs = $$x("//div[(starts-with(text(), 'Import an existing'))]" +
                "/following::div//div//input");

        for (int i = 0; i < inputs.size(); i++) {
            inputs.get(i).setValue(seedWords[i]);
        }
        $(byText("Continue")).click();
        return this;
    }

    public WalletPage isImportWalletSuccessful() {
        $(byText("Start Using Wallet")).shouldBe(visible).click();
        return this;
    }



    public WalletPage getText() {
        System.out.println($("h2").getText());
        return this;
    }
}
