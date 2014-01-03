package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.Callable;

import static org.junit.Assert.assertNotNull;
import static org.openqa.selenium.TestWaiter.waitFor;
import static org.openqa.selenium.WaitingConditions.alertToBePresent;
import static org.openqa.selenium.WaitingConditions.elementTextToContain;
import static org.openqa.selenium.qtwebkit.visualizer_tests.WaitingConditions.elementToBeDisplayed;
import static org.openqa.selenium.qtwebkit.visualizer_tests.WaitingConditions.pageUrlToBe;

public class QtWebDriverJsPage {
  private WebDriver driver;
  private WebDriver targetDriver;
  private String webDriverJsWindowHandle;

  private WebElement webDriverUrlPort;
  private WebElement webPage;
  private String webPageValue;

  private WebElement getButton;
  private WebElement screenshotButton;
  private WebElement logsSelect;

  @FindBy(xpath = "//input[@value='Source']")
  private WebElement sourceButton;

  private WebElement error;

  private WebElement findElementCriteria;
  private WebElement findElementKey;
  @FindBy(xpath = "//input[@value = 'Find element']")
  private WebElement findElementButton;
  private WebElement foundElement;

  @FindBy(xpath = "//*[@id = 'elementActions']/input[@value = 'Click']")
  private WebElement foundElementClick;

  @FindBy(xpath = "//*[@id = 'elementActions']/input[@value = 'Submit']")
  private WebElement foundElementSubmit;

  @FindBy(xpath = "//*[@id = 'elementActions']/input[@value = 'Clear']")
  private WebElement foundElementClear;

  @FindBy(xpath = "//*[@id = 'elementActions']/input[@value = 'Tag name']")
  private WebElement foundElementTagName;

  @FindBy(xpath = "//*[@id = 'elementActions']/input[@value = 'Text']")
  private WebElement foundElementText;

  @FindBy(xpath = "//*[@id = 'elementActions']/input[@value = 'Location']")
  private WebElement foundElementLocation;

  @FindBy(xpath = "//*[@id = 'elementActions']/input[@value = 'Size']")
  private WebElement foundElementSize;

  @FindBy(xpath = "//*[@id = 'elementActions']/input[@value = 'Selected?']")
  private WebElement foundElementSelected;

  @FindBy(xpath = "//*[@id = 'elementActions']/input[@value = 'Enabled?']")
  private WebElement foundElementEnabled;

  @FindBy(xpath = "//*[@id = 'elementActions']/input[@value = 'Displayed?']")
  private WebElement foundElementDisplayed;

  private WebElement listWindowButton;
  private WebElement windowList;
  private WebElement chooseWindow;

  private WebElement windowSizeWidth;
  private WebElement windowSizeHeight;
  @FindBy(xpath = "//input[@value = 'Set window size']")
  private WebElement windowSizeButton;

