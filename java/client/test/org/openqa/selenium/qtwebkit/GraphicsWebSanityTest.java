package org.openqa.selenium.qtwebkit;

import org.junit.AfterClass;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.testing.JavascriptEnabled;
import org.openqa.selenium.testing.TestUtilities;
import org.openqa.selenium.testing.drivers.Browser;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.openqa.selenium.TestWaiter.waitFor;
import static org.junit.Assume.assumeFalse;

public class GraphicsWebSanityTest extends JUnit4TestBase {

    private static Logger log = Logger.getLogger(
        org.openqa.selenium.qtwebkit.GraphicsWebSanityTest.class.getName());

    @Before
    public void setUp() {
        driver.get("qtwidget://GraphicsWebViewTestWindows");
        currentWindow = driver.getWindowHandle();
        for (String winHandle : driver.getWindowHandles()) {
            if (!currentWindow.equals(winHandle)) {
                driver.switchTo().window(winHandle);
                break;
            }
        }
        assertTrue(!currentWindow.equals(driver.getWindowHandle()));
    }

    @AfterClass
    public static void cleanUpDriver() {
        JUnit4TestBase.removeDriver();
    }

    @After
    public void clean() {
        driver.switchTo().window(currentWindow);
        assertTrue(currentWindow.equals(driver.getWindowHandle()));
    }

    @Test
    public void testEmbeddedWebViewEnumerated() {
        Set<String> allWindowHandles = driver.getWindowHandles();
        assertEquals(2, allWindowHandles.size());

        for (String handle : allWindowHandles) {
            driver.switchTo().window(handle);
        }
    }

    @Test
    public void testCanClickOnALinkAndFollowIt() {
        driver.get(pages.clicksPage);
        driver.findElement(By.id("normal")).click();

        waitFor(WaitingConditions.pageTitleToBe(driver, "XHTML Test Page"));
    }

    @Test
    public void testCanClickOnALinkThatOverflowsAndFollowIt() {
        driver.get(pages.clicksPage);
        driver.findElement(By.id("overflowLink")).click();

        waitFor(WaitingConditions.pageTitleToBe(driver, "XHTML Test Page"));
    }

    @Test
    public void testCanClickOnALinkThatUpdatesAnotherFrame() {
        driver.get(pages.clicksPage);
        driver.switchTo().frame("source");

        driver.findElement(By.id("otherframe")).click();
        driver.switchTo().defaultContent().switchTo().frame("target");

        waitFor(WaitingConditions.pageSourceToContain(driver, "Hello WebDriver"));
    }

    @JavascriptEnabled
    @Test
    public void testElementsFoundByJsCanLoadUpdatesInAnotherFrame() {
        driver.get(pages.clicksPage);
        driver.switchTo().frame("source");

        WebElement toClick = (WebElement) ((JavascriptExecutor) driver).executeScript(
            "return document.getElementById('otherframe');"
        );
        toClick.click();
        driver.switchTo().defaultContent().switchTo().frame("target");

        assertTrue("Target did not reload",
                   driver.getPageSource().contains("Hello WebDriver"));
    }

    @JavascriptEnabled
    @Test
    public void testJsLocatedElementsCanUpdateFramesIfFoundSomehowElse() {
        driver.get(pages.clicksPage);
        driver.switchTo().frame("source");

        // Prime the cache of elements
        driver.findElement(By.id("otherframe"));

        // This _should_ return the same element
        WebElement toClick = (WebElement) ((JavascriptExecutor) driver).executeScript(
            "return document.getElementById('otherframe');"
        );
        toClick.click();
        driver.switchTo().defaultContent().switchTo().frame("target");

        assertTrue("Target did not reload",
                   driver.getPageSource().contains("Hello WebDriver"));
    }

    @JavascriptEnabled
    @Test
    public void testCanClickOnAnElementWithTopSetToANegativeNumber() {
        String page = appServer.whereIs("styledPage.html");
        driver.get(page);
        WebElement searchBox = driver.findElement(By.name("searchBox"));
        searchBox.sendKeys("Cheese");
        driver.findElement(By.name("btn")).click();

        String log = driver.findElement(By.id("log")).getText();
        assertEquals("click", log);
    }

    @Test
    public void testShouldClickOnFirstBoundingClientRectWithNonZeroSize() {
        driver.get(pages.clicksPage);
        driver.findElement(By.id("twoClientRects")).click();
        waitFor(WaitingConditions.pageTitleToBe(driver, "XHTML Test Page"));
    }

