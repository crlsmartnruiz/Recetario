package com.example.admin.recetario;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 13/06/2018.
 */

public class Receta implements Serializable{

    private String nombre, link;
    private int id;
    private ArrayList<String> categorias;

    public Receta(String nombre, String link, int id, ArrayList<String> categorias) {
        this.nombre = nombre;
        this.link = link;
        this.id = id;
        this.categorias = categorias;
    }

    public Receta(String nombre, String link, ArrayList<String> categorias) {
        this.nombre = nombre;
        this.link = link;
        this.categorias = categorias;
        this.id = 0;
    }

    public Receta() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(ArrayList<String> categorias) {
        this.categorias = categorias;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
