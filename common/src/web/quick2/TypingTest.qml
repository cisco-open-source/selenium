import QtQuick 2.0
import QtQuick.Controls 1.0

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

        Rectangle {
            width: parent.width
            height: 20
            border.color: "black"

            TextInput {
                id: enabledTextElement
                objectName: "enabledTextElement"
                anchors.fill: parent
                onTextChanged: {
                    result.text = text;
                }
                onFocusChanged: console.log("textInput focus:"+activeFocus)
            }
        }

        Rectangle {
            width: parent.width
            height: 80
            border.color: "black"

            TextEdit {
                id: workingArea
                objectName: "workingArea"
                anchors.fill: parent
                onFocusChanged: console.log("textEdit focus:"+activeFocus)
            }
        }
    }

}

