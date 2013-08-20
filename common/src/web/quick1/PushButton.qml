import QtQuick 1.0

Rectangle {
    id: button
    color: "red"
    property alias text: caption.text
    width: 96; height: 24; anchors.centerIn: parent

    Text {
        id: caption;
        anchors.fill: parent;
        verticalAlignment: Text.AlignVCenter;
        horizontalAlignment:  Text.AlignHCenter;
    }

    MouseArea {
        anchors.fill: parent;
        onClicked: console.log("clicked()")
    }
}
