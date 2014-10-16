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

import QtQuick 2.0

Rectangle {
    width: 400
    height: 600
    color: "black"
    Column {
        spacing: 10
        anchors.fill: parent

        Row {
            spacing: 10
            Text {
                text: "Zoom factor:"               
                color: "white"
            }
            Text {
                id: zoomFactorLbl
                objectName: "zoomLbl"
                color: "white"
                text: "0"
            }
        }

        Row {
            spacing: 10
            Text {
                text: "Rotation:"
                color: "white"
            }
            Text {
                id: rotationLbl
                objectName: "rotationLbl"
                color: "white"
                text: "0"
            }
        }

        Rectangle {
            height: 560
            width: 400
            color: "yellow"
            Rectangle
            {
                id: actionItem
                objectName: "pinchItem"
                height: 20
                width: 20
                color: "blue"
                anchors.centerIn: parent

                PinchArea {
                    anchors.fill: parent
                    pinch.target: actionItem
                    onPinchUpdated: {                  
                        rotationLbl.text = Math.round(pinch.rotation)
                        actionItem.rotation = Math.round(pinch.scale)
                        zoomFactorLbl.text = pinch.scale
                        actionItem.scale = pinch.scale
                    }
                    onPinchFinished: {
                        rotationLbl.text = Math.round(pinch.rotation)
                        actionItem.rotation = Math.round(pinch.rotation)
                        zoomFactorLbl.text = pinch.scale
                        actionItem.scale = pinch.scale
                    }
                }
            }
        }
    }
}
