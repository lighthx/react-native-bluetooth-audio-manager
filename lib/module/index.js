import { NativeModules, Platform } from 'react-native';
const LINKING_ERROR = `The package 'react-native-bluetooth-audio-manager' doesn't seem to be linked. Make sure: \n\n` + Platform.select({
  ios: "- You have run 'pod install'\n",
  default: ''
}) + '- You rebuilt the app after installing the package\n' + '- You are not using Expo Go\n';
const BluetoothAudioManager = NativeModules.BluetoothAudioManager ? NativeModules.BluetoothAudioManager : new Proxy({}, {
  get() {
    throw new Error(LINKING_ERROR);
  }
});
export function getDevices() {
  return BluetoothAudioManager.getDevices();
}
export function connectDevice(address) {
  return BluetoothAudioManager.connectDevice(address);
}
export function lockOutputDevice(address) {
  return BluetoothAudioManager.lockOutputDevice(address);
}
export function unlockOutputDevice(address) {
  return BluetoothAudioManager.unlockOutputDevice(address);
}
//# sourceMappingURL=index.js.map