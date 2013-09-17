package org.openqa.selenium.qtwebkit.quick_tests;

import org.openqa.selenium.*;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.openqa.selenium.TestWaiter.waitFor;
import static org.openqa.selenium.testing.Ignore.Driver.ANDROID;
import static org.openqa.selenium.testing.Ignore.Driver.IPHONE;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA;

public class EmbeddedWebViewTest extends JUnit4TestBase {

    private static Logger log = Logger.getLogger(EmbeddedWebViewTest.class.getName());

    @Before
    public void setUp() {
        driver.get(appServer.whereIs("quick1/WebviewTest.qml"));
        currentWindow = driver.getWindowHandle();
        for (String winHandle : driver.getWindowHandles()) {
            if (!currentWindow.equals(winHandle)) {
                driver.switchTo().window(winHandle);
                break;
            }
        }
    }

    @After
    public void clean() {
        driver.switchTo().window(currentWindow);
//        for (String winHandle : driver.getWindowHandles()) {
//            if (!currentWindow.equals(winHandle)) {
//                driver.switchTo().window(winHandle);
//                driver.close();
//            }
//        }
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
    public void testCanClickOnAPushButton() {
        driver.get(pages.clicksPage);
        driver.findElement(By.id("button1")).click();
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        waitFor(
            WaitingConditions.elementTextToEqual(driver, By.id("clickDisplay"), "clicked button1"));
    }

    @Test
    public void testCanClickOnMouseArea() {
        driver.get(pages.clicksPage);
        driver.findElement(By.id("mouseHotSpotArea2")).click();

        waitFor(
            WaitingConditions.elementTextToEqual(driver, By.id("clickDisplay"), "clicked area 2"));
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

    @Ignore({OPERA, IPHONE, ANDROID})
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

    @Ignore(value = {IPHONE},
            reason = "sendKeys does not determine whether the element is disabled")
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


    private String currentWindow;
}
