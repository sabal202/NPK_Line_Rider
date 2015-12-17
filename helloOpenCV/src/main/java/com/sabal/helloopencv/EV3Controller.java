package com.sabal.helloopencv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import android.bluetooth.*;
import android.content.Context;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

public class EV3Controller extends Object {

    public OutputStream Outstream;
    public byte CameOn;
    public BluetoothDevice device;
    public BluetoothAdapter bluetooth;
    //public InputStream Instream;
    private byte[] _sizeBuffer = new byte[2];
    BluetoothSocket socket;

    public boolean ConnectToBT(String name) {

        bluetooth = BluetoothAdapter.getDefaultAdapter();

        boolean Result = false;
        int Poop = 0;

        Set<BluetoothDevice> DevList = bluetooth.getBondedDevices();

        Log.e("KEK", name);
        for (BluetoothDevice i : DevList) {
            Log.e("KEK",i.getName());
            if (i.getName().equals(name)) device = i;
        }
        if (device == null) {
            //Log.e("Жопец", "Еггог");
            return false;
        }

        try {
            socket = device.createInsecureRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
            socket.connect();

            Outstream = socket.getOutputStream();

            //Instream = socket.getInputStream();

            Result = true;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Result = false;
            return Result;
        }


        return Result;

    }

    public void MotorPowerOn(byte Motor) throws IOException {
        byte ke[] = {0x08, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA6, 0x00, (byte)Motor};
        Outstream.write(ke);
    }

    public void MotorPowerOff(byte Motor) throws IOException {
        byte ke[] = {0x09, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA3, 0x00, (byte)Motor, 0x01};
        Outstream.write(ke);
    }

    public void MotorPowerSet(byte Motor, byte Power) throws IOException {
        byte ke[] = {0x0a, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA4, 0x00, (byte)Motor, (byte) 0x81,(byte) Power};
        Outstream.write(ke);
    }

    /*public byte[] InPut(){
        short size = (short) (_sizeBuffer[0] | _sizeBuffer[1] << 8);
        byte[] report = new byte[size];
        try {
            Instream.read(report, 0, report.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return report;
    }*/

    public void Disconnect () {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

