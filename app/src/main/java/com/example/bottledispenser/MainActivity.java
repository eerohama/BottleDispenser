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
    private Spinner bottles;
    private BottleDispenser BD;
    private ArrayList<Bottle> list;
    private Context context = null;
    private ArrayList<String> bottlelist;


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

        bottlelist = new ArrayList<String>();
        for(Bottle b : list){
            bottlelist.add(b.getName() + " " + Double.toString(b.getSize()));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, bottlelist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        bottles.setAdapter(adapter);
    }



    public void addMoney(View view) {
        int money = seekBar.getProgress();
        BD.addMoney(money);
        String s = "Klink! Added " + money + "€ to machine.";
        printTxt.setText(s);
        seekBar.setProgress(0);
    }

    public void buyBottle(View view){


        int pos2 = bottles.getSelectedItemPosition();
        String s = "";
        try {
            s = bottles.getItemAtPosition(pos2).toString();
        } catch(Exception e){
            Log.e("Exception", "Out of bottles!");
            printTxt.setText("Out of bottles!");
            return;
        }
        String pullo = s;
        int index = 0;
        boolean available = false;

        for(Bottle b : list) {
            String str = b.getName() + " " + Double.toString(b.getSize());
            if (pullo.equals(str)) {
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
                printTxt.setText("KACHUNK! "+pullo+"l came out of the dispenser!");
                double price = list.get(index).getPrice();
                list = BD.removeBottle(list,index);
                bottlelist.remove(index);
                writeFile(pullo,price);
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

    public void writeFile (String s, double p) {
        String price = Double.toString(p);
        try {
            String file = "kuitti.txt";
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_APPEND));
            osw.write("Kuitti: "+s+"l "+price+"€\n");
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