package com.example.gauge_chart;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ekn.gruzer.gaugelibrary.HalfGauge;

public class MainActivity extends AppCompatActivity {


    Button idBtnSubir, idBtnBaixar;
    HalfGauge idMedidor;
     com.ekn.gruzer.gaugelibrary.Range Rango_1, Rango_2,Rango_3;
     int SetearGrafica;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        idBtnSubir = findViewById(R.id.idBtnSubir);
        idBtnBaixar = findViewById(R.id.idBtnBaixar);
        idMedidor = findViewById(R.id.idMedidor);

        Rango_1 = new com.ekn.gruzer.gaugelibrary.Range();
        Rango_2 = new com.ekn.gruzer.gaugelibrary.Range();
        Rango_3 = new com.ekn.gruzer.gaugelibrary.Range();

        Rango_1.setFrom(0);Rango_1.setTo(100);
        Rango_2.setFrom(100);Rango_2.setTo(150);
        Rango_3.setFrom(150);Rango_3.setTo(200);

        Rango_1.setColor(Color.GREEN);
        Rango_2.setColor(Color.BLUE);
        Rango_3.setColor(Color.RED);
        idMedidor.setMinValue(0);
        idMedidor.setMaxValue(200);
        idMedidor.setValue(0);

        idMedidor.addRange(Rango_1);
        idMedidor.addRange(Rango_2);
        idMedidor.addRange(Rango_3);

        idBtnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetearGrafica = SetearGrafica +10;
                if (SetearGrafica>200){SetearGrafica=200;}
                idMedidor.setValue(SetearGrafica);
            }
        });


        idBtnBaixar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetearGrafica = SetearGrafica -10;
                if (SetearGrafica<0){SetearGrafica=0;}
                idMedidor.setValue(SetearGrafica);
            }
        });

        idMedidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}