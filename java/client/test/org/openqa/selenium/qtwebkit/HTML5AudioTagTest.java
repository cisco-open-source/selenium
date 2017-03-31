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

package org.openqa.selenium.qtwebkit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.QtWebkitAugmenter;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;

public class HTML5AudioTagTest extends JUnit4TestBase {
    @Before
    public void setUp() throws Exception {
        driver.get(pages.html5AudioTest);
    }

    @AfterClass
    public static void cleanUpDriver() {
        JUnit4TestBase.removeDriver();
    }

    @Test
    public void testRemotePlayerState() {
        WebElement element = driver.findElement(By.id("audioPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
        player.setState(Player.PlayerState.playing);
        Player.PlayerState state = player.getState();
        assertEquals(Player.PlayerState.playing, state);
        assertEquals(null, element.getAttribute("paused"));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
        }

        player.setState(Player.PlayerState.paused);
        state = player.getState();
        assertEquals(Player.PlayerState.paused, state);
        assertEquals("true", element.getAttribute("paused"));

      // TODO HTML5 Audio is not seekable in Qt5.2.0. Issue MHA-905. Check it for next version.
//        player.setState(Player.PlayerState.stopped);
//        state = player.getState();
//        assertEquals("true", element.getAttribute("paused"));
//        assertEquals(Player.PlayerState.stopped, state);
    }

    @Test
    @Ignore(value = {QTWEBKIT}, reason  = "Segmentation fault GStreamer; bug 997153, https://bugzilla.redhat.com/show_bug.cgi?id=918553")
    public void testRemotePlayerSetMute() {
        WebElement element = driver.findElement(By.id("audioPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
        player.setMute(true);
        assertEquals("true", element.getAttribute("muted"));

        player.setMute(false);
        assertEquals(null, element.getAttribute("muted"));
    }

    @Test
    @Ignore(value = {QTWEBKIT}, reason  = "Segmentation fault GStreamer; bug 997153, https://bugzilla.redhat.com/show_bug.cgi?id=918553")
    public void testRemotePlayerGetMute() {
        WebElement element = driver.findElement(By.id("audioPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
        assertEquals(player.isMuted() ? "true" : null, element.getAttribute("muted"));
        player.setMute(true);
        assertEquals(player.isMuted() ? "true" : null, element.getAttribute("muted"));
    }

    @Test
    @Ignore(value = {QTWEBKIT}, reason = "HTML5 Audio isn't seekable: bug 38464, https://bugs.webkit.org/show_bug.cgi?id=38464; bug 37267, https://bugreports.qt-project.org/browse/QTBUG-37267#comment-234581")
    public void testRemotePlayerSeek() {
        WebElement element = driver.findElement(By.id("audioPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
        assertEquals(player.getCurrentPlayingPosition(), 0, 0.1);

        player.setState(Player.PlayerState.playing);
        assertNotEquals(player.getCurrentPlayingPosition(), 0);
    }

    @Test
    public void testRemotePlayerVolume() {
        WebElement element = driver.findElement(By.id("audioPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
        player.setVolume(0.5);
        assertEquals(0.5, player.getVolume(), 0);
        assertEquals("0.5", element.getAttribute("volume"));

        player.setVolume(0);
        assertEquals(0, player.getVolume(), 0);
        assertEquals("0", element.getAttribute("volume"));

        player.setVolume(1.0);
        assertEquals(1.0, player.getVolume(), 0);
        assertEquals("1", element.getAttribute("volume"));
    }

    @Test
    @Ignore(value = {QTWEBKIT}, reason  = "Segmentation fault GStreamer; bug 997153, https://bugzilla.redhat.com/show_bug.cgi?id=918553")
    public void testRemotePlayerVolumeAndMute() {
        WebElement element = driver.findElement(By.id("audioPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
        player.setVolume(0.5);
        player.setMute(true);
        player.setMute(false);
        assertEquals(0.5, player.getVolume(), 0.02);
        assertEquals("0.5", element.getAttribute("volume"));
    }

    public Player getPlayer(RemoteWebElement element) {
        return (Player) (new QtWebkitAugmenter().augment(element));
    }

}