    @Test
    public void testClickingLabelShouldSetCheckbox() {
        driver.get(pages.formPage);

        driver.findElement(By.id("label-for-checkbox-with-label")).click();

        assertTrue(
            "Should be selected",
            driver.findElement(By.id("checkbox-with-label")).isSelected());
    }

    @Test
    public void testCanClickOnALinkWithEnclosedImage() {
        driver.get(pages.clicksPage);
        driver.findElement(By.id("link-with-enclosed-image")).click();

        waitFor(WaitingConditions.pageTitleToBe(driver, "XHTML Test Page"));
    }

    @Test
    public void testCanClickOnAnImageEnclosedInALink() {
        driver.get(pages.clicksPage);
        driver.findElement(By.id("link-with-enclosed-image")).findElement(By.tagName("img"))
            .click();

        waitFor(WaitingConditions.pageTitleToBe(driver, "XHTML Test Page"));
    }

    @Test
    public void testCanClickOnALinkThatContainsTextWrappedInASpan() {
        driver.get(pages.clicksPage);
        driver.findElement(By.id("link-with-enclosed-span")).click();

        waitFor(WaitingConditions.pageTitleToBe(driver, "XHTML Test Page"));
    }

    @Test
    public void testCanClickOnALinkThatContainsEmbeddedBlockElements() {
        driver.get(pages.clicksPage);
        driver.findElement(By.id("embeddedBlock")).click();
        waitFor(WaitingConditions.pageTitleToBe(driver, "XHTML Test Page"));
    }

    @Test
    public void testCanClickOnAnElementEnclosedInALink() {
        driver.get(pages.clicksPage);
        driver.findElement(By.id("link-with-enclosed-span")).findElement(By.tagName("span"))
            .click();

        waitFor(WaitingConditions.pageTitleToBe(driver, "XHTML Test Page"));
    }

    // See http://code.google.com/p/selenium/issues/attachmentText?id=2700
    @Test
    public void testShouldBeAbleToClickOnAnElementInTheViewport() {
        String url = appServer.whereIs("click_out_of_bounds.html");

        driver.get(url);
        WebElement button = driver.findElement(By.id("button"));

        try {
            button.click();
        } catch (MoveTargetOutOfBoundsException e) {
            fail("Should not be out of bounds: " + e.getMessage());
        }
    }

    @Test
    public void testClicksASurroundingStrongTag() {
        driver.get(appServer.whereIs("ClickTest_testClicksASurroundingStrongTag.html"));
        driver.findElement(By.tagName("a")).click();
        waitFor(WaitingConditions.pageTitleToBe(driver, "XHTML Test Page"));
    }

    @Test
    public void testShouldBeAbleToClickOnAnElementGreaterThanTwoViewports() {
        String url = appServer.whereIs("click_too_big.html");
        driver.get(url);

        WebElement element = driver.findElement(By.id("click"));

        element.click();

        waitFor(WaitingConditions.pageTitleToBe(driver, "clicks"));
    }

    @Test
    public void testShouldBeAbleToClickOnAnElementInFrameGreaterThanTwoViewports() {
        String url = appServer.whereIs("click_too_big_in_frame.html");
        driver.get(url);

        WebElement frame = driver.findElement(By.id("iframe1"));
        driver.switchTo().frame(frame);

        WebElement element = driver.findElement(By.id("click"));
        element.click();

        waitFor(WaitingConditions.pageTitleToBe(driver, "clicks"));
    }

    @Test
    public void testShouldGetCoordinatesOfAnElementInViewPort() {
        driver.get(appServer.whereIs("coordinates_tests/simple_page.html"));
        assertThat(getLocationInViewPort(By.id("box")), is(new Point(10, 10)));
    }

    @Test
    public void testShouldGetCoordinatesOfAnEmptyElement() {
        driver.get(appServer.whereIs("coordinates_tests/page_with_empty_element.html"));
        assertThat(getLocationInViewPort(By.id("box")), is(new Point(10, 10)));
    }

    @Test
    public void testShouldGetCoordinatesOfATransparentElement() {
        driver.get(appServer.whereIs("coordinates_tests/page_with_transparent_element.html"));
        assertThat(getLocationInViewPort(By.id("box")), is(new Point(10, 10)));
    }

