package com.example.admin.recetario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Admin on 13/06/2018.
 */

public class RecetasBD extends SQLiteOpenHelper{

    static final String name = "SqliteRecetas";
    static final int version = 1;
    Context context;

    public RecetasBD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name,factory,version);
    }

    public RecetasBD(Context context) {
        super(context,name,null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Receta(" +
                "   ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "   Nombre           TEXT      NOT NULL," +
                "   Link           TEXT      NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE Categoria (categoria varchar(64) not null, receta integer not null, primary key(categoria,receta), foreign key (receta) references Receta(id) ON UPDATE CASCADE);");
        //sqLiteDatabase.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void create(String nombre, ArrayList<String> categorias,String link) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            if(db != null) {
                ContentValues values = new ContentValues();
                values.put("Nombre",nombre);
                values.put("Link",link);
                db.insert("Receta",null,values);

                ArrayList<Receta> recetas = getRecetas(nombre);

                Receta recetaConId = findReceta(new Receta(nombre,link,categorias));

                for(String s: categorias) {
                    db.execSQL("INSERT INTO Categoria VALUES ('" + s + "'," + recetaConId.getId() + ")");
                }
            }
            //db.close();
        }catch (SQLiteException e) {
            Log.e("Error BD",e.toString());
        }
    }

    public void delete(Receta receta) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            if(db != null) {
                db.execSQL("DELETE FROM Receta WHERE id=" + receta.getId());
            }
            //db.close();
        }catch (SQLiteException e) {
            Log.e("Error BD",e.toString());
        }
    }

    public void modify(Receta nueva) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            if(db != null) {
                db.execSQL("UPDATE Receta SET nombre='" + nueva.getNombre() + "' WHERE id=" + nueva.getId());
                db.execSQL("UPDATE Receta SET link='" + nueva.getLink() + "' WHERE id=" + nueva.getId());

                db.execSQL("DELETE FROM CATEGORIA WHERE receta=" + nueva.getId());

                for(String categoria: nueva.getCategorias()) {
                    db.execSQL("INSERT INTO Categoria VALUES ('" + categoria + "'," + nueva.getId() + ")");
                }
            }
            //db.close();
        }catch (SQLiteException e) {
            Log.e("Error BD",e.toString());
        }
    }

    public Receta findReceta(Receta recetaAux) {
        ArrayList<Receta> recetas = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        if(db != null) {
            Cursor c = db.rawQuery("SELECT * FROM RECETA ORDER BY nombre",null);


            if(c.moveToFirst()) {
                do {
                    Receta receta = new Receta();
                    receta.setId(c.getInt(0));
                    receta.setNombre(c.getString(1));
                    receta.setLink(c.getString(2));
                    recetas.add(receta);

                    Cursor aux = db.rawQuery("SELECT Categoria FROM Categoria WHERE receta=" + receta.getId(),null);
                    ArrayList<String> categorias = new ArrayList<>();

                    if(aux.moveToFirst()) {
                        do {
                            categorias.add(aux.getString(0));
                        }while(aux.moveToNext());
                    }
                    receta.setCategorias(categorias);

                } while(c.moveToNext());
            }
        }

        for(Receta recetaAux2:recetas) {
            if(recetaAux2.getNombre().equals(recetaAux.getNombre()))
                return recetaAux2;
        }

        //db.close();

        return null;
    }

    public ArrayList<Receta> getRecetas (String busqueda) {
        ArrayList<Receta> recetas = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        if(db != null) {
            Cursor c = db.rawQuery("SELECT * FROM RECETA ORDER BY nombre",null);

            if(c.moveToFirst()) {
                do {
                    Receta receta = new Receta();
                    receta.setId(c.getInt(0));
                    receta.setNombre(c.getString(1));
                    receta.setLink(c.getString(2));
                    recetas.add(receta);

                    Cursor aux = db.rawQuery("SELECT Categoria FROM Categoria WHERE receta=" + receta.getId(),null);
                    ArrayList<String> categorias = new ArrayList<>();

                    if(aux.moveToFirst()) {
                        do {
                            categorias.add(aux.getString(0));
                        }while(aux.moveToNext());
                    }
                    receta.setCategorias(categorias);
                } while(c.moveToNext());
            }
        }

        //db.close();

        if(busqueda.equals("")) return recetas;
        else {
            ArrayList<Receta> recetasAux = new ArrayList<>();

            for(Receta r: recetas) {
                if(r.getNombre().toUpperCase().contains(busqueda.toUpperCase())) {
                    recetasAux.add(r);
                    continue;
                }

                for(String categ: r.getCategorias()) {
                    if(categ.toUpperCase().contains(busqueda.toUpperCase())) {
                        recetasAux.add(r);
                        break;
                    }
                }
            }

            return recetasAux;
        }
    }
}
