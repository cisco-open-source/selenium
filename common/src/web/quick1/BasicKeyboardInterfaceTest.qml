import QtQuick 1.0

Rectangle {
    property string title
    width: 320
    height: 200

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
                text: "Changed: "
                color: "darkgrey"
            }

            Text {
                id: resultIsKeyDown
                objectName: "resultIsKeyDown"
            }

            Text {
                id: resultIsKeyUp
                objectName: "resultIsKeyUp"
            }
        }

        Row {
            spacing: 10
            width: parent.width

            Text {
                text: "Modifier: "
                color: "darkgrey"
            }

            Text {
                id: resultModifier
                objectName: "resultModifier"
            }
        }

        TextInput {
            id: enabledTextElement
            objectName: "enabledTextElement"
            width: parent.width
            fillColor: "darkgrey"

            Keys.onPressed: {
                resultIsKeyDown.text = "Key Down";
                var modifiers = ""

                if (event.modifiers & Qt.ShiftModifier) modifiers += " Shift";
                if (event.modifiers & Qt.ControlModifier) modifiers += " Ctrl";
                if (event.modifiers & Qt.AltModifier) modifiers += " Alt";

                resultModifier.text = modifiers;
            }

            Keys.onReleased: {
                resultIsKeyUp.text = "Key Up";
                var modifiers = ""

                if (event.modifiers & Qt.ShiftModifier) modifiers += " Shift";
                if (event.modifiers & Qt.ControlModifier) modifiers += " Ctrl";
                if (event.modifiers & Qt.AltModifier) modifiers += " Alt";

                resultModifier.text = modifiers;
            }
        }
    }
}

