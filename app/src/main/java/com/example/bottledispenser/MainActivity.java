package com.example.bottledispenser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private SeekBar seekBar;
    private TextView txtView;
    private TextView printTxt;
    private Spinner size;
    private Spinner bottles;
    private BottleDispenser BD;
    private ArrayList<Bottle> list;
    private Context context = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        BD = BottleDispenser.getInstance();
        list = BD.createList();
        printTxt = (TextView) findViewById(R.id.printText);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        txtView = (TextView) findViewById(R.id.txtView);
        size = (Spinner) findViewById(R.id.spinnerSize);
        bottles = (Spinner)findViewById(R.id.spinnerBottle);
        System.out.println("Osoite: "+context.getFilesDir());

        dropdownMenu();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int value = seekBar.getProgress();
                String s = Integer.toString(value);
                s = s+" €";
                txtView.setText(s);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void dropdownMenu(){

        /*
        ArrayList<String> bottlelist = new ArrayList<String>();
        for(Bottle b : list){
            bottlelist.add(b.getName() + " " + Double.toString(b.getSize()));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, bottlelist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        bottles.setAdapter(adapter);
        */

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.size, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size.setAdapter(adapter);
        size.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.bottles, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bottles.setAdapter(adapter2);
        bottles.setOnItemSelectedListener(this);

    }

    public void addMoney(View view) {
        int money = seekBar.getProgress();
        BD.addMoney(money);
        String s = "Klink! Added " + money + "€ to machine.";
        printTxt.setText(s);
        seekBar.setProgress(0);
    }

    public void buyBottle(View view){
        int pos1 = size.getSelectedItemPosition();
        int pos2 = bottles.getSelectedItemPosition();
        String koko = size.getItemAtPosition(pos1).toString();
        double newSize = Double.parseDouble(koko);
        String name = bottles.getItemAtPosition(pos2).toString();
        int index = 0;
        boolean available = false;

        for(Bottle b : list) {
            if (b.getName().equals(name) && b.getSize() == newSize) {
                available = true;
                break;
            }
            index++;
        }
        if(available == false){
            printTxt.setText("Bottle not available. Try another.");
            return;
        }

        int x = BD.buyBottle(list, index);
        switch(x){
            case 1:
                printTxt.setText("KACHUNK! "+name+" "+koko+"l came out of the dispenser!");
                list = BD.removeBottle(list,index);
                writeFile(name, koko);
                break;
            case 2:
                printTxt.setText("Add money first!");
                break;
        }
    }

    public void cashOut(View view){
        String s = BD.getMoney() + "€ returned!";
        printTxt.setText(s);
        BD.returnMoney();
    }

    public void writeFile (String n, String s) {
        try {
            String file = "kuitti.txt";
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_PRIVATE));
            osw.write("Kuitti:\n" + n + s +"l");
            osw.close();
        } catch (IOException e) {
            Log.e("IOException", "Virhe syötteessä");
        } finally {
            System.out.println("Kirjoitettu.");
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}