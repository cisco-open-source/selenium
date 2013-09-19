package org.openqa.selenium.qtwebkit;

import org.openqa.selenium.WebElement;

/**
 * Interface to use for testing different players - HTML5 video/audio players,
 * Widgets QVideoWidget and QML video tag.
 *
 */
public interface Player extends WebElement{


  /**
   * @enum PlayerState used for representing players state,
   */
  public enum PlayerState {
    stopped,
    playing,
    paused;
  }
  /**
   * Start playing player's media content.
   * If it's already playing nothing will be done.
   *
   */
  void play();
  /**
   * Pause player's media content.
   * If it's already paused/stopped nothing will be done.
   *
   */
  void pause();
  /**
   * Stop player's media content(pause + seek to start).
   * If it's already paused/stopped nothing will be done.
   *
   */
  void stop();
  /**
   * Sets player's volume
   * @param volume player's volume in range from 0 to 1.0.
   *
   */
  void setVolume(double volume);
  /**
   * Sets player's volume
   * @param position player's current playing time, to set. Parameter is
   *                 in range from 0 to video length, where integral part
   *                 is seconds.
   *
   */
  void seek(double position);
  /**
   * Gets player's state
   * @return {@link PlayerState}
   *
   */
  PlayerState getState();
  /**
   * Sets player's state
   * @param state {@link PlayerState}
   *
   */
  void setState(PlayerState state);
  /**
   * Gets player's volume
   * @return player's volume in range from 0 to 1.0.
   *
   */
  double getVolume();
  /**
   * Gets player's current playing position
   * @return player's current playing position. Position is
   *                 in range from 0 to video length, where integral part
   *                 is seconds.
   *
   */
  double getCurrentPlayingPosition();
  /**
   * Gets player's muted status
   * @return either player is muted or not.
   *
   */
  boolean isMuted();
  /**
   * Sets player's muted status
   * @param mute specify either player is muted or not.
   *
   */
  void setMute(boolean mute);
  /**
   * Gets player's playback speed
   * @return current playing speed
   *
   */
  double getSpeed();
  /**
   * Sets player's playback speed
   * @param speed playing speed to set
   *
   */
  void setSpeed(double speed);
}
