package org.openqa.selenium.qtwebkit;

import org.openqa.selenium.WebElement;


public interface Player extends WebElement {

  public enum PlayerState {
    stopped,
    playing,
    paused;
  }

  void play();
  void pause();
  void stop();
  void setVolume(double volume);
  void seek(double position);
  PlayerState getState();
  void setState(PlayerState state);
  double getVolume();
  double currentPlayingPosition();
  boolean isMuted();
  void setMute(boolean mute);
}
