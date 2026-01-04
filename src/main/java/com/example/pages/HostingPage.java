package com.example.pages;

import com.example.core.wait.Waiter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class HostingPage extends BasePage {

    protected static final String[] PLAN_SELECTORS = {
            "[data-testid*='plan']",
            "[class*='pricing-card']",
            "[class*='plan-card']",
            "[class*='price-card']",
            ".plan",
            ".pricing-card",
            "[id*='plan']",
            "div[class*='plan']:not([class*='plans'])"
    };

    protected static final String[] PRICE_SELECTORS = {
            "[data-price]",
            ".price",
            "[class*='price']:not([class*='prices'])",
            "span[class*='price']",
            "div[class*='price']",
            "[class*='cost']"
    };

    private static final int MIN_PLAN_HEIGHT = 50;
    private static final int MIN_DIV_PLAN_HEIGHT = 100;
    private static final BigDecimal MAX_REASONABLE_PRICE = new BigDecimal("1000");
    private static final String TERM_1_YEAR = "1-year";

    /**
     * Service method: Select 1-year term and return prices
     * 
     * @return List of prices as BigDecimal
     */
    public List<BigDecimal> getPrices1Year() {
        List<WebElement> termButtons = driver.findElements(By.cssSelector(
                "button[id*='term-" + TERM_1_YEAR + "'], button[data-term='" + TERM_1_YEAR
                        + "'], button[aria-label*='1 year' i], " +
                        "[data-term='" + TERM_1_YEAR + "'], [id*='term-" + TERM_1_YEAR + "']"));
        if (!termButtons.isEmpty()) {
            WebElement termButton = termButtons.stream()
                    .filter(WebElement::isDisplayed)
                    .findFirst()
                    .orElse(termButtons.get(0));
            if (termButton != null) {
                Waiter.clickable(termButton);
                termButton.click();
                Waiter.domReady();
            }
        }

        return extractPricesFromPlans();
    }

    /**
     * Service method: Get count of available plans
     * 
     * @return Number of plans (based on found prices if elements not found)
     */
    public int getPlanCount() {
        List<WebElement> plans = findPlans();
        if (plans != null && !plans.isEmpty()) {
            return plans.size();
        }

        List<BigDecimal> prices = getPrices1Year();
        return prices.size();
    }

    /**
     * Find plan elements using multiple selectors
     * 
     * @return List of plan elements
     */
    protected List<WebElement> findPlans() {
        Waiter.domReady();

        // Replaced Thread.sleep with explicit wait for presence of any plan selector
        try {
            Waiter.waitDefault()
                    .until(d -> {
                        for (String selector : PLAN_SELECTORS) {
                            if (!d.findElements(By.cssSelector(selector)).isEmpty())
                                return true;
                        }
                        return false;
                    });
        } catch (Exception ignored) {
        }

        for (String selector : PLAN_SELECTORS) {
            List<WebElement> elements = driver.findElements(By.cssSelector(selector));

            if (!elements.isEmpty()) {
                List<WebElement> visible = elements.stream()
                        .filter(el -> {
                            try {
                                return el.isDisplayed() && el.getSize().getHeight() > MIN_PLAN_HEIGHT;
                            } catch (Exception e) {
                                return false;
                            }
                        })
                        .toList();

                if (!visible.isEmpty()) {
                    return visible;
                }
            }
        }

        List<WebElement> allDivs = driver.findElements(By.tagName("div"));

        List<WebElement> possiblePlans = allDivs.stream()
                .filter(div -> {
                    try {
                        String text = div.getText();
                        return text.matches(".*\\$\\s*\\d+.*") && div.getSize().getHeight() > MIN_DIV_PLAN_HEIGHT;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .toList();

        if (!possiblePlans.isEmpty()) {
            return possiblePlans;
        }

        return List.of();
    }

    /**
     * Service method: Extract prices from plan cards
     * 
     * @return List of prices
     */
    protected List<BigDecimal> extractPricesFromPlans() {
        List<WebElement> plans = findPlans();

        if (plans.isEmpty()) {
            return extractPricesDirectly();
        }

        return plans.stream()
                .map(this::extractPriceFromCard)
                .filter(price -> price.compareTo(BigDecimal.ZERO) > 0)
                .toList();
    }

    /**
     * Extract price from a single card
     * 
     * @param card Plan card element
     * @return Price as BigDecimal
     */
    private BigDecimal extractPriceFromCard(WebElement card) {
        for (String priceSelector : PRICE_SELECTORS) {
            List<WebElement> priceElements = card.findElements(By.cssSelector(priceSelector));
            for (WebElement priceEl : priceElements) {
                String text = priceEl.getText().trim();
                if (!text.isEmpty() && text.matches(".*\\d+.*")) {
                    String cleaned = text.replaceAll("[^0-9.,]", "").replace(',', '.');
                    if (!cleaned.isEmpty()) {
                        try {
                            BigDecimal price = new BigDecimal(cleaned);
                            if (price.compareTo(BigDecimal.ZERO) > 0) {
                                return price;
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * Fallback: Extract prices directly from page
     * 
     * @return List of prices
     */
    private List<BigDecimal> extractPricesDirectly() {
        try {
            String pageSource = driver.getPageSource();
            List<BigDecimal> prices = new java.util.ArrayList<>();

            Pattern pattern = Pattern.compile("\\$\\s*(\\d+(?:\\.\\d{2})?)");
            Matcher matcher = pattern.matcher(pageSource);

            while (matcher.find()) {
                try {
                    String priceStr = matcher.group(1);
                    BigDecimal price = new BigDecimal(priceStr);
                    if (price.compareTo(BigDecimal.ZERO) > 0 && price.compareTo(MAX_REASONABLE_PRICE) < 0) {
                        prices.add(price);
                    }
                } catch (Exception ignored) {
                }
            }

            if (!prices.isEmpty()) {
                return prices.stream().distinct().limit(10).toList();
            }
        } catch (Exception e) {
        }

        for (String priceSelector : PRICE_SELECTORS) {

            List<WebElement> priceElements = driver.findElements(By.cssSelector(priceSelector));

            List<BigDecimal> prices = priceElements.stream()
                    .map(el -> {
                        try {
                            String text = el.getText().trim();
                            if (!text.isEmpty() && text.matches(".*\\d+.*")) {
                                String cleaned = text.replaceAll("[^0-9.,]", "").replace(',', '.');
                                if (!cleaned.isEmpty()) {
                                    return new BigDecimal(cleaned);
                                }
                            }
                        } catch (Exception ignored) {
                        }
                        return BigDecimal.ZERO;
                    })
                    .filter(price -> price.compareTo(BigDecimal.ZERO) > 0)
                    .toList();

            if (!prices.isEmpty()) {
                return prices;
            }

        }

        return List.of();
    }
}
