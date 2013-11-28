package org.openqa.selenium.qtwebkit;

import com.google.common.collect.ImmutableMap;

import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.HttpVerb;
import org.openqa.selenium.remote.Response;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static org.openqa.selenium.qtwebkit.QtWebKitDriverCommand.*;

public class QtWebDriverExecutor extends HttpCommandExecutor {

    private static final Map<String, CommandInfo> additionalCommands;
    static {
        ImmutableMap.Builder<String, CommandInfo> builder = ImmutableMap.builder();
        builder.put(GET_PLAYER_STATE,
                  new CommandInfo("/session/:sessionId/element/:id/-cisco-player-element/state", HttpVerb.GET))
        .put(SET_PLAYER_STATE, new CommandInfo("/session/:sessionId/element/:id/-cisco-player-element/state", HttpVerb.POST))
        .put(GET_PLAYER_VOLUME, new CommandInfo("/session/:sessionId/element/:id/-cisco-player-element/volume", HttpVerb.GET))
        .put(SET_PLAYER_VOLUME, new CommandInfo("/session/:sessionId/element/:id/-cisco-player-element/volume", HttpVerb.POST))
        .put(GET_CURRENT_PLAYING_POSITION,
             new CommandInfo("/session/:sessionId/element/:id/-cisco-player-element/seek", HttpVerb.GET))
        .put(SET_CURRENT_PLAYING_POSITION,
             new CommandInfo("/session/:sessionId/element/:id/-cisco-player-element/seek", HttpVerb.POST))
        .put(SET_PLAYER_MUTE, new CommandInfo("/session/:sessionId/element/:id/-cisco-player-element/mute", HttpVerb.POST))
        .put(GET_PLAYER_MUTE, new CommandInfo("/session/:sessionId/element/:id/-cisco-player-element/mute", HttpVerb.GET))
        .put(SET_PLAYBACK_SPEED, new CommandInfo("/session/:sessionId/element/:id/-cisco-player-element/speed", HttpVerb.POST))
        .put(GET_PLAYBACK_SPEED, new CommandInfo("/session/:sessionId/element/:id/-cisco-player-element/speed", HttpVerb.GET))
        .put(TOUCH_PINCH_ZOOM, new CommandInfo("/session/:sessionId/touch/-cisco-pinch-zoom", HttpVerb.POST))
        .put(TOUCH_PINCH_ROTATE, new CommandInfo("/session/:sessionId/touch/-cisco-pinch-rotate", HttpVerb.POST))
        .put(GET_VISUALIZER_SOURCE, new CommandInfo("/session/:sessionId/-cisco-visualizer-source", HttpVerb.GET))
        .put(GET_VISUALIZER_SHOW_POINT, new CommandInfo("/session/:sessionId/-cisco-visualizer-show-point", HttpVerb.GET));

        additionalCommands = builder.build();
    }

    public QtWebDriverExecutor(URL addressOfRemoteServer)
    {
        super(additionalCommands, addressOfRemoteServer);
    }

    public Response execute(Command command) throws IOException {
        Response response = super.execute(command);
        return response;
    }

}
