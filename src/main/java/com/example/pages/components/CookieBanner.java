package com.example.pages.components;

import com.example.core.driver.DriverFactory;
import org.openqa.selenium.*;
import java.util.List;

public final class CookieBanner {
    private CookieBanner() {}

    public static void acceptIfPresent() {
        WebDriver d = DriverFactory.get();
        try {
            List<WebElement> possible = d.findElements(By.xpath(
                    "//*[contains(translate(@id,'COOKIE','cookie'),'cookie') or " +
                            "contains(translate(@class,'COOKIE','cookie'),'cookie') or " +
                            "contains(translate(.,'COOKIE','cookie'),'cookie')]"));

            if (possible.isEmpty()) return;

            WebElement root = possible.get(0);
            List<WebElement> btns = root.findElements(By.xpath(
                    ".//button[contains(translate(.,'ACEPTGIRYZ','aceptgiryz'),'accept') or " +
                            "contains(.,'Agree') or contains(.,'Akcept') or contains(.,'Zgadzam')]"));

            if (!btns.isEmpty() && btns.get(0).isDisplayed()) {
                btns.get(0).click();
            }
        } catch (Exception ignored) { }
    }
}
