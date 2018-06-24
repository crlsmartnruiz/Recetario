package com.example.admin.recetario;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class CrearReceta extends AppCompatActivity {
    private EditText nombre, categoria,enlace;
    private Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_receta);

        nombre = (EditText) findViewById(R.id.cuadro_nombre);
        categoria = (EditText) findViewById(R.id.cuadro_categoria);
        boton = (Button) findViewById(R.id.crear_receta);
        enlace = (EditText) findViewById(R.id.meter_link);
        //enlace.setMovementMethod(LinkMovementMethod.getInstance());


        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nombre.getText().toString().equals("") || !categoria.getText().toString().equals("") || !enlace.getText().toString().equals("")) {
                    String[] array = categoria.getText().toString().split(",");
                    ArrayList<String> categs = new ArrayList<>();

                    for(String s:array) {
                        categs.add(s);
                    }

                    RecetasBD bd = new RecetasBD(getApplicationContext());
                    bd.create(nombre.getText().toString(),categs,enlace.getText().toString());
                    Toast.makeText(getApplicationContext(),"La receta " + nombre.getText().toString() + " ha sido creada.",Toast.LENGTH_LONG).show();

                    nombre.setText("");
                    categoria.setText("");
                    enlace.setText("");
                }else {
                    Toast.makeText(getApplicationContext(),"Los campos NOMBRE y CATEGORÍA no pueden estar vacíos",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
