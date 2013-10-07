package org.openqa.selenium.qtwebkit.nativetests;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.qtwebkit.Player;
import org.openqa.selenium.qtwebkit.RemotePlayer;
import org.openqa.selenium.testing.JUnit4TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class VideoTest extends JUnit4TestBase {
  @Before
  public void setUp() throws Exception {
    driver.get("qtwidget://VideoTestWidget");
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
      try{
        Thread.sleep(2000);
      } catch (InterruptedException ex){}
      player.setState(Player.PlayerState.playing);
      Player.PlayerState state = player.getState();
      assertEquals(Player.PlayerState.playing, state);

      try{
        Thread.sleep(2000);
      } catch (InterruptedException ex){}

      player.setState(Player.PlayerState.paused);
      state = player.getState();
      assertEquals(Player.PlayerState.paused, state);

      player.setState(Player.PlayerState.stopped);
      state = player.getState();
      assertEquals(0, player.getCurrentPlayingPosition(), 0.02);
      assertEquals(Player.PlayerState.stopped, state);
    }
  }

  @Test
  public void testRemotePlayerMute(){
    WebElement element = driver.findElement(By.id("videoPlayer"));
    if(element instanceof RemotePlayer){
      RemotePlayer player = (RemotePlayer)element;
      player.setMute(true);
      assertEquals(true, player.isMuted());

      player.setMute(false);
      assertEquals(false, player.isMuted());
    }

  }

  @Test
  public void testRemotePlayerSeek(){
    WebElement element = driver.findElement(By.id("videoPlayer"));
    if(element instanceof RemotePlayer){
      RemotePlayer player = (RemotePlayer)element;
      assertEquals(player.getCurrentPlayingPosition(), 0, 0);
      player.setState(Player.PlayerState.playing);

      try{
        Thread.sleep(20000);
      }
      catch (InterruptedException ex){}

      player.setState(Player.PlayerState.paused);
      player.seek(10.5);
      assertEquals(10.5, player.getCurrentPlayingPosition(), 0.1);
      player.setState(Player.PlayerState.playing);
    }
  }

  @Test
  public void testRemotePlayerVolume(){
    WebElement element = driver.findElement(By.id("videoPlayer"));
    if(element instanceof  RemotePlayer){
      RemotePlayer player = (RemotePlayer)element;
      player.setVolume(0.5);
      assertEquals(0.5 ,player.getVolume(), 0.02);

      player.setVolume(0);
      assertEquals(0 ,player.getVolume(), 0.02);

      player.setVolume(1.0);
      assertEquals(1.0 ,player.getVolume(), 0.02);
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
    }
  }

  @Test
  public void testRemotePlayerSpeed(){
    WebElement element = driver.findElement(By.id("videoPlayer"));
    if(element instanceof RemotePlayer){
      RemotePlayer player = (RemotePlayer)element;
      assertEquals(player.getCurrentPlayingPosition(), 0, 0);

      player.setState(Player.PlayerState.playing);
      player.setSpeed(10);
      assertEquals(10, player.getSpeed(), 0.1);
      player.setState(Player.PlayerState.paused);
      player.setState(Player.PlayerState.playing);

      try{
        Thread.sleep(5000);
      }
      catch (InterruptedException ex){}

      player.setSpeed(0.1);

      try{
        Thread.sleep(10000);
      }
      catch (InterruptedException ex){}

      player.setState(Player.PlayerState.paused);
    }
  }

}
