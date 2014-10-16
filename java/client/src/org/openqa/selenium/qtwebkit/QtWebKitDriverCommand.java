/****************************************************************************
**
** Copyright © 1992-2014 Cisco and/or its affiliates. All rights reserved.
** All rights reserved.
** 
** $CISCO_BEGIN_LICENSE:APACHE$
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
** http://www.apache.org/licenses/LICENSE-2.0
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
**
** $CISCO_END_LICENSE$
**
****************************************************************************/

package org.openqa.selenium.qtwebkit;

import org.openqa.selenium.remote.DriverCommand;

public interface QtWebKitDriverCommand extends DriverCommand {
  //CISCO Player API
  String GET_PLAYER_STATE = "getPlayerState";
  String SET_PLAYER_STATE = "setPlayerState";
  String GET_PLAYER_VOLUME = "getPlayerVolume";
  String SET_PLAYER_VOLUME = "setPlayerVolume";
  String SET_PLAYER_MUTE = "setPlayerMute";
  String GET_PLAYER_MUTE = "getPlayerMute";
  String GET_CURRENT_PLAYING_POSITION = "getCurrentPlayingPosition";
  String SET_CURRENT_PLAYING_POSITION = "setCurrentPlayingPosition";
  String GET_PLAYBACK_SPEED = "getPlaybackSpeed";
  String SET_PLAYBACK_SPEED = "setPlaybackSpeed";
  String GET_VISUALIZER_SOURCE = "getVisualizerSource";
  String  GET_VISUALIZER_SHOW_POINT = "getVisualizerShowPoint";
}
