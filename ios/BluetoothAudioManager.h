
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNBluetoothAudioManagerSpec.h"

@interface BluetoothAudioManager : NSObject <NativeBluetoothAudioManagerSpec>
#else
#import <React/RCTBridgeModule.h>

@interface BluetoothAudioManager : NSObject <RCTBridgeModule>
#endif

@end
