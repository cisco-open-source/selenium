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

import QtQuick 1.1

Rectangle {
    id: root
    width: 600
    height: 500

    Rectangle {
        property alias text: caption.text
        property string lastButtonEvent: ""
        id: pureButton
        objectName: "pureButton"
        width: 100
        height: 100
        border.color: "black"
        color: "red"
        Text {
            id: caption
            anchors.fill: parent;
            verticalAlignment: Text.AlignVCenter;
            horizontalAlignment:  Text.AlignHCenter;
            text: "Click me"
        }

        MouseArea {
            id: hotSpot
            anchors.fill: parent;
            acceptedButtons: Qt.LeftButton | Qt.RightButton
            onClicked: {
                if (mouse.button == Qt.LeftButton)
                    caption.text = "Clicked";
                if (mouse.button == Qt.RightButton)
                    caption.text = "ContextClicked";
            }
            onDoubleClicked: caption.text = "DoubleClicked"
            onPressed: pureButton.lastButtonEvent = "Pressed"
            onReleased: pureButton.lastButtonEvent = "Released"
        }
    }

        Rectangle {
            id: itemToDrag
            objectName: "itemToDrag"
            width: 100
            height: 100
            x: 300
            y: 300

            border.color: "black"
            color: "green"

            Text {
                anchors.fill: parent;
                verticalAlignment: Text.AlignVCenter;
                horizontalAlignment:  Text.AlignHCenter;
                text: "Drag me"
            }

            MouseArea {
                id: contentMouseArea
                anchors.fill: parent
                drag.target: itemToDrag
                onPressed: console.log("-----------pressed--------------" + itemToDrag.x + " : " + itemToDrag.y)
                onReleased: console.log("-----------released--------------" + itemToDrag.x + " : " + itemToDrag.y)
                onPressAndHold: console.log("-----------pressAndHold-------------" + itemToDrag.x + " : " + itemToDrag.y)
                onPositionChanged: console.log("----- update: (" + mouse.x +":"+ mouse.y+")" + "("+itemToDrag.x+":"+itemToDrag.y+")")
            }
        }
}
