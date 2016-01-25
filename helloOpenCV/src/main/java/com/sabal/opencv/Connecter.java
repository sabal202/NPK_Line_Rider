package com.sabal.opencv;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

/**
 * Created by Maxim on 25.01.2016.
 */
public class Connecter extends Object{

    public OutputStream Outstream;
    public BluetoothDevice device;
    public BluetoothAdapter bluetooth;
    BluetoothSocket socket;

    public boolean Connect(String name) {

        bluetooth = BluetoothAdapter.getDefaultAdapter();

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
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public void Disconnect () {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
