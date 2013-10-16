package org.openqa.selenium.qtwebkit.hybridtests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ViewTransitionsTest  extends JUnit4TestBase {

    String[] viewUrls;

    @Before
    public void setUp() {
        String current = "";
        try {
            current = new java.io.File( "." ).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        viewUrls = new String[] {
            "qtwidget://TextHandlingTestWidget",                        // widget
            pages.colorPage,                                            // remote html
            "file://"+current+"/common/src/web/banner.gif",             // local html
            appServer.whereIs("quick1/FindingTest.qml"),                // remote qml
            "file://"+current+"/common/src/web/quick1/FindingTest.qml"  // local qml
        };
    }

    @Test
    public void testViewTransitions() {
        int srcLength = viewUrls.length;
        int firstView = 0;
        int secondView = 1;

        while(firstView < srcLength) {
            driver.get(viewUrls[firstView]);
            assertEquals(viewUrls[firstView], driver.getCurrentUrl());

            driver.get(viewUrls[secondView]);
            assertEquals(viewUrls[secondView], driver.getCurrentUrl());

            // get next pair
            secondView++;
            if (secondView == firstView) secondView++;
            if (secondView >= srcLength) {
                firstView++;
                secondView = 0;
            }
        }
    }

}
