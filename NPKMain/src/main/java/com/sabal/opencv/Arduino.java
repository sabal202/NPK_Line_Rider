package com.sabal.opencv;

import java.io.IOException;

public class Arduino extends Connector {
    byte Pow = 0;
    @Override
    public void MotorsPowerSet(byte PowerA, byte PowerB) throws IOException {
        byte ke[] = {(byte) (PowerA*Pow), (byte) (PowerB*Pow),(byte)0xFF};
        Outstream.write(ke);
    }

    @Override
    public void MotorsPowerOff() throws IOException {
        Pow = 0;
        MotorsPowerSet((byte)0x00, (byte)0x00);
    }

    @Override
    public void MotorsPowerOn() throws IOException {
        Pow =1;
        MotorsPowerSet((byte)0x00, (byte)0x00);
    }
}
