package com.bluetoothaudiomanager;

import android.Manifest;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;

import java.lang.reflect.Method;
import java.util.List;

@ReactModule(name = BluetoothAudioManagerModule.NAME)
public class BluetoothAudioManagerModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
  public static final String NAME = "BluetoothAudioManager";
  private final BluetoothAdapter bluetoothAdapter;
  private final ReactApplicationContext context;
  private BroadcastReceiver receiver;

  public BluetoothAudioManagerModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.context = reactContext;
    reactContext.addLifecycleEventListener(this);
    this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
  }
  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void getDevices(Promise promise) {
    bluetoothAdapter.getProfileProxy(context, new BluetoothProfile.ServiceListener() {
      @Override
      public void onServiceConnected(int profile, BluetoothProfile proxy) {
        if (ActivityCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(context.getCurrentActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
          bluetoothAdapter.closeProfileProxy(BluetoothProfile.A2DP, proxy);
          return;
        }
        List<BluetoothDevice> connectedDevices = proxy.getConnectedDevices();
        WritableArray devices = Arguments.createArray();
        for (BluetoothDevice device : connectedDevices) {
          WritableMap map = Arguments.createMap();
          map.putString("name", device.getName());
          map.putString("address", device.getAddress());
          devices.pushMap(map);
        }
        promise.resolve(devices);
        bluetoothAdapter.closeProfileProxy(BluetoothProfile.A2DP, proxy);
      }

      @Override
      public void onServiceDisconnected(int profile) {
        // 这里可以处理服务断开连接的情况
      }
    }, BluetoothProfile.A2DP);
  }

  private void connectToDevice(BluetoothDevice device, Promise promise) {
    try {
      bluetoothAdapter.getProfileProxy(context, new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
          // 使用反射来调用 setActiveDevice() 方法
          try {
            Method method = proxy.getClass().getMethod("setActiveDevice", BluetoothDevice.class);
            method.invoke(proxy, device);
          } catch (Exception e) {
            // 处理异常
          }
          bluetoothAdapter.closeProfileProxy(BluetoothProfile.A2DP, proxy);
        }

        @Override
        public void onServiceDisconnected(int profile) {
          // do nothing
        }
      }, BluetoothProfile.A2DP);
    } catch (Exception e) {
      promise.reject("CONNECT_FAILED", e.toString());
      // 处理异常
    }
  }

  private void connectToDevice(BluetoothDevice device) {
    try {
      bluetoothAdapter.getProfileProxy(context, new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
          // 使用反射来调用 setActiveDevice() 方法
          try {
            Method method = proxy.getClass().getMethod("setActiveDevice", BluetoothDevice.class);
            method.invoke(proxy, device);
          } catch (Exception e) {
            // 处理异常
          }
          bluetoothAdapter.closeProfileProxy(BluetoothProfile.A2DP, proxy);
        }

        @Override
        public void onServiceDisconnected(int profile) {
          // do nothing
        }
      }, BluetoothProfile.A2DP);
    } catch (Exception e) {
      // 处理异常
    }
  }

  @ReactMethod
  public void lockOutputDevice(String address) {
    if (receiver != null) {
      context.getCurrentActivity().unregisterReceiver(receiver);
      receiver = null;
    }
    receiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED.equals(action)) {
          int state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, BluetoothA2dp.STATE_NOT_PLAYING);
          if(state == BluetoothA2dp.STATE_PLAYING) {
            connectDevice(address);
          }
        }
      }
    };
    IntentFilter filter = new IntentFilter();
    filter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);
    filter.addAction(BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED);
    context.getCurrentActivity().registerReceiver(receiver,filter);
  }

  @ReactMethod
  public void unlockOutputDevice() {
    if (receiver == null) {
      return;
    }
    context.getCurrentActivity().unregisterReceiver(receiver);
  }

  public void connectDevice(String address) {
    if (!BluetoothAdapter.checkBluetoothAddress(address)) {
      return;
    }
    BluetoothDevice device = this.bluetoothAdapter.getRemoteDevice(address);
    connectToDevice(device);
  }

  @ReactMethod
  public void connectDevice(String address, Promise promise) {
    if (!BluetoothAdapter.checkBluetoothAddress(address)) {
      promise.reject("DEVICE_NOT_FOUND", "DEVICE_NOT_FOUND");
      return;
    }
    BluetoothDevice device = this.bluetoothAdapter.getRemoteDevice(address);
    connectToDevice(device, promise);
  }

  @Override
  public void onHostResume() {

  }

  @Override
  public void onHostPause() {

  }

  @Override
  public void onHostDestroy() {
      unlockOutputDevice();
  }
}
