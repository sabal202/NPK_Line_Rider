package com.sabal.opencv;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

/**
 * Created by Maxim on 25.01.2016.
 */
public class MainController {
    public OutputStream Outstream;
    public BluetoothDevice device;
    public BluetoothAdapter bluetooth;
    BluetoothSocket socket;

    public boolean ConnectToBT(String name) {

        bluetooth = BluetoothAdapter.getDefaultAdapter();

        boolean Result = false;

        Set<BluetoothDevice> DevList = bluetooth.getBondedDevices();

        for (BluetoothDevice i : DevList) {
            if (i.getName().equals(name)) device = i;
        }
        if (device == null) {
            return false;
        }

        try {
            socket = device.createInsecureRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
            socket.connect();

            Outstream = socket.getOutputStream();

            Result = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Result = false;
            return Result;
        }


        return Result;

    }


}
