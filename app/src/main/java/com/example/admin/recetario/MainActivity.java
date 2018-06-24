package com.example.admin.recetario;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private MyAdapter myAdapter;
    private Button boton,botonBorrar;
    private EditText cuadroBusqueda;
    private RecetasBD bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.lista_recetas);
        boton = (Button) findViewById(R.id.boton_crear);
        botonBorrar = (Button) findViewById(R.id.borrar);
        cuadroBusqueda = (EditText) findViewById(R.id.busqueda);

        bd = new RecetasBD(getApplicationContext());

        myAdapter = new MyAdapter(this,bd.getRecetas(cuadroBusqueda.getText().toString()));
        listView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        cuadroBusqueda.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                actualizarArray();
            }
        });

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),CrearReceta.class);
                startActivity(i);
            }
        });

        botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cuadroBusqueda.setText("");
            }
        });

    }

    protected void onResume() {
        super.onResume();
        actualizarArray();
    }

    public void actualizarArray() {
        myAdapter.setRecetas(bd.getRecetas(cuadroBusqueda.getText().toString()));
        myAdapter.notifyDataSetChanged();
    }

    class MyAdapter extends BaseAdapter {
        private Context contexto;
        private ArrayList<Receta> recetas;


        public MyAdapter(Context context, ArrayList<Receta> lista) {
            this.contexto = context;
            this.recetas = lista;
        }

        @Override
        public int getCount() {
            return recetas.size();
        }

        @Override
        public Object getItem(int i) {
            return recetas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void setRecetas(ArrayList<Receta> recetas) {
            this.recetas = recetas;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;

            if(view == null) {
                //Crea una nueva vista en la lista
                LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.item_receta, viewGroup,false);
            }

            //Fijar la información en la vista

            TextView nombre = (TextView) rowView.findViewById(R.id.nombre);
            TextView categorias = (TextView) rowView.findViewById(R.id.categorias);
            TextView enlace = (TextView) rowView.findViewById(R.id.link);
            final Button botonBorrar = (Button) rowView.findViewById(R.id.boton_borrar);
            Button botonModify = (Button) rowView.findViewById(R.id.boton_modif);

            final Receta receta = recetas.get(i);

            botonBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bd.delete(receta);
                    actualizarArray();
                }
            });

            botonModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i =  new Intent(getApplicationContext(),ModifyReceta.class).putExtra("receta",receta);
                    startActivity(i);
                }
            });

            String cat = "";

            for(String s:receta.getCategorias()) {
                cat += s + ",";
            }

            cat = cat.substring(0,cat.length() - 1);

            nombre.setText(receta.getNombre());
            categorias.setText(cat);
            enlace.setText(receta.getLink());

            return rowView;
        }
    }
}
