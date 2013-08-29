import QtQuick 2.0

Rectangle {
    id: mainItem
    property string title: "Javascript Test Page"
    width: 320
    height: 400

    function sayHello(toWhom) {
        return "Hello " + toWhom + " !";
    }

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
            id: result
            objectName: "result"
            text: "default"
            color: "darkgrey"
            width: parent.width
        }

        PushButton {
            objectName: "changeLabelButton"
            text: "Say hello"
        }

        TextInput {
            objectName: "inputElement"
            width: parent.width
            text: "Example text"
            //fillColor: "darkgrey"
        }

        TextInput {
            objectName: "emptyInput"
            width: parent.width
            //fillColor: "darkgrey"
        }

        TextEdit {
            objectName: "workingArea"
            width: parent.width
            height: 100
            text: "Example text"
            //fillColor: "darkgrey"
        }
    }
}