  private Point getLocationFromAlert(String caption) {
    waitFor(alertToBePresent(driver));
    String value = driver.switchTo().alert().getText().replace(caption, "").trim();
    driver.switchTo().alert().accept();
    try {
      JSONObject json = new JSONObject(value);
      return new Point(json.getInt("x"), json.getInt("y"));
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }

  private Dimension getDimensionFromAlert(String caption) {
    waitFor(alertToBePresent(driver));
    String value = driver.switchTo().alert().getText().replace(caption, "").trim();
    driver.switchTo().alert().accept();
    try {
      JSONObject json = new JSONObject(value);
      return new Dimension(json.getInt("width"), json.getInt("height"));
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }

  private String getStringFromAlert(String caption) {
    waitFor(alertToBePresent(driver));
    String value = driver.switchTo().alert().getText().replace(caption, "").trim();
    driver.switchTo().alert().accept();
    return value;
  }

  private boolean getBooleanFromAlert(String caption) {
    waitFor(alertToBePresent(driver));
    String value = driver.switchTo().alert().getText().replace(caption, "").trim();
    driver.switchTo().alert().accept();
    return Boolean.valueOf(value);
  }

  private String getVisualizerWindowHandle() {
    String currentWindowHandle = driver.getWindowHandle();
    String visualizerWindowHandle = null;
    for (String windowHandle : driver.getWindowHandles()) {
      String title = driver.switchTo().window(windowHandle).getTitle();
      if (title.endsWith(" - Visualizer")) {
        visualizerWindowHandle = windowHandle;
        break;
      }
    }
    driver.switchTo().window(currentWindowHandle);
    return visualizerWindowHandle;
  }

  public void setDriver(WebDriver driver) {
    this.driver = driver;
  }

  public void setTargetDriver(WebDriver targetDriver) {
    this.targetDriver = targetDriver;
  }

  public String getWebDriverJsWindowHandle() {
    return webDriverJsWindowHandle;
  }

  public void setWebDriverUrl(String url) {
    driver.get(url + "/WebDriverJsDemo.html");
    webDriverJsWindowHandle = driver.getWindowHandle();

    webDriverUrlPort.clear();
    webDriverUrlPort.sendKeys(url);
  }

  public void setWebPage(String webPage) {
    this.webPage.clear();
    this.webPage.sendKeys(webPage);
    webPageValue = webPage;
  }

  public void clickGet() {
    getButton.click();
    waitFor(pageUrlToBe(targetDriver, webPageValue));
  }

  public String clickSource() {
    String visualizerWindow = getVisualizerWindowHandle();
    if (visualizerWindow != null) {
      driver.switchTo().window(visualizerWindow).close();
    }

    driver.switchTo().window(webDriverJsWindowHandle);
    sourceButton.click();
    visualizerWindow = waitFor(new Callable<String>() {
      @Override
      public String call() throws Exception {
        return getVisualizerWindowHandle();
      }
    });

    assertNotNull(visualizerWindow);
    return visualizerWindow;
  }

  public void clickScreenshotButton() {
    screenshotButton.click();
  }

  public void clickLogsSelect(String type) {
    new Select(logsSelect).selectByValue(type);
  }

  public String findElement(String criteria, String key) {
    findElementKey.clear();
    findElementKey.sendKeys("c7c3179a38f864a463729657f15871326baccede");
    findElementButton.click();
    waitFor(elementTextToContain(error, "The element could not be found"));

    new Select(findElementCriteria).selectByValue(criteria);
    findElementKey.clear();
    findElementKey.sendKeys(key);
    findElementButton.click();
    waitFor(elementTextToContain(foundElement, "Found element"));

    return foundElement.getText().substring("Found element".length()).trim();
  }

  public void clickElementClick() {
    foundElementClick.click();
  }

  public void clickElementSubmit() {
    foundElementSubmit.click();
  }

  public void clickElementClear() {
    foundElementClear.click();
  }

  public String getFoundElementTagName() {
    foundElementTagName.click();
    return getStringFromAlert("Element tag name:");
  }

  public String getFoundElementText() {
    foundElementText.click();
    return getStringFromAlert("Element text:");
  }

  public Point getFoundElementLocation() {
    foundElementLocation.click();
    return getLocationFromAlert("Element location:");
  }

  public Dimension getFoundElementSize() {
    foundElementSize.click();
    return getDimensionFromAlert("Element size:");
  }

  public boolean isFoundElementSelected() {
    foundElementSelected.click();
    return getBooleanFromAlert("Element selection:");
  }

  public boolean isFoundElementEnabled() {
    foundElementEnabled.click();
    return getBooleanFromAlert("Element enabled:");
  }

  public boolean isFoundElementDisplayed() {
    foundElementDisplayed.click();
    return getBooleanFromAlert("Element displayed:");
  }

  public void clickListWindowHandles() {
    listWindowButton.click();
    waitFor(elementToBeDisplayed(windowList));
  }

  public Select getWindowListSelect() {
    return new Select(windowList);
  }

  public void clickChooseWindow() {
    chooseWindow.click();
  }

  public void setWindowSize(Dimension dimension) {
    windowSizeWidth.clear();
    windowSizeWidth.sendKeys(Integer.valueOf(dimension.getWidth()).toString());

    windowSizeHeight.clear();
    windowSizeHeight.sendKeys(Integer.valueOf(dimension.getHeight()).toString());

    windowSizeButton.click();
  }
}
