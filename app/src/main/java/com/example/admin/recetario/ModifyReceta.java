package com.example.admin.recetario;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ModifyReceta extends AppCompatActivity {

    private Receta receta;
    private RecetasBD bd;
    private EditText nombre,link,categ;
    private Button modificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_receta);

        receta = (Receta) getIntent().getSerializableExtra("receta");
        bd = new RecetasBD(getApplicationContext());

        //

        nombre = (EditText) findViewById(R.id.cuadro_nombre_modf);
        categ = (EditText) findViewById(R.id.cuadro_categoria_modf);
        link = (EditText) findViewById(R.id.meter_link_modf);
        modificar = (Button) findViewById(R.id.modify_receta);



        String catAux = "";

        for(String cat:receta.getCategorias()) {
            catAux += cat + ",";
        }
        catAux = catAux.substring(0,catAux.length() - 1);

        nombre.setText(receta.getNombre());
        categ.setText(catAux);
        link.setText(receta.getLink());
    }

    public void modifyReceta(View view) {
        receta.setNombre(nombre.getText().toString());
        receta.setLink(link.getText().toString());

        String[] array = categ.getText().toString().split(",");
        ArrayList<String> categs = new ArrayList<>();

        for(String s:array) {
            categs.add(s);
        }

        receta.setCategorias(categs);

        bd.modify(receta);
        Toast.makeText(getApplicationContext(),"La receta se ha modificado correctamente con ID " + receta.getId(),Toast.LENGTH_LONG).show();
    }


}
