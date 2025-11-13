package com.example.core.driver;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WebDriverEventLogger implements WebDriverListener {
    @Override public void beforeClick(WebElement element) {  }

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        if (target instanceof WebDriver d) {
            try { ((TakesScreenshot)d).getScreenshotAs(OutputType.BYTES); } catch (Exception ignore) {}
        }
    }
}
