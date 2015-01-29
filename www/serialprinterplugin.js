var SerialPrinterPlugin = function() {};

SerialPrinterPlugin.prototype.say = function(success, fail) {
    cordova.exec(success, fail, "SerialPrinterPlugin","say", []);
};

var SerialPrinterPlugin = new SerialPrinterPlugin();
module.exports = SerialPrinterPlugin;