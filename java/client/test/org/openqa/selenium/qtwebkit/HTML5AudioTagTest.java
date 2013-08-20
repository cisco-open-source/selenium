package org.openqa.selenium.qtwebkit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.testing.JUnit4TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HTML5AudioTagTest extends JUnit4TestBase {
  @Before
  public void setUp() throws Exception {
    driver.get(pages.html5AudioTest);
    try {Thread.sleep(3000);} catch (InterruptedException ex){}
  }

  @Test
  public void testRemotePlayerState(){
    WebElement element = driver.findElement(By.id("audioPlayer"));
    if(element instanceof RemotePlayer){
      RemotePlayer player = (RemotePlayer)element;
      player.setState(Player.PlayerState.playing);
      Player.PlayerState state = player.getState();
      assertEquals(Player.PlayerState.playing, state);
      assertEquals(null, player.getAttribute("paused"));

      try{
        Thread.sleep(5000);
      } catch (InterruptedException ex){}

      player.setState(Player.PlayerState.paused);
      state = player.getState();
      assertEquals(Player.PlayerState.paused, state);
      assertEquals("true", player.getAttribute("paused"));

      player.setState(Player.PlayerState.stopped);
      state = player.getState();
      assertEquals("true", player.getAttribute("paused"));
      assertEquals(0, player.currentPlayingPosition(), 0.01);
      assertEquals(Player.PlayerState.stopped, state);
      }
    }

    @Test
  public void testRemotePlayerSetMute(){
    WebElement element = driver.findElement(By.id("audioPlayer"));
    if(element instanceof RemotePlayer){
      RemotePlayer player = (RemotePlayer)element;
      player.setMute(true);
      assertEquals("true", player.getAttribute("muted"));

      player.setMute(false);
      assertEquals(null, player.getAttribute("muted"));
    }

  }

  @Test
  public void testRemotePlayerGetMute(){
    WebElement element = driver.findElement(By.id("audioPlayer"));
    if(element instanceof RemotePlayer){
      RemotePlayer player = (RemotePlayer)element;
      assertEquals(player.isMuted() ? "true" : null , player.getAttribute("muted"));
      player.setMute(true);
      assertEquals(player.isMuted() ? "true" : null , player.getAttribute("muted"));
    }
  }

  @Test
  public void testRemotePlayerSeek(){
    WebElement element = driver.findElement(By.id("audioPlayer"));
    if(element instanceof RemotePlayer){
      RemotePlayer player = (RemotePlayer)element;
      assertEquals(player.currentPlayingPosition(), 0, 0.1);

      player.setState(Player.PlayerState.playing);
      assertNotEquals(player.currentPlayingPosition(), 0);
    }
  }

}
