package tests;

import org.testng.annotations.Test;

public class SwapTest extends BaseTest {

    @Test
    public void swapTokens() throws InterruptedException {
        swapPage.openPage()
                .isOpened()
                .closeAnotherTabs();
        walletPage.openLoginPage()
                .isLoginPageOpened()
                .login(email, password)
                .isCreatePinCodePageOpened()
                .createPinCode(newPassword)
                .isImportPageOpened()
                .importWallet(seed)
                .isImportWalletSuccessful();
        swapPage.openPage()
                .isOpened()
                .connectWallet()
                .walletConnectSuccessful()
                .swapTokens(mon, wmon, "0.1", 21);
    }

}
