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
            width: parent.width

            Text {
                objectName: "displayedLabel"
                text: "Visible Label"
                color: "darkgrey"
            }

            Text {
                objectName: "notDisplayedLabel"
                text: "Invisible label."
                visible: false
            }
        }

        Row {
            spacing: 10
            width: parent.width

            Rectangle {
                property alias text: buttonText1.text
                id: button1
                objectName: "workingButton"
                enabled: true

                width: 120
                height: 40
                color: "darkgrey"
                Text {
                    id: buttonText1
                    text: "Working Button"
                }
            }

            Rectangle {
                property alias text: buttonText2.text
                id: button2
                objectName: "notWorkingButton"
                enabled: false
                visible: false

                width: 120
                height: 40
                color: "darkgrey"
                Text {
                    id: buttonText2
                    text: "Disabled button"
                }
            }
        }

        Row {
            spacing: 10
            width: parent.width
            Rectangle {
                width: (parent.width - parent.spacing) / 2
                height: 50
                border.color: "black"
                TextInput {
                    id: disabledTextElement
                    objectName: "disabledTextElement"
                    anchors.fill: parent
                    enabled: false
                }
            }

            Rectangle {
                width: (parent.width - parent.spacing) / 2
                height: 50
                border.color: "black"
                TextInput {
                    id: enabledTextElement
                    objectName: "enabledTextElement"
                    anchors.fill: parent
                }
            }
        }

        Rectangle {
            width: parent.width
            height: 50
            border.color: "black"
            TextEdit {
                id: areaWithDefaultText
                objectName: "areaWithDefaultText"
                anchors.fill: parent
                text: "Example text"
                readOnly: true
                enabled: false
            }
        }

        Rectangle {
            width: parent.width
            height: 50
            border.color: "black"
            TextInput {
                id: workingArea
                objectName: "workingArea"
                anchors.fill: parent
            }
        }

    }
}

