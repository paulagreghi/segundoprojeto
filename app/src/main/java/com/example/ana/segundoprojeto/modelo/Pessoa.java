package com.example.ana.segundoprojeto.modelo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "pessoa")
public class Pessoa {

    public static final String ID        = "id";
    public static final String NOME = "nome";
    public static final String DATA_NASCIMENTO = "data_nascimento";


    @DatabaseField(generatedId=true)
    private int   id;

    @DatabaseField(canBeNull = false)
    private String nome;

    @DatabaseField
    private int    idade;

    public Pessoa(){

    }

    public Pessoa(String nome){
        setNome(nome);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    @Override
    public String toString(){
        return getNome();
    }
}
