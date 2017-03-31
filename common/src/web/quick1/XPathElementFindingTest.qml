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
    height: 500

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

        Text {
            objectName: "captionLabel"
            text: "This Is Caption"
        }

        Row {
            spacing: 10
            width: parent.width

            Rectangle {
                property alias text: captionText1.text
                objectName: "defaultSelected"
                height: 50
                width: (parent.width - parent.spacing)/2
                Text { id: captionText1; text: "First"; anchors.fill: parent; verticalAlignment: Text.AlignVCenter; horizontalAlignment:  Text.AlignHCenter; }
                border.color: "black"
                color: "green"
            }

            Rectangle {
                property alias text: captionText2.text
                objectName: "noSelected"
                height: 50
                width: (parent.width - parent.spacing)/2
                Text { id: captionText2; text: "Second"; anchors.fill: parent; verticalAlignment: Text.AlignVCenter; horizontalAlignment:  Text.AlignHCenter; }
                border.color: "black"
                color: "yellow"
            }
        }

        TextInput {
            objectName: "inputElement"
            width: parent.width
            fillColor: "darkgrey"
            text: "Example text"
        }

        TextInput {
            objectName: "inputEmpty"
            width: parent.width
            fillColor: "darkgrey"
        }

        TextInput {
            objectName: "inputDisabled"
            width: parent.width
            fillColor: "darkgrey"
            text: "Read Only"
            enabled: false
            readOnly: false
        }

        TextEdit {
            objectName: "workingArea"
            width: parent.width
            height: 100
            fillColor: "darkgrey"
            text: "Example text"
        }

        Item {
            objectName: "scrollArea"
            width: parent.width
            height: 100

            PushButton {
                objectName: "buttonInScrollArea"
                text: "Click here"
            }
        }
    }
}

