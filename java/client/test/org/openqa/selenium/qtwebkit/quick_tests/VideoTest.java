package org.openqa.selenium.qtwebkit.quick_tests;

import org.junit.AfterClass;
import org.openqa.selenium.*;
import org.openqa.selenium.qtwebkit.Player;
import org.openqa.selenium.qtwebkit.RemotePlayer;
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
  public void setUp()
  {
    driver.get(pages.videoTest);
  }

  @AfterClass
  public static void cleanUpDriver() {
    JUnit4TestBase.removeDriver();
  }

  @Test
  public void testRemotePlayerState(){
    WebElement element = driver.findElement(By.id("videoPlayer"));
    if(element instanceof RemotePlayer){
      RemotePlayer player = (RemotePlayer)element;
      player.setState(Player.PlayerState.playing);
      Player.PlayerState state = player.getState();
      assertEquals(Player.PlayerState.playing, state);
      assertEquals(String.valueOf(state.ordinal()), player.getAttribute("playbackState"));

      try{
        Thread.sleep(5000);
      } catch (InterruptedException ex){}

      player.setState(Player.PlayerState.paused);
      state = player.getState();
      assertEquals(Player.PlayerState.paused, state);
      assertEquals(String.valueOf(state.ordinal()), player.getAttribute("playbackState"));

      player.setState(Player.PlayerState.stopped);
      state = player.getState();
      assertEquals(String.valueOf(state.ordinal()), player.getAttribute("playbackState"));
      assertEquals(0, player.getCurrentPlayingPosition(), 0.02);
      assertEquals(Player.PlayerState.stopped, state);
    }
  }

  @Test
  public void testRemotePlayerSetMute(){
    WebElement element = driver.findElement(By.id("videoPlayer"));
    if(element instanceof RemotePlayer){
      RemotePlayer player = (RemotePlayer)element;
      player.setMute(true);
      assertEquals("true", player.getAttribute("muted"));

      player.setMute(false);
      assertEquals("false", player.getAttribute("muted"));
    }

  }

  @Test
  public void testRemotePlayerGetMute(){
    WebElement element = driver.findElement(By.id("videoPlayer"));
    if(element instanceof RemotePlayer){
      RemotePlayer player = (RemotePlayer)element;
      assertEquals(player.isMuted() ? "true" : "false" , player.getAttribute("muted"));
      player.setMute(true);
      assertEquals(player.isMuted() ? "true" : "false" , player.getAttribute("muted"));
    }
  }

  @Test
  public void testRemotePlayerSeek(){
    WebElement element = driver.findElement(By.id("videoPlayer"));
    if(element instanceof RemotePlayer){
      RemotePlayer player = (RemotePlayer)element;
      assertEquals(0, player.getCurrentPlayingPosition(), 0.2);

      player.setState(Player.PlayerState.playing);
      assertNotEquals(player.getCurrentPlayingPosition(), 0);
    }
  }

  @Test
  public void testRemotePlayerVolume(){
    WebElement element = driver.findElement(By.id("videoPlayer"));
    if(element instanceof  RemotePlayer){
      RemotePlayer player = (RemotePlayer)element;
      player.setVolume(0.5);
      player.setState(Player.PlayerState.playing);
      assertEquals(0.5, player.getVolume(), 0.02);
      assertEquals("0.5", player.getAttribute("volume"));

      player.setVolume(0);
      assertEquals(0 ,player.getVolume(), 0.02);
      assertEquals("0", player.getAttribute("volume"));

      player.setVolume(1.0);
      assertEquals(1.0 ,player.getVolume(), 0.02);
      assertEquals("1", player.getAttribute("volume"));
    }
  }

  @Test
  public void testRemotePlayerVolumeAndMute(){
    WebElement element = driver.findElement(By.id("videoPlayer"));
    if(element instanceof  RemotePlayer){
      RemotePlayer player = (RemotePlayer)element;
      player.setVolume(0.5);
      player.setMute(true);
      player.setMute(false);
      assertEquals(0.5 ,player.getVolume(), 0.02);
      assertEquals("0.5", player.getAttribute("volume"));
    }
  }


}