    @Test
    public void testShouldGetCoordinatesOfAHiddenElement() {
        driver.get(appServer.whereIs("coordinates_tests/page_with_hidden_element.html"));
        assertThat(getLocationInViewPort(By.id("box")), is(new Point(10, 10)));
    }

    @Test
    public void testShouldGetCoordinatesOfAnInvisibleElement() {
        driver.get(appServer.whereIs("coordinates_tests/page_with_invisible_element.html"));
        assertThat(getLocationInViewPort(By.id("box")), is(new Point(0, 0)));
    }

    @Test
    public void testShouldScrollPageAndGetCoordinatesOfAnElementThatIsOutOfViewPort() {
        driver.get(appServer.whereIs("coordinates_tests/page_with_element_out_of_view.html"));
        int windowHeight = driver.manage().window().getSize().getHeight();
        Point location = getLocationInViewPort(By.id("box"));
        assertThat(location.getX(), is(10));
        assertThat(location.getY(), greaterThanOrEqualTo(0));
        assertThat(location.getY(), lessThanOrEqualTo(windowHeight - 100));
    }

    @Test
    public void testShouldGetCoordinatesOfAnElementInAFrame() {
        driver.get(appServer.whereIs("coordinates_tests/element_in_frame.html"));
        driver.switchTo().frame("ifr");
        WebElement box = driver.findElement(By.id("box"));
        assertThat(box.getLocation(), is(new Point(10, 10)));
    }

    @Test
    public void testShouldGetCoordinatesInViewPortOfAnElementInAFrame() {
        driver.get(appServer.whereIs("coordinates_tests/element_in_frame.html"));
        driver.switchTo().frame("ifr");
        assertThat(getLocationInViewPort(By.id("box")), is(new Point(25, 25)));
    }

    @Test
    public void testShouldGetCoordinatesInViewPortOfAnElementInANestedFrame() {
        driver.get(appServer.whereIs("coordinates_tests/element_in_nested_frame.html"));
        driver.switchTo().frame("ifr");
        driver.switchTo().frame("ifr");
        assertThat(getLocationInViewPort(By.id("box")), is(new Point(40, 40)));
    }

    private Point getLocationInViewPort(By locator) {
        WebElement element = driver.findElement(locator);
        return ((Locatable) element).getCoordinates().inViewPort();
    }

    @Test
    public void testShouldReturnNullWhenGettingTheValueOfAnAttributeThatIsNotListed() {
        driver.get(pages.simpleTestPage);
        WebElement head = driver.findElement(By.xpath("/html"));
        String attribute = head.getAttribute("cheese");
        assertThat(attribute, is(nullValue()));
    }

    @Test
    public void testShouldReturnNullWhenGettingSrcAttributeOfInvalidImgTag() {
        driver.get(pages.simpleTestPage);
        WebElement img = driver.findElement(By.id("invalidImgTag"));
        String attribute = img.getAttribute("src");
        assertThat(attribute, is(nullValue()));
    }

    @Test
    public void testShouldReturnAnAbsoluteUrlWhenGettingSrcAttributeOfAValidImgTag() {
        driver.get(pages.simpleTestPage);
        WebElement img = driver.findElement(By.id("validImgTag"));
        String attribute = img.getAttribute("src");
        assertThat(attribute, equalTo(appServer.whereIs("icon.gif")));
    }

    @Test
    public void testShouldReturnAnAbsoluteUrlWhenGettingHrefAttributeOfAValidAnchorTag() {
        driver.get(pages.simpleTestPage);
        WebElement img = driver.findElement(By.id("validAnchorTag"));
        String attribute = img.getAttribute("href");
        assertThat(attribute, equalTo(appServer.whereIs("icon.gif")));
    }

    @Test
    public void testShouldReturnEmptyAttributeValuesWhenPresentAndTheValueIsActuallyEmpty() {
        driver.get(pages.simpleTestPage);
        WebElement body = driver.findElement(By.xpath("//body"));
        assertThat(body.getAttribute("style"), equalTo(""));
    }

