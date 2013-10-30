package org.openqa.selenium.qtwebkit;

import com.google.common.collect.ImmutableMap;

import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.HttpVerb;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.service.DriverCommandExecutor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import static org.openqa.selenium.qtwebkit.QtWebKitDriverCommand.*;

public class QtWebDriverExecutor extends HttpCommandExecutor {

    private static ArrayList<String> executedCommands = new ArrayList<String>();
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
      .put(TOUCH_PINCH_ROTATE, new CommandInfo("/session/:sessionId/touch/-cisco-pinch-rotate", HttpVerb.POST));

    additionalCommands = builder.build();
  }
    private static Object[][] commands =
            {
                    {"GET", STATUS, "/status"},
                    {"POST", NEW_SESSION, "/session"},
                    {"GET", "sessionsList", "/sessions"},
                    {"GET", "getSession", "/session/:sessionId"},
                    {"DELETE", QUIT, "/session/:sessionId"},
                    {"POST", SET_TIMEOUT, "/session/:sessionId/timeouts"},
                    {"POST", SET_SCRIPT_TIMEOUT, "/session/:sessionId/timeouts/async_script"},
                    {"POST", IMPLICITLY_WAIT, "/session/:sessionId/timeouts/implicit_wait"},
                    {"GET", GET_CURRENT_WINDOW_HANDLE, "/session/:sessionId/window_handle"},
                    {"GET", GET_WINDOW_HANDLES, "/session/:sessionId/window_handles"},
                    {"GET", GET_CURRENT_URL, "/session/:sessionId/url"},
                    {"POST", GET, "/session/:sessionId/url"},
                    {"POST", GO_FORWARD, "/session/:sessionId/forward",false},
                    {"POST", GO_BACK, "/session/:sessionId/back"},
                    {"POST", REFRESH, "/session/:sessionId/refresh"},
                    {"POST", EXECUTE_SCRIPT, "/session/:sessionId/execute"},
                    {"POST", EXECUTE_ASYNC_SCRIPT, "/session/:sessionId/execute_async"},
                    {"GET", SCREENSHOT, "/session/:sessionId/screenshot"},
                    {"GET", IME_GET_AVAILABLE_ENGINES, "/session/:sessionId/ime/available_engines"},
                    {"GET", IME_GET_ACTIVE_ENGINE, "/session/:sessionId/ime/active_engine"},
                    {"GET", IME_IS_ACTIVATED, "/session/:sessionId/ime/activated"},
                    {"POST", IME_DEACTIVATE, "/session/:sessionId/ime/deactivate"},
                    {"POST", IME_ACTIVATE_ENGINE, "/session/:sessionId/ime/activate"},
                    {"POST", SWITCH_TO_FRAME, "/session/:sessionId/frame"},
                    {"POST", SWITCH_TO_WINDOW, "/session/:sessionId/window"},
                    {"DELETE", CLOSE, "/session/:sessionId/window"},
                    {"POST", SET_WINDOW_SIZE, "/session/:sessionId/window/:windowHandle/size"},
                    {"GET", GET_WINDOW_SIZE, "/session/:sessionId/window/:windowHandle/size"},
                    {"POST", SET_WINDOW_POSITION, "/session/:sessionId/window/:windowHandle/position"},
                    {"GET", GET_WINDOW_POSITION, "/session/:sessionId/window/:windowHandle/position"},
                    {"POST", MAXIMIZE_WINDOW, "/session/:sessionId/window/:windowHandle/maximize"},
                    {"GET", GET_ALL_COOKIES, "/session/:sessionId/cookie"},
                    {"POST", ADD_COOKIE, "/session/:sessionId/cookie"},
                    {"DELETE", DELETE_ALL_COOKIES, "/session/:sessionId/cookie"},
                    {"DELETE", DELETE_COOKIE, "/session/:sessionId/cookie/:name"},
                    {"GET", GET_PAGE_SOURCE, "/session/:sessionId/source"},
                    {"GET", GET_TITLE, "/session/:sessionId/title"},
                    {"POST", FIND_ELEMENT, "/session/:sessionId/element"},
                    {"POST", FIND_ELEMENTS, "/session/:sessionId/elements"},
                    {"POST", GET_ACTIVE_ELEMENT, "/session/:sessionId/element/active"},
                    {"GET", "getElement", "/session/:sessionId/element/:id"},
                    {"POST", FIND_CHILD_ELEMENT, "/session/:sessionId/element/:id/element"},
                    {"POST", FIND_CHILD_ELEMENTS, "/session/:sessionId/element/:id/elements"},
                    {"POST", CLICK_ELEMENT, "/session/:sessionId/element/:id/click"},
                    {"POST", SUBMIT_ELEMENT, "/session/:sessionId/element/:id/submit"},
                    {"GET", GET_ELEMENT_TEXT, "/session/:sessionId/element/:id/text"},
                    {"POST", SEND_KEYS_TO_ELEMENT, "/session/:sessionId/element/:id/value"},
                    {"POST", SEND_KEYS_TO_ACTIVE_ELEMENT, "/session/:sessionId/keys"},
                    {"GET", GET_ELEMENT_TAG_NAME, "/session/:sessionId/element/:id/name"},
                    {"POST", CLEAR_ELEMENT, "/session/:sessionId/element/:id/clear"},
                    {"GET", IS_ELEMENT_SELECTED, "/session/:sessionId/element/:id/selected"},
                    {"GET", IS_ELEMENT_ENABLED, "/session/:sessionId/element/:id/enabled"},
                    {"GET", GET_ELEMENT_ATTRIBUTE, "/session/:sessionId/element/:id/attribute/:name"},
                    {"GET", ELEMENT_EQUALS, "/session/:sessionId/element/:id/equals/:other"},
                    {"GET", IS_ELEMENT_DISPLAYED, "/session/:sessionId/element/:id/displayed"},
                    {"GET", GET_ELEMENT_LOCATION, "/session/:sessionId/element/:id/location"},
                    {"GET", GET_ELEMENT_LOCATION_ONCE_SCROLLED_INTO_VIEW, "/session/:sessionId/element/:id/location"},
                    {"GET", GET_ELEMENT_SIZE, "/session/:sessionId/element/:id/size"},
                    {"GET", GET_ELEMENT_VALUE_OF_CSS_PROPERTY, "/session/:sessionId/element/:id/css/:propertyName"},
                    {"GET", GET_SCREEN_ORIENTATION, "/session/:sessionId/orientation"},
                    {"POST", SET_SCREEN_ORIENTATION, "/session/:sessionId/orientation"},
                    {"GET", GET_ALERT_TEXT, "/session/:sessionId/alert_text"},
                    {"POST", SET_ALERT_VALUE, "/session/:sessionId/alert_text"},
                    {"POST", ACCEPT_ALERT, "/session/:sessionId/accept_alert"},
                    {"POST", DISMISS_ALERT, "/session/:sessionId/dismiss_alert"},
                    {"POST", MOVE_TO, "/session/:sessionId/moveto"},
                    {"POST", CLICK, "/session/:sessionId/click"},
                    {"POST", MOUSE_DOWN, "/session/:sessionId/buttondown"},
                    {"POST", MOUSE_UP, "/session/:sessionId/buttonup"},
                    {"POST", DOUBLE_CLICK, "/session/:sessionId/doubleclick"},
                    {"POST", TOUCH_SINGLE_TAP, "/session/:sessionId/touch/click"},
                    {"POST", TOUCH_DOWN, "/session/:sessionId/touch/down"},
                    {"POST", TOUCH_UP, "/session/:sessionId/touch/up"},
                    {"POST", TOUCH_MOVE, "/session/:sessionId/touch/move"},
                    {"POST", TOUCH_SCROLL, "/session/:sessionId/touch/scroll"},
                    {"POST", TOUCH_DOUBLE_TAP, "/session/:sessionId/touch/doubleclick"},
                    {"POST", TOUCH_LONG_PRESS, "/session/:sessionId/touch/longclick"},
                    {"POST", TOUCH_FLICK, "/session/:sessionId/touch/flick"},
                    {"GET", GET_LOCATION, "/session/:sessionId/location"},
                    {"POST", SET_LOCATION, "/session/:sessionId/location"},
                    {"GET", GET_LOCAL_STORAGE_ITEM, "/session/:sessionId/local_storage"},
                    {"POST", SET_LOCAL_STORAGE_ITEM, "/session/:sessionId/local_storage"},
                    {"DELETE", CLEAR_LOCAL_STORAGE, "/session/:sessionId/local_storage"},
                    {"GET", GET_LOCAL_STORAGE_KEYS, "/session/:sessionId/local_storage/key/:key"},
                    {"DELETE", REMOVE_LOCAL_STORAGE_ITEM, "/session/:sessionId/local_storage/key/:key"},
                    {"GET", GET_LOCAL_STORAGE_SIZE, "/session/:sessionId/local_storage/size"},
                    {"GET", GET_SESSION_STORAGE_ITEM, "/session/:sessionId/session_storage"},
                    {"POST", SET_SESSION_STORAGE_ITEM, "/session/:sessionId/session_storage"},
                    {"DELETE", CLEAR_SESSION_STORAGE, "/session/:sessionId/session_storage"},
                    {"GET", GET_SESSION_STORAGE_KEYS, "/session/:sessionId/session_storage/key/:key"},
                    {"DELETE", REMOVE_SESSION_STORAGE_ITEM, "/session/:sessionId/session_storage/key/:key"},
                    {"GET", GET_SESSION_STORAGE_SIZE, "/session/:sessionId/session_storage/size"},
                    {"POST", "getLog", "/session/:sessionId/log"},
                    {"GET", GET_AVAILABLE_LOG_TYPES, "/session/:sessionId/log/types"},
                    {"GET", GET_APP_CACHE_STATUS, "/session/:sessionId/application_cache/status"},

                    // not present in RFC
                    {"GET", GET_ALERT, "/session/:sessionId/alert"},
                    {"POST", SET_BROWSER_VISIBLE, "/session/:sessionId/visible"},
                    {"GET", IS_BROWSER_VISIBLE, "/session/:sessionId/visible"},
                //CISO Player commands
                    {"GET", GET_PLAYER_STATE, "/session/:sessionId/element/:id/-cisco-player-element/state"},
                    {"POST", SET_PLAYER_STATE, "/session/:sessionId/element/:id/-cisco-player-element/state"},
                    {"GET", GET_PLAYER_VOLUME, "/session/:sessionId/element/:id/-cisco-player-element/volume"},
                    {"POST", SET_PLAYER_VOLUME, "/session/:sessionId/element/:id/-cisco-player-element/volume"},
                    {"GET", GET_CURRENT_PLAYING_POSITION, "/session/:sessionId/element/:id/-cisco-player-element/seek"},
                    {"POST", SET_CURRENT_PLAYING_POSITION, "/session/:sessionId/element/:id/-cisco-player-element/seek"},
                    {"GET", GET_PLAYBACK_SPEED, "/session/:sessionId/element/:id/-cisco-player-element/speed"},
                    {"POST", SET_PLAYBACK_SPEED, "/session/:sessionId/element/:id/-cisco-player-element/speed"},
                    {"GET", GET_PLAYER_MUTE, "/session/:sessionId/element/:id/-cisco-player-element/mute"},
                    {"POST", SET_PLAYER_MUTE, "/session/:sessionId/element/:id/-cisco-player-element/mute"},
                    //CISO MultiTouch commands
                    {"POST", TOUCH_PINCH_ZOOM, "/session/:sessionId/touch/-cisco-pinch-zoom"},
                    {"POST", TOUCH_PINCH_ROTATE, "/session/:sessionId/touch/-cisco-pinch-rotate"},
            };

    public QtWebDriverExecutor(URL addressOfRemoteServer)
    {
        super(additionalCommands, addressOfRemoteServer);
    }

    public Response execute(Command command) throws IOException {

        executedCommands.add(command.getName());
        Response response = super.execute(command);

        return response;
    }

    public static ArrayList<String> getExecutedCommands()
    {
        return executedCommands;
    }

    public static void clearExecutedList()
    {
        executedCommands.clear();
    }

    public static String getCommandMethod(String command)
    {
        for (int i=0; i<commands.length; i++)
        {
            if (commands[i][1].equals(command))
                return (String)commands[i][0];
        }

        return null;
    }

    public static String getCommandPath(String command)
    {
        for (int i=0; i<commands.length; i++)
        {
            if (commands[i][1].equals(command))
                return (String)commands[i][2];
        }

        return null;
    }
}
