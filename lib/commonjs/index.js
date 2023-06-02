"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.connectDevice = connectDevice;
exports.getDevices = getDevices;
exports.lockOutputDevice = lockOutputDevice;
exports.unlockOutputDevice = unlockOutputDevice;
var _reactNative = require("react-native");
const LINKING_ERROR = `The package 'react-native-bluetooth-audio-manager' doesn't seem to be linked. Make sure: \n\n` + _reactNative.Platform.select({
  ios: "- You have run 'pod install'\n",
  default: ''
}) + '- You rebuilt the app after installing the package\n' + '- You are not using Expo Go\n';
const BluetoothAudioManager = _reactNative.NativeModules.BluetoothAudioManager ? _reactNative.NativeModules.BluetoothAudioManager : new Proxy({}, {
  get() {
    throw new Error(LINKING_ERROR);
  }
});
function getDevices() {
  return BluetoothAudioManager.getDevices();
}
function connectDevice(address) {
  return BluetoothAudioManager.connectDevice(address);
}
function lockOutputDevice(address) {
  return BluetoothAudioManager.lockOutputDevice(address);
}
function unlockOutputDevice(address) {
  return BluetoothAudioManager.unlockOutputDevice(address);
}
//# sourceMappingURL=index.js.map