    @Test
    public void testShouldReturnTheValueOfTheDisabledAttributeAsNullIfNotSet() {
        driver.get(pages.formPage);
        WebElement inputElement = driver.findElement(By.xpath("//input[@id='working']"));
        assertThat(inputElement.getAttribute("disabled"), equalTo(null));
        assertThat(inputElement.isEnabled(), equalTo(true));

        WebElement pElement = driver.findElement(By.id("peas"));
        assertThat(pElement.getAttribute("disabled"), equalTo(null));
        assertThat(pElement.isEnabled(), equalTo(true));
    }

    @Test
    public void testShouldReturnTheValueOfTheIndexAttrbuteEvenIfItIsMissing() {
        driver.get(pages.formPage);

        WebElement multiSelect = driver.findElement(By.id("multi"));
        List<WebElement> options = multiSelect.findElements(By.tagName("option"));
        assertThat(options.get(1).getAttribute("index"), equalTo("1"));
    }

    @Test
    public void testShouldIndicateTheElementsThatAreDisabledAreNotEnabled() {
        driver.get(pages.formPage);
        WebElement inputElement = driver.findElement(By.xpath("//input[@id='notWorking']"));
        assertThat(inputElement.isEnabled(), is(false));

        inputElement = driver.findElement(By.xpath("//input[@id='working']"));
        assertThat(inputElement.isEnabled(), is(true));
    }

    @Test
    public void testElementsShouldBeDisabledIfTheyAreDisabledUsingRandomDisabledStrings() {
        driver.get(pages.formPage);
        WebElement disabledTextElement1 = driver.findElement(By.id("disabledTextElement1"));
        assertThat(disabledTextElement1.isEnabled(), is(false));

        WebElement disabledTextElement2 = driver.findElement(By.id("disabledTextElement2"));
        assertThat(disabledTextElement2.isEnabled(), is(false));

        WebElement disabledSubmitElement = driver.findElement(By.id("disabledSubmitElement"));
        assertThat(disabledSubmitElement.isEnabled(), is(false));
    }

    @Test
    public void testShouldThrowExceptionIfSendingKeysToElementDisabledUsingRandomDisabledStrings() {
        driver.get(pages.formPage);
        WebElement disabledTextElement1 = driver.findElement(By.id("disabledTextElement1"));
        try {
            disabledTextElement1.sendKeys("foo");
            fail("Should have thrown exception");
        } catch (InvalidElementStateException e) {
            // Expected
        }
        assertThat(disabledTextElement1.getText(), is(""));

        WebElement disabledTextElement2 = driver.findElement(By.id("disabledTextElement2"));
        try {
            disabledTextElement2.sendKeys("bar");
            fail("Should have thrown exception");
        } catch (InvalidElementStateException e) {
            // Expected
        }
        assertThat(disabledTextElement2.getText(), is(""));
    }

    @Test
    public void testShouldIndicateWhenATextAreaIsDisabled() {
        driver.get(pages.formPage);
        WebElement textArea = driver.findElement(By.xpath("//textarea[@id='notWorkingArea']"));
        assertThat(textArea.isEnabled(), is(false));
    }

    @Test
    public void testShouldIndicateWhenASelectIsDisabled() {
        driver.get(pages.formPage);

        WebElement enabled = driver.findElement(By.name("selectomatic"));
        WebElement disabled = driver.findElement(By.name("no-select"));

        assertTrue(enabled.isEnabled());
        assertFalse(disabled.isEnabled());
    }

    @Test
    public void testShouldReturnTheValueOfCheckedForACheckboxOnlyIfItIsChecked() {
        driver.get(pages.formPage);
        WebElement checkbox = driver.findElement(By.xpath("//input[@id='checky']"));
        assertThat(checkbox.getAttribute("checked"), equalTo(null));
        checkbox.click();
        assertThat(checkbox.getAttribute("checked"), equalTo("true"));
    }

    @Test
    public void testShouldOnlyReturnTheValueOfSelectedForRadioButtonsIfItIsSet() {
        driver.get(pages.formPage);
        WebElement neverSelected = driver.findElement(By.id("cheese"));
        WebElement initiallyNotSelected = driver.findElement(By.id("peas"));
        WebElement initiallySelected = driver.findElement(By.id("cheese_and_peas"));

        assertThat(neverSelected.getAttribute("selected"), equalTo(null));
        assertThat(initiallyNotSelected.getAttribute("selected"), equalTo(null));
        assertThat(initiallySelected.getAttribute("selected"), equalTo("true"));

        initiallyNotSelected.click();
        assertThat(neverSelected.getAttribute("selected"), equalTo(null));
        assertThat(initiallyNotSelected.getAttribute("selected"), equalTo("true"));
        assertThat(initiallySelected.getAttribute("selected"), equalTo(null));
    }

