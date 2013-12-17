package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import static org.openqa.selenium.TestWaiter.waitFor;
import static org.openqa.selenium.WaitingConditions.elementTextToContain;
import static org.openqa.selenium.qtwebkit.visualizer_tests.WaitingConditions.elementToBeDisplayed;
import static org.openqa.selenium.qtwebkit.visualizer_tests.WaitingConditions.pageUrlToBe;

public class QtWebDriverJsPage {
  protected WebDriver targetDriver;

  private WebElement webDriverUrlPort;
  private WebElement webPage;
  private String webPageValue;

  private WebElement getButton;
  private WebElement screenshotButton;
  private WebElement logsSelect;

  private WebElement findElementCriteria;
  private WebElement findElementKey;
  @FindBy(xpath = "//input[@value = 'Find element']")
  private WebElement findElementButton;
  private WebElement foundElement;

  private WebElement listWindowButton;
  private WebElement windowList;
  private WebElement chooseWindow;

  public void setTargetDriver(WebDriver targetDriver) {
    this.targetDriver = targetDriver;
  }

  public void setWebDriverUrl(String url) {
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
    waitFor(elementTextToContain(foundElement, "The element could not be found"));

    new Select(findElementCriteria).selectByValue(criteria);
    findElementKey.clear();
    findElementKey.sendKeys(key);
    findElementButton.click();
    waitFor(elementTextToContain(foundElement, "Found element"));

    return foundElement.getText().substring("Found element".length()).trim();
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
}
