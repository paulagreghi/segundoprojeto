package com.example.ana.segundoprojeto;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ana.segundoprojeto.modelo.Medida;
import com.example.ana.segundoprojeto.persistencia.DatabaseHelper;
import com.example.ana.segundoprojeto.utils.UtilsGUI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.ana.segundoprojeto.PessoasActivity.REQUEST_NOVA_PESSOA;

public class MedidasActivity extends AppCompatActivity {

    private ListView listViewMedidas;
    private ArrayAdapter<Medida> listaAdapter;

    List<Medida> lista = new ArrayList<Medida>();

    protected static final int REQUEST_NOVA_MEDIDA    = 1;
    private static final int REQUEST_ALTERAR_MEDIDA = 2;

    private int posicaoSelecionada = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_medidas);
        listViewMedidas = (ListView) findViewById (R.id.listViewMedidas);

        listViewMedidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Medida medida = (Medida) parent.getItemAtPosition(position);

                MedidaActivity.alterar(MedidasActivity.this
                        ,REQUEST_ALTERAR_MEDIDA, medida);
            }
        });

        listViewMedidas.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listViewMedidas.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {

                boolean selecionado = listViewMedidas.isItemChecked(position);

                View view = listViewMedidas.getChildAt(position);

                if (selecionado){
                    view.setBackgroundColor(Color.LTGRAY);
                }else{
                    view.setBackgroundColor(Color.TRANSPARENT);
                }

                int totalSelecionados = listViewMedidas.getCheckedItemCount();

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

                if (listViewMedidas.getCheckedItemCount() > 1){
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
                        for (int posicao = listViewMedidas.getChildCount(); posicao >= 0; posicao--){
                            if (listViewMedidas.isItemChecked(posicao)){
                                MedidaActivity.alterar(MedidasActivity.this,
                                        REQUEST_ALTERAR_MEDIDA,
                                        (Medida) listViewMedidas.getItemAtPosition(posicao));
                            }
                        }
                        mode.finish();
                        return true;
                    case R.id.menuItemExcluir:
                        for (int posicao = listViewMedidas.getChildCount(); posicao >= 0; posicao--){
                            if (listViewMedidas.isItemChecked(posicao)){
                                excluirMedida((Medida)listViewMedidas.getItemAtPosition(posicao));
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

                for (int posicao = 0; posicao < listViewMedidas.getChildCount(); posicao++){

                    View view = listViewMedidas.getChildAt(posicao);
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });


        popularLista();

        registerForContextMenu(listViewMedidas);
    }

    private void popularLista(){



        try {
            DatabaseHelper conexao = DatabaseHelper.getInstance(this);

            lista = conexao.getMedidaDao()
                    .queryBuilder()
                    .orderBy("data", true)
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        listaAdapter = new ArrayAdapter<Medida>(this,
                android.R.layout.simple_list_item_1,
                lista);

        listViewMedidas.setAdapter(listaAdapter);
    }

    private void excluirMedida(final Medida medida){

        String mensagem = getString(R.string.deseja_realmente_apagar)
                + "\n" + medida.getData ();

        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:

                                try {
                                    DatabaseHelper conexao =
                                            DatabaseHelper.getInstance(MedidasActivity.this);

                                    conexao.getMedidaDao().delete(medida);

                                    listaAdapter.remove(medida);

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

        if ((requestCode == REQUEST_NOVA_MEDIDA || requestCode == REQUEST_ALTERAR_MEDIDA)
                && resultCode == Activity.RESULT_OK){

            popularLista();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_pessoas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemNovo:
                MedidaActivity.nova(this, REQUEST_NOVA_MEDIDA);
                return true;
            case R.id.menuItemCadastrarPessoas:
                PessoaActivity.nova(this, REQUEST_NOVA_PESSOA);
                return true;
            case R.id.menuItemPessoasCadastradas:
                Intent intent = new Intent(this, PessoasActivity.class);
                startActivity(intent);
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
