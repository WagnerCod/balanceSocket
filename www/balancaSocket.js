var exec = require('cordova/exec');

exports.conectarBalanca = function(ip, porta, success, error) {
  exec(success, error, 'BalancaSocket', 'conectarBalanca', [ip, porta]);
};
