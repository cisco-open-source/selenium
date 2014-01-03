package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.qtwebkit.Visualizer;
import org.openqa.selenium.testing.JUnit4TestBase;
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
    //System.out.println(source);

    assertThat("JavaScript removal", source, not(containsString("Script to remove")));

    assertThat("Images assemplying is happening", source, containsString("data:image/jpeg;base64,/9j/4AAQ"));
    assertThat(source, containsString("src=\"data:image/jpeg;base64,/9j/4AAQ"));
    assertThat(source, containsString("style=\"background:url('data:image/jpeg;base64,/9j/4AAQ"));
    assertThat(source, containsString("background:url('data:image/jpeg;base64,/9j/4AAQ"));

    assertThat("Stylesheets assemplying is happening", source, containsString("<style type=\"text/css\">* {"));
    assertThat(source, not(containsString("<link")));

    assertThat("Closing tag persistance", source, containsString("</textarea>"));
  }
}
