import QtQuick 1.0
import CiscoQtWebKit 1.0

Rectangle {
    width: 640
    height: 480
    color: "yellow"

    Text {
        text: webView.url
    }

    Rectangle {
        anchors.fill: parent
        anchors.bottomMargin: 40
        anchors.topMargin: 40
        anchors.leftMargin: 40
        anchors.rightMargin: 40
        border.color: "black"
        color: "blue"

        CiscoWebView {
            id: webView
            html: "<p>This is <b>HTML</b>."
            //preferredWidth: 640
            //preferredHeight: 480
            smooth: false
            anchors.fill: parent
        }
    }
}


