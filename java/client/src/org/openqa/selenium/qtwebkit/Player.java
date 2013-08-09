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
  void setVolume(int level);
  void seek(double position);
  PlayerState getState();
  void setState(PlayerState state);
  int getVolume();
  double currentPlayingPosition();
}
