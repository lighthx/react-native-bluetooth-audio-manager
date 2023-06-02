import * as React from 'react';

import { useEffect, useState } from 'react';
import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import {
  Device,
  getDevices,
  lockOutputDevice,
} from 'react-native-bluetooth-audio-manager';

export default function App() {
  const [list, setList] = useState<Device[]>([]);
  useEffect(() => {
    getDevices().then((res) => {
      console.log(res);
      setList(res);
    });
  }, []);
  return (
    <View style={styles.container}>
      {list.map((item) => (
        <TouchableOpacity
          key={item.address}
          style={styles.item}
          onPress={() => {
            lockOutputDevice(item.address);
          }}
        >
          <Text>{item.name}</Text>
        </TouchableOpacity>
      ))}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  item: {
    height: 60,
    justifyContent: 'center',
  },
});
