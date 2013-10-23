/*
Copyright 2010 Selenium committers

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */


package org.openqa.selenium.remote;

import com.google.common.collect.ImmutableMap;

import org.openqa.selenium.qtwebkit.Player;
import org.openqa.selenium.qtwebkit.QtWebKitDriverCommand;

import java.lang.reflect.Method;

public class AddRemotePlayerApi implements AugmenterProvider {
    public Class<?> getDescribedInterface() {
        return Player.class;
    }

    private class PlayerInterfaceImplementation implements InterfaceImplementation, Player{

        private ExecuteMethod driverExecuteMethod;
        private Object id;

        @Override
        public Object invoke(ExecuteMethod executeMethod, Object self, Method method, Object... args) {
            id = ((RemoteWebElement) self).getId();
            driverExecuteMethod = executeMethod;
            Method[] methods = this.getClass().getMethods();
            for(Method tmpMethod: methods){
                if(tmpMethod.getName() == method.getName()){
                    try{
                        return tmpMethod.invoke(this, args);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        public void play() {
            driverExecuteMethod.execute(QtWebKitDriverCommand.SET_PLAYER_STATE,
                    ImmutableMap.of("id", id, "state", PlayerState.playing.ordinal()));
        }

        @Override
        public void pause() {
            driverExecuteMethod.execute(QtWebKitDriverCommand.SET_PLAYER_STATE,
                    ImmutableMap.of("id", id, "state", PlayerState.paused.ordinal()));
        }

        @Override
        public void stop() {
            driverExecuteMethod.execute(QtWebKitDriverCommand.SET_PLAYER_STATE,
                    ImmutableMap.of("id", id, "state", PlayerState.stopped.ordinal()));
        }

        @Override
        public PlayerState getState() {
            Number response = (Number)driverExecuteMethod
                    .execute(QtWebKitDriverCommand.GET_PLAYER_STATE, ImmutableMap.of("id", id));
            PlayerState state = PlayerState.values()[((Long)response).intValue()];
            return state;
        }

        @Override
        public void setVolume(double volume) throws IllegalArgumentException{
            if(volume > 1 || volume < 0){
                throw new IllegalArgumentException("Volume should be between 1 and 0");
            }
            driverExecuteMethod.execute(QtWebKitDriverCommand.SET_PLAYER_VOLUME,
                    ImmutableMap.of("id", id, "volume", volume));
        }

        @Override
        public void seek(double position) {
            driverExecuteMethod.execute(QtWebKitDriverCommand.SET_CURRENT_PLAYING_POSITION,
                    ImmutableMap.of("id", id, "position", position));
        }

        @Override
        public void setState(PlayerState state) {
            driverExecuteMethod.execute(QtWebKitDriverCommand.SET_PLAYER_STATE,
                    ImmutableMap.of("id", id, "state", state.ordinal()));
        }

        @Override
        public double getVolume() {
            return  ((Number)driverExecuteMethod.
                    execute(QtWebKitDriverCommand.GET_PLAYER_VOLUME, ImmutableMap.of("id", id)))
            .doubleValue();
        }

        @Override
        public double getCurrentPlayingPosition() {
            return  ((Number)driverExecuteMethod
                    .execute(QtWebKitDriverCommand.GET_CURRENT_PLAYING_POSITION, ImmutableMap.of("id", id)))
                    .doubleValue();
        }

        @Override
        public boolean isMuted(){
            return  (Boolean)driverExecuteMethod
                    .execute(QtWebKitDriverCommand.GET_PLAYER_MUTE, ImmutableMap.of("id", id));
        }

        @Override
        public void setMute(boolean mute){
            driverExecuteMethod.execute(QtWebKitDriverCommand.SET_PLAYER_MUTE,
                    ImmutableMap.of("id", id, "mute", mute));
        }

        @Override
        public double getSpeed() {
            return ((Number)driverExecuteMethod
                    .execute(QtWebKitDriverCommand.GET_PLAYBACK_SPEED, ImmutableMap.of("id", id)))
                    .doubleValue();
        }

        @Override
        public void setSpeed(double speed) {
            driverExecuteMethod.execute(QtWebKitDriverCommand.SET_PLAYBACK_SPEED,
                    ImmutableMap.of("id", id, "speed", speed));
        }

    }

    public InterfaceImplementation getImplementation(Object value) {
        return new PlayerInterfaceImplementation();
    }
}
