package com.sabal.opencv;

import java.io.IOException;

/**
 * Created by Maxim on 25.01.2016.
 */
public interface Robot2WD {

    boolean Connect(String Name);
    void Disconnect();
    void MotorsPowerSet(byte PowerA,byte PowerB) throws IOException;
    void MotorsPowerOff() throws IOException;
    void MotorsPowerOn() throws IOException;

}
