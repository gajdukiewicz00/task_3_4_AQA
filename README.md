# Task 3 AQA - Smoke Tests for InMotion Hosting

Автоматизированные smoke-тесты для проверки цен на сайте [InMotion Hosting](https://www.inmotionhosting.com/).

## 📋 Описание

Проект реализует автоматизированные тесты для проверки отображения и корректности цен на страницах Shared Hosting и VPS Hosting. Фреймворк построен на основе **Page Object Pattern** с использованием **Factory Pattern**.

## 🏗️ Архитектура

Проект следует принципам:
- **Page Object Pattern** - каждая страница представлена отдельным классом
- **Factory Pattern** - создание объектов страниц через `PageFactoryManager`
- **Service Methods** - публичные методы страниц возвращают новые объекты страниц
- **Separation of Concerns** - все проверки (assertions) выполняются в тестах, а не в page objects

### Особенности реализации:
- ✅ Все элементы страниц приватные и используют `@FindBy` и `@FindBys`
- ✅ Публичные методы реализуют сервисы, которые предоставляет страница
- ✅ Методы страниц возвращают новые объекты страниц
- ✅ Все проверки выполняются в тестах, а не в page objects

## 🛠️ Технологии

- **Java 17**
- **Selenium WebDriver 4.25.0**
- **TestNG 7.10.2**
- **Allure 2.27.0** - для генерации отчетов
- **WebDriverManager 5.9.2** - автоматическое управление драйверами
- **Maven 3.x** - управление зависимостями и сборка

## 📁 Структура проекта

```
Task_3_AQA/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/example/
│   │           ├── core/
│   │           │   ├── config/          # Конфигурация
│   │           │   ├── driver/          # Управление WebDriver
│   │           │   └── wait/            # Утилиты ожидания
│   │           └── pages/
│   │               ├── components/      # Компоненты страниц
│   │               ├── BasePage.java
│   │               ├── HomePage.java
│   │               ├── PricingHubPage.java
│   │               ├── SharedHostingPage.java
│   │               ├── VpsHostingPage.java
│   │               └── PageFactoryManager.java
│   └── test/
│       └── java/
│           └── com/example/
│               ├── listeners/          # TestNG listeners
│               ├── tests/              # Тестовые классы
│               └── utils/              # Утилиты для тестов
└── pom.xml
```

## ⚙️ Требования

- **JDK 17** или выше
- **Maven 3.6+**
- **Firefox** или **Chrome** браузер (по умолчанию используется Firefox)

## 🚀 Установка и запуск

### 1. Клонирование репозитория

```bash
git clone <repository-url>
cd Task_3_AQA
```

### 2. Сборка проекта

```bash
mvn clean install
```

### 3. Запуск тестов

#### Запуск всех smoke тестов:
```bash
mvn test
```

#### Запуск через TestNG suite:
```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

#### Запуск с параметрами:

**Использование Chrome вместо Firefox:**
```bash
mvn test -Dbrowser=chrome
```

**Запуск в headless режиме:**
```bash
mvn test -Dheadless=true
```

**Изменение базового URL:**
```bash
mvn test -DbaseUrl=https://www.inmotionhosting.com/
```

**Комбинация параметров:**
```bash
mvn test -Dbrowser=chrome -Dheadless=true
```

### 4. Генерация Allure отчетов

```bash
# Генерация отчета
mvn allure:report

# Открытие отчета в браузере
mvn allure:serve
```

## 📝 Тесты

Проект содержит следующие smoke тесты:

1. **sharedHostingPricesAreVisibleAndPositive** - проверяет, что цены на Shared Hosting отображаются и положительны
2. **vpsHostingPricesAreVisibleAndPositive** - проверяет, что цены на VPS Hosting отображаются и положительны
3. **sharedHostingHasAtLeastOnePlan** - проверяет наличие хотя бы одного плана на странице Shared Hosting
4. **vpsHostingHasAtLeastOnePlan** - проверяет наличие хотя бы одного плана на странице VPS Hosting

Все тесты имеют группу `smoke` и используют `RetryOnFailure` для автоматических повторов при падении.

## 🔧 Конфигурация

Настройки проекта находятся в `Config.java`:

- `baseUrl()` - базовый URL сайта (по умолчанию: `https://www.inmotionhosting.com/`)
- `browser()` - браузер для тестов (по умолчанию: `firefox`)
- `headless()` - headless режим (по умолчанию: `true`)
- `defaultTimeoutSec()` - таймаут ожидания элементов (по умолчанию: `15` секунд)

Параметры можно переопределить через системные свойства:
```bash
-Dbrowser=chrome -Dheadless=false -Dtimeout=20
```

## 📊 Отчеты

### TestNG отчеты
После выполнения тестов отчеты доступны в:
```
target/surefire-reports/
```

### Allure отчеты
Для генерации Allure отчетов:
```bash
mvn allure:report
```

Отчеты будут доступны в:
```
target/site/allure-maven-plugin/
```

## 🎯 Page Objects

### HomePage
- `goToPricing()` - переход на страницу Pricing Hub

### PricingHubPage
- `openSharedHosting()` - переход на страницу Shared Hosting
- `openVpsHosting()` - переход на страницу VPS Hosting

### SharedHostingPage / VpsHostingPage
- `getPrices1Year()` - получение списка цен для 1-летнего периода
- `getPlanCount()` - получение количества доступных планов

## 🔄 Retry механизм

Тесты используют `RetryOnFailure` для автоматических повторов при падении. Количество повторов настраивается в `RetryOnFailure.java`.

## 📦 Зависимости

Основные зависимости (см. `pom.xml`):
- `selenium-java:4.25.0`
- `testng:7.10.2`
- `allure-testng:2.27.0`
- `webdrivermanager:5.9.2`


## 🥒 BDD Implementation (Cucumber)

This project includes a BDD layer using Cucumber for testing defects and enhancements.

### Architecture
- **Dependencies**: `cucumber-java`, `cucumber-testng`, `allure-cucumber7-jvm`
- **Runner**: `CucumberTestRunner.java`
- **Features**: Located in `src/test/resources/features`
    - `search_functionality.feature` (Defect 1)
    - `cookie_banner.feature` (Defect 2)

### Running BDD Tests
```bash
# Run all BDD tests
mvn test -Dtest=CucumberTestRunner

# Run specific tags
mvn test -Dcucumber.filter.tags="@defect-1"
```

### Reports
Cucumber HTML reports are generated in `target/cucumber-reports/index.html`. Allure reports integrate BDD steps automatically.

## 🐛 Troubleshooting

### Driver Issues
WebDriverManager automatically handles drivers. Ensure internet connection is active.

### Firefox Issues
If Firefox fails to start, ensure you have the latest version installed or switch to Chrome:
```bash
mvn test -Dbrowser=chrome
```

### Cookie Banner Flakiness
The framework automatically accepts cookies by default. To disable this behavior (e.g., for testing the banner itself), use:
```bash
mvn test -DautoAcceptCookies=false
```



