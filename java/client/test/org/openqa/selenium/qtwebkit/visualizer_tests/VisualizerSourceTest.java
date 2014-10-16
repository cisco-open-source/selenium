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
