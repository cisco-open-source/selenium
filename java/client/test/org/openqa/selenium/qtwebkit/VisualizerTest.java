package org.openqa.selenium.qtwebkit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.qtwebkit.Visualizer;
import org.openqa.selenium.testing.JUnit4TestBase;

import static org.junit.Assert.*;

public class VisualizerTest extends JUnit4TestBase {
  @Before
  public void setUp() throws Exception {
  }

  @AfterClass
  public static void cleanUpDriver() {
    JUnit4TestBase.removeDriver();
  }

  @Test
  public void testRemotePlayerState(){
    driver.get("http://www.google.com");
    String source = ((Visualizer)driver).getVisualizerSource();
    assertNotNull(source);
  }
}
