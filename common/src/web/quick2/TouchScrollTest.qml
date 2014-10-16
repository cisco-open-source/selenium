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
    id: root
    width: 400
    height: 620


    ListView {
        id: list_view
        objectName: "listView"
        anchors.fill: parent
        spacing: 10
        clip: true
        property real maxVelocity: 0.0
        onVerticalVelocityChanged: {
            if (verticalVelocity > maxVelocity)
            {
                maxVelocity = verticalVelocity
                resultLabel.text ="Flicked"
            }

        }
        Text {
            id: resultLabel
            objectName: "resultLabel"
            color: "black"
        }

        delegate: Item {
            x: 0
            height: 40
            objectName: "item"+index
            Rectangle {
                width: root.width
                height: 40
                border.color: colorCode
                border.width: 2
                color: "white"

                Text {
                    text: name
                    id: label
                    anchors.verticalCenter: parent.verticalCenter
                    anchors.horizontalCenter: parent.horizontalCenter
                }

            }

        }

        model: ListModel {
            ListElement {
                name: "Grey"
                colorCode: "grey"
            }

            ListElement {
                name: "Red"
                colorCode: "red"
            }

            ListElement {
                name: "Blue"
                colorCode: "blue"
            }

            ListElement {
                name: "Green"
                colorCode: "green"
            }

            ListElement {
                name: "Grey2"
                colorCode: "grey"
            }

            ListElement {
                name: "Red2"
                colorCode: "red"
            }

            ListElement {
                name: "Blue2"
                colorCode: "blue"
            }

            ListElement {
                name: "Green2"
                colorCode: "green"
            }
            ListElement {
                name: "Grey3"
                colorCode: "grey"
            }

            ListElement {
                name: "Red3"
                colorCode: "red"
            }

            ListElement {
                name: "Blue3"
                colorCode: "blue"
            }

            ListElement {
                name: "Green3"
                colorCode: "green"
            }

            ListElement {
                name: "Grey4"
                colorCode: "grey"
            }

            ListElement {
                name: "Red4"
                colorCode: "red"
            }

            ListElement {
                name: "Blue4"
                colorCode: "blue"
            }

            ListElement {
                name: "Green4"
                colorCode: "green"
            }
            ListElement {
                name: "Grey5"
                colorCode: "grey"
            }

            ListElement {
                name: "Red5"
                colorCode: "red"
            }

            ListElement {
                name: "Blue5"
                colorCode: "blue"
            }

            ListElement {
                name: "Green5"
                colorCode: "green"
            }

            ListElement {
                name: "Grey6"
                colorCode: "grey"
            }

            ListElement {
                name: "Red6"
                colorCode: "red"
            }

            ListElement {
                name: "Blue6"
                colorCode: "blue"
            }

            ListElement {
                name: "Green6"
                colorCode: "green"
            }
            ListElement {
                name: "Grey7"
                colorCode: "grey"
            }

            ListElement {
                name: "Red7"
                colorCode: "red"
            }

            ListElement {
                name: "Blue7"
                colorCode: "blue"
            }

            ListElement {
                name: "Green7"
                colorCode: "green"
            }

            ListElement {
                name: "Grey8"
                colorCode: "grey"
            }

            ListElement {
                name: "Red8"
                colorCode: "red"
            }

            ListElement {
                name: "Blue8"
                colorCode: "blue"
            }

            ListElement {
                name: "Green8"
                colorCode: "green"
            }
            ListElement {
                name: "Grey9"
                colorCode: "grey"
            }

            ListElement {
                name: "Red9"
                colorCode: "red"
            }

            ListElement {
                name: "Blue9"
                colorCode: "blue"
            }

            ListElement {
                name: "Green9"
                colorCode: "green"
            }

            ListElement {
                name: "Grey10"
                colorCode: "grey"
            }

            ListElement {
                name: "Red10"
                colorCode: "red"
            }

            ListElement {
                name: "Blue10"
                colorCode: "blue"
            }

            ListElement {
                name: "Green10"
                colorCode: "green"
            }
            ListElement {
                name: "Grey11"
                colorCode: "grey"
            }

            ListElement {
                name: "Red11"
                colorCode: "red"
            }

            ListElement {
                name: "Blue11"
                colorCode: "blue"
            }

            ListElement {
                name: "Green11"
                colorCode: "green"
            }

            ListElement {
                name: "Grey12"
                colorCode: "grey"
            }

            ListElement {
                name: "Red12"
                colorCode: "red"
            }

            ListElement {
                name: "Blue12"
                colorCode: "blue"
            }

            ListElement {
                name: "Green12"
                colorCode: "green"
            }

        }
    }
}
