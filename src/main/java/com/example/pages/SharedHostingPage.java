package com.example.pages;

import com.example.core.wait.Waiter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.math.BigDecimal;
import java.util.List;

public class SharedHostingPage extends BasePage {
    
    private static final String[] PLAN_SELECTORS = {
            "[data-testid*='plan']",
            "[class*='pricing-card']",
            "[class*='plan-card']",
            "[class*='price-card']",
            ".plan",
            ".pricing-card",
            "[id*='plan']",
            "div[class*='plan']:not([class*='plans'])"
    };

    private static final String[] PRICE_SELECTORS = {
            "[data-price]",
            ".price",
            "[class*='price']:not([class*='prices'])",
            "span[class*='price']",
            "div[class*='price']",
            "[class*='cost']"
    };

    /**
     * Service method: Select 1-year term and return prices
     * @return List of prices as BigDecimal
     */
    public List<BigDecimal> getPrices1Year() {
            List<WebElement> termButtons = driver.findElements(By.cssSelector(
                    "button[id*='term-1-year'], button[data-term='1-year'], button[aria-label*='1 year' i], " +
                    "[data-term='1-year'], [id*='term-1-year']"));
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
     * @return List of plan elements
     */
    private List<WebElement> findPlans() {
        Waiter.domReady();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        for (String selector : PLAN_SELECTORS) {
                List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                
                if (!elements.isEmpty()) {
                    List<WebElement> visible = elements.stream()
                            .filter(el -> {
                                try {
                                    return el.isDisplayed() && el.getSize().getHeight() > 50;
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
                            return text.matches(".*\\$\\s*\\d+.*") && div.getSize().getHeight() > 100;
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
     * @return List of prices
     */
    private List<BigDecimal> extractPricesFromPlans() {
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
     * @return List of prices
     */
    private List<BigDecimal> extractPricesDirectly() {
        try {
            String pageSource = driver.getPageSource();
            List<BigDecimal> prices = new java.util.ArrayList<>();
            
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\$\\s*(\\d+(?:\\.\\d{2})?)");
            java.util.regex.Matcher matcher = pattern.matcher(pageSource);
            
            while (matcher.find()) {
                try {
                    String priceStr = matcher.group(1);
                    BigDecimal price = new BigDecimal(priceStr);
                    if (price.compareTo(BigDecimal.ZERO) > 0 && price.compareTo(new BigDecimal("1000")) < 0) {
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
