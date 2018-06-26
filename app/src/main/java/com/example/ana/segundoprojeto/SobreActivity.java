package com.example.ana.segundoprojeto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import static com.example.ana.segundoprojeto.MedidasActivity.REQUEST_NOVA_MEDIDA;
import static com.example.ana.segundoprojeto.PessoasActivity.REQUEST_NOVA_PESSOA;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_sobre);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_sobre, menu);
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
//            case R.id.menuItemSobre:
//                Intent intentSobre = new Intent(this, SobreActivity.class);
//                startActivity(intentSobre);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
