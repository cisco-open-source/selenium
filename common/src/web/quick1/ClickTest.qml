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

import QtQuick 1.0

Rectangle {
    property string title
    width: 320
    height: 240

    Column {
        anchors.left: parent.left
        anchors.leftMargin: 10

        anchors.top: parent.top
        anchors.topMargin: 10

        spacing: 20
        width: 180

        Text {
            id: clickDisplay
            objectName: "clickDisplay"
            text: "no action"
            font.pixelSize: 18
        }

        Rectangle {
            id: button1
            objectName: "button1"

            width: parent.width
            height: 40
            color: "green"
            Text {
                //anchors.fill: parent
                text: "push me"
            }

            MouseArea {
                objectName: "mouseHotSpotButton1"
                anchors.fill: parent
                onClicked: { clickDisplay.text = "clicked button1" }
            }
        }

        MouseArea {
            objectName: "mouseHotSpotArea2"
            width: 40
            height: 40
            onClicked: { clickDisplay.text = "clicked area 2" }
        }
    }
}

