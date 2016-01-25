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
////546454545454544545454545454545454545454554545454544545445454545454544554455445454455454455454454
}
