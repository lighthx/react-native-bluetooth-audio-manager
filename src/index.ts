import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-bluetooth-audio-manager' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const BluetoothAudioManager = NativeModules.BluetoothAudioManager
  ? NativeModules.BluetoothAudioManager
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export interface Device {
  name: string;
  address: string;
}
export function getDevices(): Promise<Device[]> {
  return BluetoothAudioManager.getDevices();
}

export function connectDevice(address: string): Promise<void> {
  return BluetoothAudioManager.connectDevice(address);
}

export function lockOutputDevice(address: string): Promise<void> {
  return BluetoothAudioManager.lockOutputDevice(address);
}
