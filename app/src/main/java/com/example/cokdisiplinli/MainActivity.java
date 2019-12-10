package com.example.cokdisiplinli;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.hardware.camera2.params.BlackLevelPattern;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter myBluetooth;
    private Set<BluetoothDevice> pairedDevices;
    Button bluetooth_toggle_button;
    Button show_devices;
    ListView device_list;
    public static String EXTRA_ADRESS = "device_adress";
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        bluetooth_toggle_button = findViewById(R.id.bluetooth_toggle);
        show_devices = findViewById(R.id.show_devices);
        device_list = findViewById(R.id.device_list);

        bluetooth_toggle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggleBluetooth();
            }
        });

        show_devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDevices();
            }
        });

    }

    private void toggleBluetooth(){

        if(myBluetooth==null){
            Toast.makeText(getApplicationContext(),"Bluetooth Cihazı Bulunamadı",Toast.LENGTH_SHORT).show();
        }
        if(!myBluetooth.isEnabled()){

            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);
        }
        if(myBluetooth.isEnabled()){
            myBluetooth.disable();
        }

    }

    private void listDevices(){

        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if(pairedDevices.size()>0){
            for(BluetoothDevice bt: pairedDevices){
                list.add(bt.getName()+"\n"+bt.getAddress());
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"No Paired Devices",Toast.LENGTH_SHORT).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        device_list.setAdapter(adapter);
        device_list.setOnItemClickListener(select_device);


    }

    public AdapterView.OnItemClickListener select_device = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String info = ((TextView) view).getText().toString();
            String adress = info.substring(info.length()-17);

            Intent comintent = new Intent(MainActivity.this,Communication.class);
            comintent.putExtra(EXTRA_ADRESS, adress);
            startActivity(comintent);


        }
    };

}
