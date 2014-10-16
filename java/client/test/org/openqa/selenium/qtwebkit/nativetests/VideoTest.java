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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.qtwebkit.Player;
import org.openqa.selenium.remote.QtWebkitAugmenter;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.testing.JUnit4TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class VideoTest extends JUnit4TestBase {
    @Before
    public void setUp() throws Exception {
        driver.get("qtwidget://VideoTestWidget");
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
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }
        player.setState(Player.PlayerState.playing);
        Player.PlayerState state = player.getState();
        assertEquals(Player.PlayerState.playing, state);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }

        player.setState(Player.PlayerState.paused);
        state = player.getState();
        assertEquals(Player.PlayerState.paused, state);

        player.setState(Player.PlayerState.stopped);
        state = player.getState();
        assertEquals(0, player.getCurrentPlayingPosition(), 0.02);
        assertEquals(Player.PlayerState.stopped, state);
    }

    @Test
    public void testRemotePlayerMute() {
        player.setMute(true);
        assertEquals(true, player.isMuted());

        player.setMute(false);
        assertEquals(false, player.isMuted());
    }

    @Test
    public void testRemotePlayerSeek() {
        assertEquals(player.getCurrentPlayingPosition(), 0, 0);
        player.setState(Player.PlayerState.playing);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
        }

        player.setState(Player.PlayerState.paused);
        player.seek(10);
        assertEquals(10, player.getCurrentPlayingPosition(), 0.1);
        player.setState(Player.PlayerState.playing);
    }

    @Test
    public void testRemotePlayerVolume() {
        player.setVolume(0.5);
        assertEquals(0.5, player.getVolume(), 0.02);

        player.setVolume(0);
        assertEquals(0, player.getVolume(), 0.02);

        player.setVolume(1.0);
        assertEquals(1.0, player.getVolume(), 0.02);
    }

    @Test
    public void testRemotePlayerVolumeAndMute() {
        player.setVolume(0.5);
        player.setMute(true);
        player.setMute(false);
        assertEquals(0.5, player.getVolume(), 0.02);
    }

    @Test
    public void testRemotePlayerSpeed() {
        assertEquals(player.getCurrentPlayingPosition(), 0, 0);

        player.setState(Player.PlayerState.playing);
        player.setSpeed(10);
        assertEquals(10, player.getSpeed(), 0.1);
        player.setState(Player.PlayerState.paused);
        player.setState(Player.PlayerState.playing);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
        }

        player.setSpeed(0.1);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
        }

        player.setState(Player.PlayerState.paused);
    }

    public Player getPlayer(RemoteWebElement element) {
        return (Player) (new QtWebkitAugmenter().augment(element));
    }

    private Player player;
    private WebElement element;

}
