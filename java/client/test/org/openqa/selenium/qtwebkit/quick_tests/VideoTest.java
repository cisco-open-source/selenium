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

package org.openqa.selenium.qtwebkit.quick_tests;

import org.junit.After;
import org.junit.AfterClass;
import org.openqa.selenium.*;
import org.openqa.selenium.qtwebkit.Player;
import org.openqa.selenium.remote.QtWebkitAugmenter;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.testing.Ignore;

import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;

import org.openqa.selenium.testing.JUnit4TestBase;

import org.junit.Test;
import org.junit.Before;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class VideoTest extends JUnit4TestBase {

    @Before
    public void setUp() {
        driver.get(pages.videoTest);
        element = driver.findElement(By.id("videoPlayer"));
        player = getPlayer((RemoteWebElement) element);
    }

    @After
    public void stopPlayer() {
        player.stop();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void cleanUpDriver() {
        JUnit4TestBase.removeDriver();
    }

    @Test
    public void testRemotePlayerState() {
        player.setState(Player.PlayerState.playing);
        Player.PlayerState state = player.getState();
        assertEquals(Player.PlayerState.playing, state);
        assertEquals(String.valueOf(state.ordinal()), element.getAttribute("playbackState"));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
        }

        player.setState(Player.PlayerState.paused);
        state = player.getState();
        assertEquals(Player.PlayerState.paused, state);
        assertEquals(String.valueOf(state.ordinal()), element.getAttribute("playbackState"));

        player.setState(Player.PlayerState.stopped);
        state = player.getState();
        assertEquals(String.valueOf(state.ordinal()), element.getAttribute("playbackState"));
        assertEquals(0, player.getCurrentPlayingPosition(), 0.02);
        assertEquals(Player.PlayerState.stopped, state);
    }

    @Test
    public void testRemotePlayerSetMute() {
        player.setMute(true);
        assertEquals("true", element.getAttribute("muted"));

        player.setMute(false);
        assertEquals("false", element.getAttribute("muted"));

    }

    @Test
    public void testRemotePlayerGetMute() {
        assertEquals(player.isMuted() ? "true" : "false", element.getAttribute("muted"));
        player.setMute(true);
        assertEquals(player.isMuted() ? "true" : "false", element.getAttribute("muted"));

    }

    @Test
    public void testRemotePlayerSeek() {
        assertEquals(player.getCurrentPlayingPosition(), 0, 0);
        player.setState(Player.PlayerState.playing);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
        }
        assertEquals(5, player.getCurrentPlayingPosition(), 1);

        player.seek(1.5);
        assertEquals(1.5, player.getCurrentPlayingPosition(), 0.1);

    }

    @Test
    public void testRemotePlayerVolume() {
        player.setVolume(0.5);
        player.setState(Player.PlayerState.playing);
        assertEquals(0.5, player.getVolume(), 0.02);
        assertEquals("0.5", element.getAttribute("volume"));

        player.setVolume(0);
        assertEquals(0, player.getVolume(), 0.02);
        assertEquals("0", element.getAttribute("volume"));

        player.setVolume(1.0);
        assertEquals(1.0, player.getVolume(), 0.02);
        assertEquals("1", element.getAttribute("volume"));
    }

    @Test
    public void testRemotePlayerVolumeAndMute() {
        player.setVolume(0.5);
        player.setMute(true);
        player.setMute(false);
        assertEquals(0.5, player.getVolume(), 0.02);
        assertEquals("0.5", element.getAttribute("volume"));
    }

    @Test
    @Ignore(value = {QTWEBKIT},
            reason = "Qt bug https://bugreports.qt-project.org/browse/QTBUG-34004",
            issues = 764)
    public void testRemotePlayerSpeed() {
        assertEquals(player.getCurrentPlayingPosition(), 0, 0);

        player.setState(Player.PlayerState.playing);
        player.setSpeed(2);
        assertEquals(2, player.getSpeed(), 0.1);
        player.setState(Player.PlayerState.paused);
        player.setState(Player.PlayerState.playing);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
        }
        assertEquals(10, player.getSpeed(), 0.1);

        player.setSpeed(0.1);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
        }
        assertEquals(0.1, player.getSpeed(), 0.01);

        player.setState(Player.PlayerState.paused);

    }

    public Player getPlayer(RemoteWebElement element) {
        return (Player) (new QtWebkitAugmenter().augment(element));
    }

    private Player player;
    private WebElement element;
}
