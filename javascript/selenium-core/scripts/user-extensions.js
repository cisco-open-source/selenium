// User extensions can be added here.
//
// Keep this file to avoid  mystifying "Invalid Character" error in IE

Selenium.prototype.doRunScriptBySrc = function(src) {
    var win = this.browserbot.getCurrentWindow();
    var doc = win.document;
    var scriptTag = doc.createElement('script');
    scriptTag.type = "text/javascript"
    scriptTag.src = src;
    doc.head.appendChild(scriptTag);
}