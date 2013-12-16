package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import static org.openqa.selenium.TestWaiter.waitFor;
import static org.openqa.selenium.qtwebkit.visualizer_tests.WaitingConditions.elementToBeDisplayed;
import static org.openqa.selenium.qtwebkit.visualizer_tests.WaitingConditions.pageUrlToBe;

public class QtWebDriverJsPage {
  protected WebDriver driver2;

  private WebElement webDriverUrlPort;
  private WebElement webPage;
  private String webPageValue;

  @FindBy(xpath = "//input[@value='GET']")
  private WebElement getButton;

  @FindBy(xpath = "//input[@value='List window handles']")
  private WebElement listWindowHandlesButton;

  private WebElement windowList;

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

  public Select getWindowListSelect() {
    return new Select(windowList);
  }

  public void clickGet() {
    getButton.click();
    waitFor(pageUrlToBe(driver2, webPageValue));
  }

  public void clickListWindowHandles() {
    listWindowHandlesButton.click();
    waitFor(elementToBeDisplayed(windowList));
  }
}
