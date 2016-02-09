package com.sabal.opencv;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

public abstract class Connector implements Robot2WD {

    public OutputStream Outstream;
    public BluetoothDevice device;
    public BluetoothAdapter bluetooth;
    BluetoothSocket socket;

    public boolean Connect(String name) throws IOException {

        bluetooth = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> DevList = bluetooth.getBondedDevices();

        for (BluetoothDevice i : DevList) {
            if (i.getName().equals(name)) device = i;
        }
        if (device == null) {
            return false;
        }

        socket = device.createInsecureRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
        socket.connect();
        Outstream = socket.getOutputStream();
        
        return Outstream != null;
    }

    public void Disconnect () throws IOException {
        socket.close();
    }
}
