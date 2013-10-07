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
                objectName: "notDisplayedLabel"
                text: "No Visible Label"
                color: "darkgrey"
                visible: false
            }

            Text {
                objectName: "displayedLabel"
                text: "Visible Label"
                color: "darkgrey"
            }

            Text {
                objectName: "zeroLabel"
                color: "darkgrey"
                width: 0
                height: 0
            }
        }

        Row {
            spacing: 10

            PushButton {
                objectName: "buttonCanHide"
                text: "Click to Hide"
                onButtonClick: visible = false
            }

            PushButton {
                objectName: "buttonHidden"
                text: "hidden"
                visible: false
            }
        }

        TextInput {
            objectName: "inputHidden"
            width: parent.width
            visible: false
        }

        Rectangle {
            objectName: "hiddenScrollArea"
            width: parent.width
            height: 100
            border.color: "black"
            visible: false

            PushButton {
                objectName: "buttonInScrollArea"
                text: "Click here"
            }
        }
    }
}

