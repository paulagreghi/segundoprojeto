package com.example.ana.segundoprojeto.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ana.segundoprojeto.modelo.Medida;
import com.example.ana.segundoprojeto.modelo.Pessoa;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME    = "pessoas.db";
    private static final int    DB_VERSION = 1;

    private static DatabaseHelper instance;
    private Dao<Medida, Integer> medidaDao;
    private Dao<Pessoa, Integer> pessoaDao;

    public static DatabaseHelper getInstance(Context contexto){

        if (instance == null){
            instance = new DatabaseHelper(contexto);
        }

        return instance;
    }

    private DatabaseHelper(Context contexto) {
        super(contexto, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Medida.class);
            TableUtils.createTable(connectionSource, Pessoa.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "onCreate", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable (connectionSource, Medida.class, true);
            TableUtils.dropTable(connectionSource, Pessoa.class, true);

            onCreate(database, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "onUpgrade", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<Medida, Integer> getMedidaDao() throws SQLException {

        if (medidaDao == null) {
            medidaDao = getDao(Medida.class);
        }

        return medidaDao;
    }

    public Dao<Pessoa, Integer> getPessoaDao() throws SQLException {

        if (pessoaDao == null) {
            pessoaDao = getDao(Pessoa.class);
        }

        return pessoaDao;
    }
}
