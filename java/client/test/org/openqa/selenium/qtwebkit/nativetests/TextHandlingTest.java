/****************************************************************************
**
** Copyright © 1992-2014 Cisco and/or its affiliates. All rights reserved.
** All rights reserved.
** 
** $CISCO_BEGIN_LICENSE:APACHE$
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
** http://www.apache.org/licenses/LICENSE-2.0
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
**
** $CISCO_END_LICENSE$
**
****************************************************************************/

package org.openqa.selenium.qtwebkit.nativetests;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.regex.Pattern;

import org.hamcrest.TypeSafeMatcher;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;


public class TextHandlingTest extends JUnit4TestBase {

    private final String newLine = "\n";

    @Before
    public void setUp() throws Exception
    {
        driver.get("qtwidget://TextHandlingTestWidget");
    }

    @Test
    public void testShouldReturnTheTextContentOfASingleElementWithNoChildren() {

        String selectText = driver.findElement(By.id("oneline")).getText();
        assertThat(selectText, equalTo("A single line of text"));

        String getText = driver.findElement(By.id("oneline")).getText();
        assertThat(getText, equalTo("A single line of text"));
    }

    @Test
    public void testShouldReturnTheEntireTextContentOfChildElements() {
        String text = driver.findElement(By.id("multiline")).getText();

        assertThat(text.contains("A div containing"), is(true));
        assertThat(text.contains("More than one line of text"), is(true));
        assertThat(text.contains("and block level elements"), is(true));
    }

    @Test
    public void testShouldIgnoreNonInputElements() {
        try
        {
            WebElement groupBox = driver.findElement(By.id("notext"));
            String text = groupBox.getText();

            System.out.println("#### groupBox text: " + text);

            assertNull(text);
        }
        catch (NoSuchElementException ex)
        {
            // ok with current WD implementation
        }
    }

    @Test
    public void testShouldRepresentABlockLevelElementAsANewline() {
        String text = driver.findElement(By.id("multiline")).getText();

        assertThat(text, startsWith("A div containing" + newLine));
        assertThat(text, containsString("More than one line of text" + newLine));
        assertThat(text, endsWith("and block level elements"));
    }


    @Test
    public void testShouldTrimText() {
        String text = driver.findElement(By.id("multiline")).getText();

        assertThat(text, startsWith("A div containing"));
        assertThat(text, endsWith("block level elements"));
    }

    @Test
    public void testShouldReturnUnvisibleText() {
        String text = driver.findElement(By.id("unvisibletext")).getText();
        assertThat(text, containsString("30"));
    }

    @Test
    public void testShouldReturnTheEntireTextOfInlineElements() {
        String text = driver.findElement(By.id("childtext")).getText();

        assertThat(text, equalTo("child text"));
    }

    @Test
    public void testShouldRetainTheFormatingOfTextWithinAPreElement() {
        String text = driver.findElement(By.id("preformatted")).getText();

        assertThat(text, equalTo(" This section has a preformatted\n" +
                "    text block    \n" +
                "  split in four lines\n" +
                "         "));
    }

    @Test
    public void testShouldBeAbleToSetMoreThanOneLineOfTextInATextArea() {
        WebElement textarea = driver.findElement(By.id("emptytext"));
        textarea.clear();

        wait.until(WaitingConditions.elementTextToEqual(textarea, ""));

        String expectedText = "i like cheese" + newLine + newLine + "it's really nice";

        textarea.sendKeys(expectedText);

        String seenText = textarea.getText();
        assertThat(seenText, equalTo(expectedText));
    }

    @Test
    public void testShouldReturnEmptyStringWhenTextIsOnlySpaces() {

        WebElement spaces = driver.findElement(By.id("lineedit"));
        spaces.sendKeys("    ");

        String text = driver.findElement(By.id("lineedit")).getText();
        assertThat(text, equalTo("    "));
    }

    @Test
    public void testShouldReturnEmptyStringWhenTextIsEmpty() {
        WebElement empty = driver.findElement(By.id("lineedit"));
        empty.clear();

        String text = driver.findElement(By.id("lineedit")).getText();
        assertThat(text, equalTo(""));
    }

    @Test
    public void testShouldHandleSiblingBlockLevelElements() {
        String text = driver.findElement(By.id("childtext")).getText();

        assertThat(text, is("child text"));
    }

    @Test
    public void testReadALargeAmountOfData() {
        String source = driver.getPageSource().trim().toLowerCase();

        assertThat(source.startsWith("<?xml"), is(true));
    }


    private Matcher<String> matchesPattern(String javaRegex) {
        final Pattern pattern = Pattern.compile(javaRegex);

        return new TypeSafeMatcher<String>() {
            @Override
            public boolean matchesSafely(String s) {
                return pattern.matcher(s).matches();
            }

            public void describeTo(Description description) {
                description.appendText("a string matching the pattern " + pattern);
            }
        };
    }

    @Test
    public void testShouldOnlyIncludeVisibleText() {

        String empty = driver.findElement(By.id("unvisible")).getText();
        String explicit = driver.findElement(By.id("oneline")).getText();

        assertEquals("", empty);
        assertEquals("A single line of text", explicit);
    }

    @Test
    public void testShouldGetTextFromListView() {

        WebElement tr = driver.findElement(By.id("listview"));
        String text = tr.getText();

        assertTrue(text.contains("Some text"));
        assertFalse(text.contains("Some text again"));
    }

    @Test
    public void testTextOfATextAreaShouldBeEqualToItsDefaultText() {
        WebElement area = driver.findElement(By.id("oneline"));
        assertEquals("A single line of text", area.getText());
    }
}
