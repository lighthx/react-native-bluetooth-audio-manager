export interface Device {
    name: string;
    address: string;
}
export declare function getDevices(): Promise<Device[]>;
export declare function connectDevice(address: string): Promise<void>;
export declare function lockOutputDevice(address: string): Promise<void>;
export declare function unlockOutputDevice(address: string): Promise<void>;
//# sourceMappingURL=index.d.ts.map