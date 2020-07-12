package com.example.themcalcproapp;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.SensorManager;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import ca.roumani.i2c.MPro;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener ,  SensorEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.tts= new TextToSpeech(this,this);
        SensorManager sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor event=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, event, SensorManager.SENSOR_DELAY_NORMAL);

    }
    public void ButtonClicked (View v){

        MPro mp = new MPro();
        try {
                mp.setPrinciple(((EditText) findViewById(R.id.pBox)).getText().toString());
                mp.setAmortization(((EditText) findViewById(R.id.aBox)).getText().toString());
                mp.setInterest(((EditText) findViewById(R.id.iBox)).getText().toString());
        }
        catch ( Exception e ){
                Toast label = Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT);
                label.show();
        }

        EditText m = ((EditText) findViewById(R.id.aBox));
        EditText l = ((EditText) findViewById(R.id.iBox));
        int i = Integer.parseInt(l.getText().toString());
        int aM = Integer.parseInt(m.getText().toString());

        if (aM>=20 && aM<=30 && i<=50 && i>=0) {
        String s = " Monthly Payment = " + mp.computePayment("%,.2f");
        s+="\n\n";
        s+=" By making this payment monthly for " + ((EditText) findViewById(R.id.aBox)).getText().toString()+" years";
        s+="\n\n";
        s+=" the mortgage will be paid in full. But ";
        s+="\n\n";
        s+=" if you terminate the mortgage on its nth";
        s+="\n\n";
        s+=" anniversary, the balance still owing depends";
        s+="\n\n";
        s+=" on n as shown below:";
        s+="\n\n";
        s+="\n\n";
        s+="       n" + "         Balance";
        s+="\n\n";


        for (int n = 0; n <= aM; n = n + 1) {
            s += String.format("%8d", n) + mp.outstandingAfter(n, "%,16.0f");
            s += "\n\n";
        }
            ((TextView) findViewById(R.id.output)).setText(s);
            tts.speak(s,TextToSpeech.QUEUE_FLUSH,null);

    }




    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }
    @Override
    public void onSensorChanged( SensorEvent event){
        double ax= event.values[0];
        double ay= event.values[1];
        double az= event.values[2];
        double a = Math.sqrt(ax*ax + ay*ay + az*az);
        if(a>10){
            ((EditText) findViewById(R.id.pBox)).setText(" ");
            ((EditText) findViewById(R.id.aBox)).setText(" ");
            ((EditText) findViewById(R.id.iBox)).setText(" ");
            ((TextView) findViewById(R.id.output)).setText(" ");
            tts.stop();

        }
    }
    private TextToSpeech tts;
    @Override
    public void onInit(int initStatus) {
        this.tts.setLanguage(Locale.US);
    }



}




