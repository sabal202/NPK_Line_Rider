package com.sabal.opencv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sabal.helloopencv.R;

public class SetTypeActivity extends Activity {

    String Types[] = {"LEGO Mindstorms EV3","Arduino 2WD Robot","LEGO Mindstorms NXT"};
    ListView ChooseType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_type);

        ChooseType = (ListView) findViewById(R.id.Choose);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,	android.R.layout.simple_list_item_1, Types);
        ChooseType.setAdapter(adapter);

        ChooseType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ToScreen(Integer.toString(i+1));
                Intent intent = new Intent(SetTypeActivity.this, OpenCVMainActivity.class);
                intent.putExtra("Device Name", getIntent().getExtras().getString("Device Name","null"));
                intent.putExtra("Type",i);
                startActivity(intent);
            }
        });
    }

    public void ToScreen(String stroka) {
        Toast.makeText(getApplicationContext(), stroka, Toast.LENGTH_SHORT).show();
    }
}
