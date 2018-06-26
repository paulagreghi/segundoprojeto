package com.example.ana.segundoprojeto.modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.DateFormat;
import java.util.Date;

@DatabaseTable(tableName = "medidas")
public class Medida {
    public static final String ID = "id";
    public static final String DATA = "data";
    public static final String PESO = "peso";
    public static final String ALTURA = "altura";
    public static final String PESSOA = "pessoa";

    @DatabaseField(generatedId = true, canBeNull =  false, columnName = ID)
    private int id;

    @DatabaseField(columnName = DATA)
    private String data;

    @DatabaseField(columnName = PESO)
    private double peso;

    @DatabaseField(columnName = ALTURA)
    private int altura;

    @DatabaseField(foreign = true, canBeNull = false)
    Pessoa pessoa;

    public Medida(){

    }

    public static String getID() {
        return ID;
    }

    public static String getDATA() {
        return DATA;
    }

    public static String getPESO() {
        return PESO;
    }

    public static String getALTURA() {
        return ALTURA;
    }

    public static String getPESSOA() {
        return PESSOA;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public String toString() {
        return
                data;
    }
}