    @Test
    public void testShouldReturnTheValueOfSelectedForOptionsOnlyIfTheyAreSelected() {
        driver.get(pages.formPage);
        WebElement selectBox = driver.findElement(By.xpath("//select[@name='selectomatic']"));
        List<WebElement> options = selectBox.findElements(By.tagName("option"));
        WebElement one = options.get(0);
        WebElement two = options.get(1);
        assertThat(one.isSelected(), is(true));
        assertThat(two.isSelected(), is(false));
        assertThat(one.getAttribute("selected"), equalTo("true"));
        assertThat(two.getAttribute("selected"), equalTo(null));
    }

    @Test
    public void testShouldReturnValueOfClassAttributeOfAnElement() {
        driver.get(pages.xhtmlTestPage);

        WebElement heading = driver.findElement(By.xpath("//h1"));
        String className = heading.getAttribute("class");

        assertThat(className, equalTo("header"));
    }

    @Test
    public void testShouldReturnTheContentsOfATextAreaAsItsValue() {
        driver.get(pages.formPage);

        String value = driver.findElement(By.id("withText")).getAttribute("value");

        assertThat(value, equalTo("Example text"));
    }

    @Test
    public void testShouldTreatReadonlyAsAValue() {
        driver.get(pages.formPage);

        WebElement element = driver.findElement(By.name("readonly"));
        String readonly = element.getAttribute("readonly");

        assertNotNull(readonly);

        WebElement textInput = driver.findElement(By.name("x"));
        String notReadonly = textInput.getAttribute("readonly");

        assertFalse(readonly.equals(notReadonly));
    }

    @Test
    public void testShouldGetNumericAtribute() {
        driver.get(pages.formPage);
        WebElement element = driver.findElement(By.id("withText"));
        assertThat(element.getAttribute("rows"), is("5"));
    }

    @Test
    public void testCanReturnATextApproximationOfTheStyleAttribute() {
        driver.get(pages.javascriptPage);

        String style = driver.findElement(By.id("red-item")).getAttribute("style");

        assertTrue(style.toLowerCase().contains("background-color"));
    }

    @Test
    public void testShouldReturnValueOfOnClickAttribute() {
        driver.get(pages.javascriptPage);

        WebElement mouseclickDiv = driver.findElement(By.id("mouseclick"));

        String onClickValue = mouseclickDiv.getAttribute("onclick");
        String expectedOnClickValue = "displayMessage('mouse click');";
        assertThat("Javascript code expected", onClickValue, anyOf(
            equalTo("javascript:" + expectedOnClickValue), // Non-IE
            equalTo("function anonymous()\n{\n" + expectedOnClickValue + "\n}"), // IE
            equalTo("function onclick()\n{\n" + expectedOnClickValue + "\n}"))); // IE

        WebElement mousedownDiv = driver.findElement(By.id("mousedown"));
        assertEquals(null, mousedownDiv.getAttribute("onclick"));
    }

    @Test
    public void testGetAttributeDoesNotReturnAnObjectForSvgProperties() {
        driver.get(pages.svgPage);
        WebElement svgElement = driver.findElement(By.id("rotate"));
        assertEquals("rotate(30)", svgElement.getAttribute("transform"));
    }

    @Test
    public void testCanRetrieveTheCurrentValueOfATextFormField_textInput() {
        driver.get(pages.formPage);
        WebElement element = driver.findElement(By.id("working"));
        assertEquals("", element.getAttribute("value"));
        element.sendKeys("hello world");
        assertEquals("hello world", element.getAttribute("value"));
    }

    @Test
    public void testCanRetrieveTheCurrentValueOfATextFormField_emailInput() {
        driver.get(pages.formPage);
        WebElement element = driver.findElement(By.id("email"));
        assertEquals("", element.getAttribute("value"));
        element.sendKeys("hello@example.com");
        assertEquals("hello@example.com", element.getAttribute("value"));
    }

    @Test
    public void testCanRetrieveTheCurrentValueOfATextFormField_textArea() {
        driver.get(pages.formPage);
        WebElement element = driver.findElement(By.id("emptyTextArea"));
        assertEquals("", element.getAttribute("value"));
        element.sendKeys("hello world");
        assertEquals("hello world", element.getAttribute("value"));
    }

