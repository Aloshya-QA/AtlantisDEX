package utils;

import lombok.extern.log4j.Log4j2;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

@Log4j2
public class Retry implements IRetryAnalyzer {

    private static final int MAX_RETRY = 3;

    @Override
    public boolean retry(ITestResult iTestResult) {
        Integer retryCount = (Integer) iTestResult.getAttribute("retryCount");

        if (retryCount == null) {
            retryCount = 0;
        }

        if (retryCount < MAX_RETRY) {
            retryCount++;
            iTestResult.setAttribute("retryCount", retryCount);
            log.warn("Retrying test '{}' - attempt {}/{}", iTestResult.getName(), retryCount, MAX_RETRY);
            return true;
        } else {
            log.error("Test '{}' failed after {} attempts", iTestResult.getName(), MAX_RETRY);
            return false;
        }
    }
}
