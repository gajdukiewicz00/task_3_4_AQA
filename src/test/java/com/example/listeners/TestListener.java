package com.example.listeners;

import com.example.core.driver.DriverFactory;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestListener extends TestListenerAdapter {
    @Override public void onTestFailure(ITestResult tr) { attachPng(); }

    @Attachment(value="Failure screenshot", type="image/png")
    private byte[] attachPng() {
        try { return ((TakesScreenshot) DriverFactory.get()).getScreenshotAs(OutputType.BYTES); }
        catch (Exception e) { return new byte[0]; }
    }
}