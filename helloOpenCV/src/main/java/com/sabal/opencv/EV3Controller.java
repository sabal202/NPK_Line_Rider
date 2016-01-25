package com.sabal.opencv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import android.bluetooth.*;
import android.content.Context;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

public class EV3Controller extends Connecter implements Robot2WD{

    public void MotorsPowerOn() throws IOException {
        byte ke1[] = {0x08, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA6, 0x00, 0x02};
        Outstream.write(ke1);
        byte ke2[] = {0x08, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA6, 0x00, 0x04};
        Outstream.write(ke2);
    }

    public void MotorsPowerOff() throws IOException {
        byte ke1[] = {0x09, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA3, 0x00, 0x02, 0x01};
        Outstream.write(ke1);
        byte ke2[] = {0x09, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA3, 0x00, 0x04, 0x01};
        Outstream.write(ke2);
    }

    public void MotorsPowerSet(byte PowerA, byte PowerB) throws IOException {
        byte ke1[] = {0x0a, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA4, 0x00, 0x02, (byte) 0x81,(byte) PowerA};
        Outstream.write(ke1);
        byte ke2[] = {0x0a, 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xA4, 0x00, 0x04, (byte) 0x81,(byte) PowerB};
        Outstream.write(ke2);
    }
}

