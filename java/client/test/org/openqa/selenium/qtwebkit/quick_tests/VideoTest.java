package org.openqa.selenium.qtwebkit.quick_tests;

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
import static org.openqa.selenium.TestWaiter.waitFor;

public class VideoTest extends JUnit4TestBase {

    @Before
    public void setUp() {
        driver.get(pages.videoTest);
    }

    @AfterClass
    public static void cleanUpDriver() {
        JUnit4TestBase.removeDriver();
    }

    @Test
    public void testRemotePlayerState() {
        WebElement element = driver.findElement(By.id("videoPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
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
        WebElement element = driver.findElement(By.id("videoPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
        player.setMute(true);
        assertEquals("true", element.getAttribute("muted"));

        player.setMute(false);
        assertEquals("false", element.getAttribute("muted"));
    }

    @Test
    public void testRemotePlayerGetMute() {
        WebElement element = driver.findElement(By.id("videoPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
        assertEquals(player.isMuted() ? "true" : "false", element.getAttribute("muted"));
        player.setMute(true);
        assertEquals(player.isMuted() ? "true" : "false", element.getAttribute("muted"));
    }

    @Test
    public void testRemotePlayerSeek() {
        WebElement element = driver.findElement(By.id("videoPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
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
        WebElement element = driver.findElement(By.id("videoPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
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
        WebElement element = driver.findElement(By.id("videoPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
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
        WebElement element = driver.findElement(By.id("videoPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
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
}
