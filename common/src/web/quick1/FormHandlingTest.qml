import QtQuick 1.0

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
            fillColor: "darkgrey"
        }

        TextInput {
            objectName: "emptyInput"
            width: parent.width
            fillColor: "darkgrey"
        }

        TextEdit {
            objectName: "workingArea"
            width: parent.width
            height: 100
            text: "Example text"
            fillColor: "darkgrey"
        }
    }
}

