package com.example.ana.segundoprojeto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import static com.example.ana.segundoprojeto.MedidasActivity.REQUEST_NOVA_MEDIDA;
import static com.example.ana.segundoprojeto.PessoasActivity.REQUEST_NOVA_PESSOA;

public class ConfiguracoesActivity extends AppCompatActivity {

    SeekBar seekBarRed,seekBarGreen,seekBarBlue;
    RadioGroup idForRadioGroup;
    TextView textR,textG,textB;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean bar=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        idForRadioGroup=(RadioGroup)findViewById(R.id.idForRadioGroup);
        RadioButton rButton = (RadioButton) idForRadioGroup.getChildAt(0);
        rButton.setChecked(true);

        RadioButton rButton2 = (RadioButton) idForRadioGroup.getChildAt(1);
        rButton2.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences("key_clr", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        textR=(TextView)findViewById(R.id.textR);
        textG=(TextView)findViewById(R.id.textG);
        textB=(TextView)findViewById(R.id.textB);

        seekBarRed=(SeekBar)findViewById(R.id.seekBarRed);
        seekBarGreen=(SeekBar)findViewById(R.id.seekBarGreen);
        seekBarBlue=(SeekBar)findViewById(R.id.seekBarBlue);
        getClr();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rButton2.setVisibility(View.VISIBLE);
            initStatusBarClr();
        }

        idForRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio0:

                        bar=true;
                        getClr();
                        break;
                    case R.id.radio1:

                        bar=false;
                        getClr();
                        break;
                }
            }
        });


        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textR.setText("= "+String.valueOf(progress));
                putClr();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textG.setText("= "+String.valueOf(progress));
                putClr();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textB.setText("= "+String.valueOf(progress));
                putClr();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    public void ActionBarClr(){
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable (Color.rgb(seekBarRed.getProgress()
                        ,seekBarGreen.getProgress(),seekBarBlue.getProgress())));
    }

    public void StatusBarClr(){
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(Color.rgb(seekBarRed.getProgress()
                    ,seekBarGreen.getProgress(),seekBarBlue.getProgress()));
        }
    }

    public void initStatusBarClr(){

        int r=sharedPreferences.getInt("s_r",0);
        int g=sharedPreferences.getInt("s_g",0);
        int b=sharedPreferences.getInt("s_b",0);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(r,g,b));
        }

    }

    public void getClr(){
        if(bar) {
            int r=sharedPreferences.getInt("a_r",0);
            int g=sharedPreferences.getInt("a_g",0);
            int b=sharedPreferences.getInt("a_b",0);

            seekBarRed.setProgress(r);
            seekBarGreen.setProgress(g);
            seekBarBlue.setProgress(b);

            textR.setText("= "+String.valueOf(r));
            textG.setText("= "+String.valueOf(g));
            textB.setText("= "+String.valueOf(b));
            ActionBarClr();
        }else{
            int r=sharedPreferences.getInt("s_r",0);
            int g=sharedPreferences.getInt("s_g",0);
            int b=sharedPreferences.getInt("s_b",0);

            seekBarRed.setProgress(r);
            seekBarGreen.setProgress(g);
            seekBarBlue.setProgress(b);

            textR.setText("= "+String.valueOf(r));
            textG.setText("= "+String.valueOf(g));
            textB.setText("= "+String.valueOf(b));
            StatusBarClr();
        }
    }

    public void putClr(){
        if (bar){
            editor.putInt("a_r",seekBarRed.getProgress());
            editor.putInt("a_g",seekBarGreen.getProgress());
            editor.putInt("a_b",seekBarBlue.getProgress());
            editor.commit();
            ActionBarClr();
        }else{
            editor.putInt("s_r",seekBarRed.getProgress());
            editor.putInt("s_g",seekBarGreen.getProgress());
            editor.putInt("s_b",seekBarBlue.getProgress());
            editor.commit();
            StatusBarClr();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.lista_configuracoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId ()) {

            case R.id.menuItemNovo:
                MedidaActivity.nova (this, REQUEST_NOVA_MEDIDA);
                return true;
            case R.id.menuItemCadastrarPessoas:
                PessoaActivity.nova (this, REQUEST_NOVA_PESSOA);
                return true;
            case R.id.menuItemPessoasCadastradas:
                Intent intent = new Intent (this, PessoasActivity.class);
                startActivity (intent);
                return true;
            case R.id.menuItemSobre:
                Intent intentSobre = new Intent (this, SobreActivity.class);
                startActivity (intentSobre);
                return true;
            default:
                return super.onOptionsItemSelected (item);
        }
    }

}
