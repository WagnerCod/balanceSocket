var exec = require('cordova/exec');

exports.connect = function(host, port, success, error) {
  exec(success, error, 'BalanceSocket', 'connect', [host, port]);
};
exports.disconnect = function(success, error) {
  exec(success, error, 'BalanceSocket', 'disconnect', []);
};
