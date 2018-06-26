package com.example.ana.segundoprojeto;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ana.segundoprojeto.modelo.Pessoa;
import com.example.ana.segundoprojeto.persistencia.DatabaseHelper;
import com.example.ana.segundoprojeto.utils.UtilsGUI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoasActivity extends AppCompatActivity {

    private ListView listViewPessoas;
    private ArrayAdapter<Pessoa> listaAdapter;



    protected static final int REQUEST_NOVA_PESSOA    = 1;
    private static final int REQUEST_ALTERAR_PESSOA = 2;

    private int posicaoSelecionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoas);

        listViewPessoas = (ListView) findViewById(R.id.listViewPessoas);

        listViewPessoas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Pessoa pessoa = (Pessoa) parent.getItemAtPosition(position);

                PessoaActivity.alterar(PessoasActivity.this,
                        REQUEST_ALTERAR_PESSOA,
                        pessoa);
            }
        });

        listViewPessoas.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listViewPessoas.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {

                boolean selecionado = listViewPessoas.isItemChecked(position);

                View view = listViewPessoas.getChildAt(position);

                if (selecionado){
                    view.setBackgroundColor(Color.LTGRAY);
                }else{
                    view.setBackgroundColor(Color.TRANSPARENT);
                }

                int totalSelecionados = listViewPessoas.getCheckedItemCount();

                if (totalSelecionados > 0){

                    mode.setTitle(getResources().getQuantityString(R.plurals.selecionado,
                            totalSelecionados,
                            totalSelecionados));
                }

                mode.invalidate();
            }

            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.principal_item_selecionado, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {

                if (listViewPessoas.getCheckedItemCount() > 1){
                    menu.getItem(0).setVisible(false);
                }else{
                    menu.getItem(0).setVisible(true);
                }

                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menuItemAlterar:
                        for (int posicao = listViewPessoas.getChildCount(); posicao >= 0; posicao--){
                            if (listViewPessoas.isItemChecked(posicao)){
                                PessoaActivity.alterar(PessoasActivity.this,
                                        REQUEST_ALTERAR_PESSOA,
                                        (Pessoa) listViewPessoas.getItemAtPosition(posicao));
                            }
                        }
                        mode.finish();
                        return true;
                    case R.id.menuItemExcluir:
                        for (int posicao = listViewPessoas.getChildCount(); posicao >= 0; posicao--){
                            if (listViewPessoas.isItemChecked(posicao)){
                                excluirPessoa((Pessoa) listViewPessoas.getItemAtPosition(posicao));
                            }
                        }
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {

                for (int posicao = 0; posicao < listViewPessoas.getChildCount(); posicao++){

                    View view = listViewPessoas.getChildAt(posicao);
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        popularLista();
        registerForContextMenu(listViewPessoas);
    }

    private void popularLista(){

        List<Pessoa> lista = null;

        try {
            DatabaseHelper conexao = DatabaseHelper.getInstance(this);

            lista = conexao.getPessoaDao()
                    .queryBuilder()
                    .orderBy("nome", true)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        listaAdapter = new ArrayAdapter<Pessoa>(this,
                android.R.layout.simple_list_item_1,
                lista);

        listViewPessoas.setAdapter(listaAdapter);
    }

    private void excluirPessoa(final Pessoa pessoa){

        String mensagem = getString(R.string.deseja_realmente_apagar)
                + "\n" + pessoa.getNome();

        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:

                                try {
                                    DatabaseHelper conexao =
                                            DatabaseHelper.getInstance(PessoasActivity.this);

                                    conexao.getPessoaDao().delete(pessoa);

                                    listaAdapter.remove(pessoa);

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

        UtilsGUI.confirmaAcao(this, mensagem, listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == REQUEST_NOVA_PESSOA || requestCode == REQUEST_ALTERAR_PESSOA)
                && resultCode == Activity.RESULT_OK){
            popularLista();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_pessoa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuItemNovo:
                PessoaActivity.nova(this, REQUEST_NOVA_PESSOA);
                return true;
            case R.id.menuItemConfiguracao:
                Intent intentConfiguracao = new Intent(this, ConfiguracoesActivity.class);
                startActivity(intentConfiguracao);
                return true;
            case R.id.menuItemSobre:
                Intent intentSobre = new Intent(this, SobreActivity.class);
                startActivity(intentSobre);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
