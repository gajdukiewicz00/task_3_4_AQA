package com.example.pages.components;

import com.example.core.driver.DriverFactory;
import com.example.core.wait.Waiter;
import org.openqa.selenium.*;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import java.util.List;

public class Header {
    private final SearchContext root;
    private final WebDriver driver;

    public Header(SearchContext root) {
        this.root = root;
        this.driver = DriverFactory.get();
        PageFactory.initElements(new DefaultElementLocatorFactory(root), this);
    }


    @FindBy(css = "button[aria-label*='menu' i], button[id*='menu' i], button[class*='menu' i]")
    private List<WebElement> menuButtons;

    private List<WebElement> findPricingLinks() {
        return root.findElements(By.cssSelector("a[aria-label='Plans & Pricing'], a[href*='pricing']"));
    }

    public void openPlansAndPricing() {
        try {
            if (!menuButtons.isEmpty() && menuButtons.get(0).isDisplayed()) {
                menuButtons.get(0).click();
            }
        } catch (Exception ignored) {}


        var links = findPricingLinks().stream().filter(WebElement::isDisplayed).toList();
        if (links.isEmpty()) links = findPricingLinks();
        if (links.isEmpty()) throw new NoSuchElementException("Pricing link not found in header");

        WebElement link = links.get(0);

        try {
            Waiter.scrollIntoView(link);
            Waiter.clickable(link);
            link.click();
        } catch (ElementNotInteractableException | MoveTargetOutOfBoundsException ex) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        }
    }
}