    @Test
    public void testShouldReturnNullForNonPresentBooleanAttributes() {
        driver.get(pages.booleanAttributes);
        WebElement element1 = driver.findElement(By.id("working"));
        assertNull(element1.getAttribute("required"));
        WebElement element2 = driver.findElement(By.id("wallace"));
        assertNull(element2.getAttribute("nowrap"));
    }

    private Actions getBuilder(WebDriver driver) {
        return new Actions(driver);
    }

    @JavascriptEnabled
    @Test
    public void testBasicKeyboardInput() {
        driver.get(pages.javascriptPage);

        WebElement keyReporter = driver.findElement(By.id("keyReporter"));

        Action sendLowercase = getBuilder(driver).sendKeys(keyReporter, "abc def").build();

        sendLowercase.perform();

        assertThat(keyReporter.getAttribute("value"), is("abc def"));
    }

    @JavascriptEnabled
    @Test
    public void testSendingKeyDownOnly() {
        ignoreOnFfWindowsWithNativeEvents(); // Issue 3722

        driver.get(pages.javascriptPage);

        WebElement keysEventInput = driver.findElement(By.id("theworks"));

        Action pressShift = getBuilder(driver).keyDown(keysEventInput, Keys.SHIFT).build();

        pressShift.perform();

        WebElement keyLoggingElement = driver.findElement(By.id("result"));
        String logText = keyLoggingElement.getText();

        Action releaseShift = getBuilder(driver).keyUp(keysEventInput, Keys.SHIFT).build();
        releaseShift.perform();

        assertTrue("Key down event not isolated, got: " + logText,
                   logText.endsWith("keydown"));
    }

    @JavascriptEnabled
    @Test
    public void testSendingKeyUp() {
        ignoreOnFfWindowsWithNativeEvents(); // Issue 3722

        driver.get(pages.javascriptPage);
        WebElement keysEventInput = driver.findElement(By.id("theworks"));

        Action pressShift = getBuilder(driver).keyDown(keysEventInput, Keys.SHIFT).build();
        pressShift.perform();

        WebElement keyLoggingElement = driver.findElement(By.id("result"));

        String eventsText = keyLoggingElement.getText();
        assertTrue("Key down should be isolated for this test to be meaningful. " +
                   "Got events: " + eventsText, eventsText.endsWith("keydown"));

        Action releaseShift = getBuilder(driver).keyUp(keysEventInput, Keys.SHIFT).build();

        releaseShift.perform();

        eventsText = keyLoggingElement.getText();
        assertTrue("Key up event not isolated. Got events: " + eventsText,
                   eventsText.endsWith("keyup"));
    }

    @JavascriptEnabled
    @Test
    public void testSendingKeysToActiveElement() {
        driver.get(pages.bodyTypingPage);

        Action someKeys = getBuilder(driver).sendKeys("ab").build();
        someKeys.perform();

        assertThatBodyEventsFiredAreExactly("keypress keypress");
        assertThatFormEventsFiredAreExactly("");
    }

    @Test
    public void testBasicKeyboardInputOnActiveElement() {
        driver.get(pages.javascriptPage);

        WebElement keyReporter = driver.findElement(By.id("keyReporter"));

        keyReporter.click();

        Action sendLowercase = getBuilder(driver).sendKeys("abc def").build();

        sendLowercase.perform();

        assertThat(keyReporter.getAttribute("value"), is("abc def"));
    }

    private void assertThatFormEventsFiredAreExactly(String message, String expected) {
        assertThat(message, getFormEvents(), is(expected.trim()));
    }

    private String getFormEvents() {
        return driver.findElement(By.id("result")).getText().trim();
    }

    private void assertThatFormEventsFiredAreExactly(String expected) {
        assertThatFormEventsFiredAreExactly("", expected);
    }

    private void assertThatBodyEventsFiredAreExactly(String expected) {
        assertThat(driver.findElement(By.id("body_result")).getText().trim(), is(expected.trim()));
    }

    private void ignoreOnFfWindowsWithNativeEvents() {
        assumeFalse(Browser.detect() == Browser.ff &&
                    TestUtilities.getEffectivePlatform().is(Platform.WINDOWS) &&
                    TestUtilities.isNativeEventsEnabled(driver));
    }

    private String currentWindow;
}
