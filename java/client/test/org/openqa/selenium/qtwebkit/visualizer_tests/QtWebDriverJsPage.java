package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static org.openqa.selenium.TestWaiter.waitFor;
import static org.openqa.selenium.qtwebkit.visualizer_tests.WaitingConditions.elementToBeDisplayed;
import static org.openqa.selenium.qtwebkit.visualizer_tests.WaitingConditions.pageUrlToBe;

public class QtWebDriverJsPage {
  protected WebDriver driver2;

  private WebElement webDriverUrlPort;
  private WebElement webPage;
  private String webPageValue;

  private WebElement getButton;

  private WebElement listWindowButton;
  private WebElement windowList;
  private WebElement chooseWindow;

  public void setDriver2(WebDriver driver2) {
    this.driver2 = driver2;
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
    waitFor(pageUrlToBe(driver2, webPageValue));
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
