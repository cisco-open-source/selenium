package org.openqa.selenium.remote;

import org.openqa.selenium.MultiTouchScreen;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.remote.ExecuteMethod;
import org.openqa.selenium.remote.CoordinatesUtils;

import java.util.Map;

public class RemoteMultiTouchScreen implements MultiTouchScreen {

    private final ExecuteMethod executeMethod;

    public RemoteMultiTouchScreen(ExecuteMethod executeMethod) {
        this.executeMethod = executeMethod;
    }

    @Override
    public void pinchZoom(Coordinates where, double scale) {
        Map<String, Object> zoomParams = CoordinatesUtils.paramsFromCoordinates(where);
        zoomParams.put("scale", scale);
        executeMethod.execute(DriverCommand.TOUCH_PINCH_ZOOM, zoomParams);
    }

    @Override
    public void pinchRotate(Coordinates where, int angle) {
        Map<String, Object> rotateParams = CoordinatesUtils.paramsFromCoordinates(where);
        rotateParams.put("angle", angle);
        executeMethod.execute(DriverCommand.TOUCH_PINCH_ROTATE, rotateParams);
    }
}
