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
            width: parent.width

            Text {
                text: "You changed:"
                color: "darkgrey"
            }

            Text {
                id: result
                objectName: "result"
                text: "default"
            }
        }

        TextInput {
            id: enabledTextElement
            objectName: "enabledTextElement"
            width: parent.width
            fillColor: "darkgrey"
            onTextChanged: {
                result.text = text;
            }

            MouseArea {
                anchors.fill: parent
                onClicked: parent.forceActiveFocus()
            }
        }

        TextEdit {
            id: workingArea
            objectName: "workingArea"
            width: parent.width
            height: parent.height - top - 40
            fillColor: "darkgrey"

            MouseArea {
                anchors.fill: parent
                onClicked: parent.forceActiveFocus()
            }
        }
    }
}

