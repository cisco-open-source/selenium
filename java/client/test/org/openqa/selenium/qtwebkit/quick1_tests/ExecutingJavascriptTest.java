/*
Copyright 2007-2009 Selenium committers

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

package org.openqa.selenium.qtwebkit.quick1_tests;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.InProject;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.testing.JavascriptEnabled;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.openqa.selenium.testing.Ignore.Driver.*;

public class ExecutingJavascriptTest extends JUnit4TestBase {

    @Before
    public void setUp() throws Exception {
        driver.get(appServer.whereIs("quick1/JavascriptTest.qml"));
    }

    @JavascriptEnabled
    @Test
    public void testShouldBeAbleToExecuteSimpleJavascriptAndReturnAString() {
        if (!(driver instanceof JavascriptExecutor)) {
            return;
        }

        Object result = executeScript("return title;");

        assertTrue(result instanceof String);
        assertEquals("Javascript Test Page", result);
    }

    @JavascriptEnabled
    @Test
    public void testShouldBeAbleToExecuteSimpleJavascriptAndReturnALong() {
        if (!(driver instanceof JavascriptExecutor)) {
            return;
        }

        Object result = executeScript("return width;");

        assertTrue(result.getClass().getName(), result instanceof Long);
        assertTrue((Long) result > 1);
    }

    @JavascriptEnabled
    @Test
    public void testShouldBeAbleToExecuteSimpleJavascriptAndReturnABoolean() {
        if (!(driver instanceof JavascriptExecutor)) {
            return;
        }

        Object result = executeScript("return true;");

        assertNotNull(result);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @JavascriptEnabled
    @Test
    public void testPassingAndReturningALongShouldReturnAWholeNumber() {
        if (!(driver instanceof JavascriptExecutor)) {
            return;
        }

        Long expectedResult = 1L;
        Object result = executeScript("return arguments[0];", expectedResult);
        assertTrue("Expected result to be an Integer or Long but was a " +
                   result.getClass(), result instanceof Integer || result instanceof Long);
        assertEquals(expectedResult.longValue(), result);
    }

    @JavascriptEnabled
    @Test
    public void testPassingAndReturningADoubleShouldReturnADecimal() {
        if (!(driver instanceof JavascriptExecutor)) {
            return;
        }

        Double expectedResult = 1.2;
        Object result = executeScript("return arguments[0];", expectedResult);
        assertTrue("Expected result to be a Double or Float but was a " +
                   result.getClass(), result instanceof Float || result instanceof Double);
        assertEquals(expectedResult.doubleValue(), result);
    }

    @JavascriptEnabled
    @Test
    public void testShouldThrowAnExceptionWhenTheJavascriptIsBad() {
        if (!(driver instanceof JavascriptExecutor)) {
            return;
        }

        try {
            executeScript("return squiggle();");
            fail("Expected an exception");
        } catch (WebDriverException e) {
            // This is expected
            assertFalse(e.getMessage(), e.getMessage().startsWith("null "));
        }
    }

    @JavascriptEnabled
    @Test
    public void testShouldBeAbleToCallFunctionsDefinedOnThePage() {
        if (!(driver instanceof JavascriptExecutor)) {
            return;
        }

        Object result = executeScript("return sayHello('Selenium');");

        assertTrue(result instanceof String);
        assertEquals("Hello Selenium !", result);
    }

    private Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    @JavascriptEnabled
    @Test
    public void testShouldBeAbleToPassAStringAnAsArgument() {
        if (!(driver instanceof JavascriptExecutor)) {
            return;
        }

        String value =
            (String) executeScript("return arguments[0] == 'fish' ? 'fish' : 'not fish';", "fish");

        assertEquals("fish", value);
    }

    @JavascriptEnabled
    @Test
    public void testShouldBeAbleToPassABooleanAsArgument() {
        if (!(driver instanceof JavascriptExecutor)) {
            return;
        }

        boolean value = (Boolean) executeScript("return arguments[0] == true;", true);

        assertTrue(value);
    }

    @JavascriptEnabled
    @Test
    public void testShouldBeAbleToPassANumberAnAsArgument() {
        if (!(driver instanceof JavascriptExecutor)) {
            return;
        }

        boolean value = (Boolean) executeScript("return arguments[0] == 1 ? true : false;", 1);

        assertTrue(value);
    }

    @JavascriptEnabled
    @Test
    public void testPassingArrayAsOnlyArgumentFlattensArray() {
        if (!(driver instanceof JavascriptExecutor)) {
            return;
        }

        Object[] array = new Object[]{"zero", 1, true, 3.14159, false};
        String value = (String) executeScript("return arguments[0]", array);
        assertEquals(array[0], value);
    }

    @JavascriptEnabled
    @Test
    public void testShouldBeAbleToPassAnArrayAsAdditionalArgument() {
        if (!(driver instanceof JavascriptExecutor)) {
            return;
        }

        Object[] array = new Object[]{"zero", 1, true, 3.14159, false};
        long length = (Long) executeScript("return arguments[1].length", "string", array);
        assertEquals(array.length, length);
    }

    @JavascriptEnabled
    @Test
    public void testShouldBeAbleToPassACollectionAsArgument() {
        if (!(driver instanceof JavascriptExecutor)) {
            return;
        }

        Collection<Object> collection = new ArrayList<Object>();
        collection.add("Cheddar");
        collection.add("Brie");
        collection.add(7);
        long length = (Long) executeScript("return arguments[0].length", collection);
        assertEquals(collection.size(), length);

        collection = new HashSet<Object>();
        collection.add("Gouda");
        collection.add("Stilton");
        collection.add("Stilton");
        collection.add(true);
        length = (Long) executeScript("return arguments[0].length", collection);
        assertEquals(collection.size(), length);
    }

    @JavascriptEnabled
    @Test
    public void testShouldThrowAnExceptionIfAnArgumentIsNotValid() {
        if (!(driver instanceof JavascriptExecutor)) {
            return;
        }

        try {
            executeScript("return arguments[0];", driver);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // this is expected
        }
    }

    @JavascriptEnabled
    @Test
    public void testShouldBeAbleToPassInMoreThanOneArgument() {
        if (!(driver instanceof JavascriptExecutor)) {
            return;
        }

        String result = (String) executeScript("return arguments[0] + arguments[1];", "one", "two");

        assertEquals("onetwo", result);
    }

    @JavascriptEnabled
    @Test
    public void testJavascriptStringHandlingShouldWorkAsExpected() {

        String value = (String) executeScript("return '';");
        assertEquals("", value);

        value = (String) executeScript("return ' '");
        assertEquals(" ", value);
    }

    @JavascriptEnabled
    @Test
    public void testCanPassAMapAsAParameter() {

        List<Integer> nums = ImmutableList.of(1, 2);
        Map<String, Object> args = ImmutableMap.of("bar", "test", "foo", nums);

        Object res = ((JavascriptExecutor) driver).executeScript("return arguments[0]['foo'][1]", args);

        assertEquals(2, ((Number) res).intValue());
    }

}
