/*
Copyright 2012 Selenium committers
Copyright 2012 Software Freedom Conservancy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


package org.openqa.selenium.qtwebkit.quick_tests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;

public class ElementEqualityTest extends JUnit4TestBase {

    @Before
    public void createWebDriver() {
        driver.get(pages.findingTest);
    }

    @Test
    public void testSameElementLookedUpDifferentWaysShouldBeEqual() {

        WebElement body = driver.findElement(By.tagName("QDeclarativeTextInput"));
        WebElement xbody = driver.findElements(By.xpath("//QDeclarativeTextInput")).get(0);

        assertEquals(body, xbody);
    }

    @Test
    public void testDifferentElementsShouldNotBeEqual() {

        List<WebElement> ps = driver.findElements(By.tagName("QDeclarativeRectangle"));

        assertFalse(ps.get(0).equals(ps.get(1)));
    }

    @Test
    @Ignore(QTWEBKIT)
    // wont fix. Two different ids can reference same element
    public void testSameElementLookedUpDifferentWaysUsingFindElementShouldHaveSameHashCode() {

        WebElement body = driver.findElement(By.tagName("QDeclarativeTextInput"));
        WebElement xbody = driver.findElement(By.xpath("//QDeclarativeTextInput"));

        assertEquals(body.hashCode(), xbody.hashCode());
    }

    @Test
    @Ignore(QTWEBKIT)
    // wont fix. Two different ids can reference same element
    public void testSameElementLookedUpDifferentWaysUsingFindElementsShouldHaveSameHashCode() {

        List<WebElement> body = driver.findElements(By.tagName("QDeclarativeTextInput"));
        List<WebElement> xbody = driver.findElements(By.xpath("//QDeclarativeTextInput"));

        assertEquals(body.get(0).hashCode(), xbody.get(0).hashCode());
    }
}
