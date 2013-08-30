import QtQuick 2.0

Rectangle {
    property string title
    width: 320
    height: 240

    Text {
        id: content
        text: "WindowTest"
        font.pointSize: 24
        verticalAlignment: Text.AlignVCenter
        horizontalAlignment: Text.AlignHCenter
        width: parent.width
        height: parent.height
    }
}
