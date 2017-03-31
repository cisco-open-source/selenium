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

package org.openqa.selenium.remote;

import org.openqa.selenium.interactions.MultiTouchScreen;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.remote.ExecuteMethod;
import org.openqa.selenium.remote.CoordinatesUtils;

import java.util.Map;

public class RemoteMultiTouchScreen extends RemoteTouchScreen implements MultiTouchScreen {

    private final ExecuteMethod executeMethod;

    public RemoteMultiTouchScreen(ExecuteMethod executeMethod) {
        super(executeMethod);
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
