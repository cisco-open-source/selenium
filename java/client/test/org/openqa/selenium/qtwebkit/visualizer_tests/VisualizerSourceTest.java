package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.qtwebkit.Visualizer;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class VisualizerSourceTest extends JUnit4TestBase {
  @Before
  public void setUp() throws Exception {
    driver.get(pages.visualizerPage);
    try {
        Thread.sleep(500);
    } catch (InterruptedException ex) {
    }
  }

  @AfterClass
  public static void cleanUpDriver() {
    JUnit4TestBase.removeDriver();
  }

  @Test
  public void testSource(){
    String source = ((Visualizer)driver).getVisualizerSource();
    assertNotNull(source);

    try {
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().
          parse(new InputSource(new StringReader(source)));
      assertNotNull(doc);
    } catch (SAXException e) {
      e.printStackTrace();
      fail(e.getMessage());
    } catch (IOException e) {
      e.printStackTrace();
      fail(e.getMessage());
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }

    assertThat("JavaScript removal", source, not(containsString("Script to remove")));

    assertThat("Images assemplying is happening", source, containsString("data:image/jpeg;base64,/9j/4AAQ"));
    assertThat(source, containsString("src=\"data:image/jpeg;base64,/9j/4AAQ"));
    assertThat(source, containsString("style=\"background:url('data:image/jpeg;base64,/9j/4AAQ"));
    assertThat(source, containsString("background:url('data:image/jpeg;base64,/9j/4AAQ"));

    assertThat("Stylesheets assemplying is happening", source, containsString("<style type=\"text/css\">* {"));

    assertThat("Closing tag persistance", source, containsString("</textarea>"));
  }
}
