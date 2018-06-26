package com.example.ana.segundoprojeto;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.ana.segundoprojeto.modelo.Medida;
import com.example.ana.segundoprojeto.modelo.Pessoa;
import com.example.ana.segundoprojeto.persistencia.DatabaseHelper;
import com.example.ana.segundoprojeto.utils.UtilsGUI;
import com.example.ana.segundoprojeto.utils.UtilsString;

import java.sql.SQLException;
import java.util.List;

public class MedidaActivity extends AppCompatActivity {
    public static final String MODO    = "MODO";
    public static final String ID      = "ID";
    public static final int    NOVO    = 1;
    public static final int    ALTERAR = 2;

    private EditText editTextData;
    private EditText editTextPeso;
    private EditText editTextAltura;
    private Spinner spinnerPessoa;

    private List<Pessoa> listaPessoas;

    private int    modo;
    private Medida medida;


    public static void nova(Activity activity, int requestCode){

        Intent intent = new Intent(activity, MedidaActivity.class);

        intent.putExtra(MODO, NOVO);

        activity.startActivityForResult(intent, NOVO);
    }

    public static void alterar(Activity activity, int requestCode, Medida medida){

        Intent intent = new Intent(activity, MedidaActivity.class);

        intent.putExtra(MODO, requestCode);
        intent.putExtra(ID, medida.getId());

        activity.startActivityForResult(intent, requestCode);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_medida);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editTextPeso   = (EditText) findViewById(R.id.editTextPeso);
        editTextAltura = (EditText) findViewById(R.id.editTextAltura);
        editTextData = (EditText) findViewById (R.id.editTextData);
        spinnerPessoa = (Spinner) findViewById (R.id.spinnerPessoa);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        popularSpinner();

        modo = bundle.getInt(MODO);

        if (modo == ALTERAR){

            int id = bundle.getInt(ID);

            try {

                DatabaseHelper conexao = DatabaseHelper.getInstance(this);
                medida = conexao.getMedidaDao().queryForId(id);

                editTextAltura.setText(String.valueOf(medida.getAltura ()));
                editTextPeso.setText(String.valueOf(medida.getPeso ()));
                editTextData.setText (String.valueOf (medida.getData ()));

                conexao.getPessoaDao ().refresh (medida.getPessoa ());

            } catch (SQLException e) {
                e.printStackTrace();
            }

            int posicao = posicaoPessoa(medida.getPessoa());
            spinnerPessoa.setSelection(posicao);

            setTitle(R.string.alterar_medida);

        }else{

            medida = new Medida ();

            setTitle(R.string.nova_medida);
        }

    }

    private int posicaoPessoa(Pessoa pessoa){

        for (int pos = 0; pos < listaPessoas.size(); pos++){

            Pessoa t = listaPessoas.get(pos);

            if (t.getId() == pessoa.getId()){
                return pos;
            }
        }

        return -1;
    }

    private void popularSpinner(){

        listaPessoas = null;

        try {
            DatabaseHelper conexao = DatabaseHelper.getInstance(this);

            listaPessoas = conexao.getPessoaDao ()
                    .queryBuilder()
                    .orderBy(Pessoa.NOME, true)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayAdapter<Pessoa> spinnerAdapter = new ArrayAdapter<Pessoa>(this,
                android.R.layout.simple_list_item_1,
                listaPessoas);

        spinnerPessoa.setAdapter(spinnerAdapter);
    }

    private void salvar(){

//
//        String altura  = String.valueOf (Integer.parseInt (UtilsGUI.validaCampoTexto(this,
//                editTextAltura,
//                R.string.altura_vazio)));
//        if (altura == null){
//            return;
//        }
//
//        String peso = UtilsGUI.validaCampoTexto(this,
//                editTextPeso,
//                R.string.peso_vazio);
//        if (peso == null){
//            return;
//        }
//
//        String data = UtilsGUI.validaCampoTexto(this,
//                editTextData,R.string.data_vazio);
//        if (data == null){
//            return;
//        }
        String data  = UtilsGUI.validaCampoTexto(this,
                editTextData,
                R.string.data_vazio);
        if (data == null){
            return;
        }

        String peso  = UtilsGUI.validaCampoTexto(this,
                editTextPeso,
                R.string.peso_vazio);
        if (peso == null){
            return;
        }

        double pesoc = Double.parseDouble (String.valueOf (peso));

        String altura  = UtilsGUI.validaCampoTexto(this,
                editTextAltura,
                R.string.altura_vazio);
        if (altura == null){
            return;
        }
        int alturac = Integer.parseInt(altura);



        medida.setPeso(pesoc);
        medida.setAltura (alturac);
        medida.setData (data);


        Pessoa pessoa = (Pessoa) spinnerPessoa.getSelectedItem();

        if (pessoa != null){
            medida.setPessoa (pessoa);
        }


        try {

            DatabaseHelper conexao = DatabaseHelper.getInstance(this);

            if (modo == NOVO) {

                conexao.getMedidaDao().create(medida);

            } else {

                conexao.getMedidaDao().update(medida);
            }

            setResult(Activity.RESULT_OK);
            finish();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edicao_detalhes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemSalvar:
                salvar();
                return true;
            case R.id.menuItemCancelar:
                cancelar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
