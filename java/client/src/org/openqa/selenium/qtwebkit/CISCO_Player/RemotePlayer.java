package org.openqa.selenium.qtwebkit.CISCO_Player;

import com.google.common.collect.ImmutableMap;

import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.Response;

import java.util.Map;

public class RemotePlayer extends RemoteWebElement implements Player {

  @Override
  public void play() {
    execute(DriverCommand.SET_PLAYER_STATE,
            ImmutableMap.of("id", id, "state", PlayerState.playing.ordinal()));
  }

  @Override
  public void pause() {
    execute(DriverCommand.SET_PLAYER_STATE,
            ImmutableMap.of("id", id, "state", PlayerState.paused.ordinal()));
  }

  @Override
  public void stop() {
    execute(DriverCommand.SET_PLAYER_STATE,
            ImmutableMap.of("id", id, "state", PlayerState.stopped.ordinal()));
  }

  @Override
  public PlayerState getState() {
    Response response = execute(DriverCommand.GET_PLAYER_STATE, ImmutableMap.of("id", id));
    Map<String, Object> parameters = (Map<String, Object>) response.getValue();
    PlayerState state = PlayerState.values()[((Number)parameters.get("state")).intValue()];
    return state;
  }

  @Override
  public void setVolume(int level) {
    execute(DriverCommand.SET_PLAYER_VOLUME,
            ImmutableMap.of("id", id, "level", level));
  }

  @Override
  public void seek(double position) {
    execute(DriverCommand.SET_CURRENT_PLAYING_POSITION,
            ImmutableMap.of("id", id, "position", position));
  }

  @Override
  public void setState(PlayerState state) {
    execute(DriverCommand.SET_PLAYER_STATE,
            ImmutableMap.of("id", id, "state", state.ordinal()));
  }

  @Override
  public int getVolume() {
    Response response = execute(DriverCommand.GET_PLAYER_VOLUME, ImmutableMap.of("id", id));
    Map<String, Object> parameters = (Map<String, Object>) response.getValue();
    int volume = ((Number)parameters.get("level")).intValue();
    return volume;
  }

  @Override
  public double currentPlayingPosition() {
    Response response = execute(DriverCommand.GET_CURRENT_PLAYING_POSITION, ImmutableMap.of("id", id));
    Map<String, Object> parameters = (Map<String, Object>) response.getValue();
    double position = ((Number)parameters.get("position")).intValue();
    return position;
  }
}
