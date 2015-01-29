var SerialPrinter = function() {};

SerialPrinter.prototype.say = function(success, fail) {
    cordova.exec(success, fail, "SerialPrinter","say", []);
};

var SerialPrinter = new SerialPrinter();
module.exports = SerialPrinter;