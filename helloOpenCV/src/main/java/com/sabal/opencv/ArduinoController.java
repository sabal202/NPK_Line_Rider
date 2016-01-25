package com.sabal.opencv;

import java.io.IOException;

/**
 * Created by Maxim on 25.01.2016.
 */
public class ArduinoController extends Connecter implements Robot2WD{
    @Override
    public void MotorsPowerSet(byte PowerA, byte PowerB) throws IOException {
        byte ke[] = {PowerA,PowerB};
        Outstream.write(ke);
    }

    @Override
    public void MotorsPowerOff() throws IOException {
        MotorsPowerSet((byte)0x00, (byte)0x00);
    }

    @Override
    public void MotorsPowerOn() throws IOException {
        MotorsPowerSet((byte)0x00, (byte)0x00);
    }
}
