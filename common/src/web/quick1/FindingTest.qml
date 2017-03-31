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
        spacing: 2

        TextInput {
            id: inputElement
            objectName: "inputElement"
            text: { "<b>" + "(^ ^)" + "</b>" }
            font.pixelSize: 18
        }

        Rectangle { objectName: "hiddenRect"; color: "red"; width: 50; height: 50; visible: false }
        Rectangle {
            color: "green"; width: 20; height: 50;
            MouseArea {
                objectName: "mouseHotSpot"
                anchors.fill: parent
                onClicked: { parent.color = 'red' }
            }
        }
        Rectangle { color: "blue"; width: 50; height: 20 }
    }
}

