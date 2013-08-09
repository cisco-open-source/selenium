package org.openqa.selenium.qtwebkit;

import org.openqa.selenium.remote.DriverCommand;

public interface QtWebKitDriverCommand extends DriverCommand {
  //CISCO Player API
  String GET_PLAYER_STATE = "getPlayerState";
  String SET_PLAYER_STATE = "setPlayerState";
  String GET_PLAYER_VOLUME = "getPlayerVolume";
  String SET_PLAYER_VOLUME = "setPlayerVolume";
  String GET_CURRENT_PLAYING_POSITION = "getCurrentPlayingPosition";
  String SET_CURRENT_PLAYING_POSITION = "setCurrentPlayingPosition";
}
