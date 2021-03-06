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
    property string title
    width: 320
    height: 400

    Column {
        anchors.left: parent.left
        anchors.leftMargin: 10

        anchors.top: parent.top
        anchors.topMargin: 10

        anchors.right: parent.right
        anchors.rightMargin: 10

        anchors.bottom: parent.bottom
        anchors.bottomMargin: 10

        spacing: 20

        Row {
            spacing: 10

            Text {
                id: notClickableLabel
                objectName: "notClickableLabel"
                text: "Label not clickable"
                color: "darkgrey"
            }

            PushButton {
                objectName: "changeLabelButton"
                text: "Change Label"
                onButtonClick: notClickableLabel.text = "Label changed"
            }
        }

        TextInput {
            objectName: "inputElement"
            width: parent.width
            text: "Example text"
        }

        TextInput {
            objectName: "emptyInput"
            width: parent.width
        }

        TextEdit {
            objectName: "workingArea"
            width: parent.width
            height: 100
            text: "Example text"
        }
    }
}

