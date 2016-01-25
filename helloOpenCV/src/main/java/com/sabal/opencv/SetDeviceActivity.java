package com.sabal.opencv;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;

import com.sabal.helloopencv.R;

import java.util.Set;


public class SetDeviceActivity extends Activity {

    public ListView ChooseDev;
    public String Device;
    public EV3Controller contrl = new EV3Controller();
    public String imena [] = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_set_device);
        ChooseDev = (ListView) findViewById(R.id.Choose);

        BluetoothAdapter bluetoothe = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothe.isEnabled()) {
        	ToScreen("\n     Switch on BlueTooth, please!     \n");
			SetDeviceActivity.this.finish();        	
        }
        final Set<BluetoothDevice> DevList = bluetoothe.getBondedDevices();
        imena = new String [DevList.size()];
        int j = 0;
        for(BluetoothDevice i : DevList) {
            imena[j ++] = i.getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,	android.R.layout.simple_list_item_1, imena);

        ChooseDev.setAdapter(adapter);

        ChooseDev.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Device = imena[position];
                Intent intent = new Intent(SetDeviceActivity.this, SetTypeActivity.class);
                intent.putExtra("Device Name", Device);
                startActivity(intent);
                finish();
            }
        });
    }

    public void ToScreen(String stroka) {
        Toast.makeText(getApplicationContext(), stroka, Toast.LENGTH_LONG).show();
    }

}
