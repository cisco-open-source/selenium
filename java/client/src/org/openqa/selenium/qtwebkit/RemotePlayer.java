package org.openqa.selenium.qtwebkit;

import com.google.common.collect.ImmutableMap;

import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.Response;

import java.util.Map;

public class RemotePlayer extends RemoteWebElement implements Player {

  @Override
  public void play() {
    execute(QtWebKitDriverCommand.SET_PLAYER_STATE,
            ImmutableMap.of("id", id, "state", PlayerState.playing.ordinal()));
  }

  @Override
  public void pause() {
    execute(QtWebKitDriverCommand.SET_PLAYER_STATE,
            ImmutableMap.of("id", id, "state", PlayerState.paused.ordinal()));
  }

  @Override
  public void stop() {
    execute(QtWebKitDriverCommand.SET_PLAYER_STATE,
            ImmutableMap.of("id", id, "state", PlayerState.stopped.ordinal()));
  }

  @Override
  public PlayerState getState() {
    Response response = execute(QtWebKitDriverCommand.GET_PLAYER_STATE, ImmutableMap.of("id", id));
    PlayerState state = PlayerState.values()[((Long)response.getValue()).intValue()];
    return state;
  }

  @Override
  public void setVolume(double level) throws IllegalArgumentException{
    if(level > 1 || level < 0){
      throw new IllegalArgumentException("Volume should be between 1 and 0");
    }
    execute(QtWebKitDriverCommand.SET_PLAYER_VOLUME,
            ImmutableMap.of("id", id, "level", level));
  }

  @Override
  public void seek(double position) {
    execute(QtWebKitDriverCommand.SET_CURRENT_PLAYING_POSITION,
            ImmutableMap.of("id", id, "position", position));
  }

  @Override
  public void setState(PlayerState state) {
    execute(QtWebKitDriverCommand.SET_PLAYER_STATE,
            ImmutableMap.of("id", id, "state", state.ordinal()));
  }

  @Override
  public double getVolume() {
    Response response = execute(QtWebKitDriverCommand.GET_PLAYER_VOLUME, ImmutableMap.of("id", id));
    return ((Number)response.getValue()).doubleValue();
  }

  @Override
  public double currentPlayingPosition() {
    Response response = execute(QtWebKitDriverCommand.GET_CURRENT_PLAYING_POSITION, ImmutableMap.of("id", id));
    return ((Number)response.getValue()).doubleValue();
  }

  @Override
  public boolean isMuted(){
    Response response = execute(QtWebKitDriverCommand.GET_PLAYER_MUTE,
            ImmutableMap.of("id", id));
    return (Boolean)response.getValue();
  }

  @Override
  public void setMute(boolean mute){
    execute(QtWebKitDriverCommand.SET_PLAYER_MUTE,
            ImmutableMap.of("id", id, "mute", mute));
  }
}
