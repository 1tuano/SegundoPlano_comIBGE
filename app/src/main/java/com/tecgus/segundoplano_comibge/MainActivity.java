package com.tecgus.segundoplano_comibge;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tecgus.segundoplano_comibge.objetos.Estados;
import com.tecgus.segundoplano_comibge.objetos.Municipios;
import com.tecgus.segundoplano_comibge.objetos.Subdistrito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {


    private static ProgressBar load;
    Spinner spinnerEstados, spinnerMunicipios, spinnerSubDistritos;
    Municipios[] municipios = null;
    final ArrayList<String> subdistritosParaSpinner = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        load = findViewById(R.id.progressBar);
        //load.setVisibility(View.GONE);

        spinnerEstados = findViewById(R.id.spinner_estado);
        spinnerMunicipios = findViewById(R.id.spinnerMunicipios);
        spinnerMunicipios.setEnabled(false);
        spinnerSubDistritos = findViewById(R.id.spinnerSubdistritos);
        spinnerSubDistritos.setEnabled(false);

        String respostaEstados = executaSegundoPlano("estado");

        Gson gsonEstados = new GsonBuilder().setPrettyPrinting().create();

        final Estados[] estados = gsonEstados.fromJson(respostaEstados, Estados[].class);


        final ArrayList<String> estadosParaSpinner = new ArrayList<>();


        for (Estados estado: estados){
          estadosParaSpinner.add(estado.getNome());

        }


        //deixar o  spinner em ordem alfabetica
        Collections.sort(estadosParaSpinner);

        estadosParaSpinner.add(0,"selecione o estado");

        spinnerMunicipios.setEnabled(true);


        ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1,
                estadosParaSpinner);

        spinnerEstados.setAdapter(adapterEstados);



        spinnerEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

               for (Estados estado: estados){
                   if (estado.getNome().equals(spinnerEstados.getSelectedItem().toString())){
                       solicitaMunicipios(estado.getSigla());
                   }
               }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerMunicipios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                for (Municipios municipios: municipios){
                    if (municipios.getNome().equals(spinnerMunicipios.getSelectedItem().toString())){
                        solicitaSubDistritos(String.valueOf(municipios.getId()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void solicitaMunicipios(String siglaEstado) {

        String respostaMunicipios = executaSegundoPlano("municipio", siglaEstado);

        Gson gsonMunicipios = new GsonBuilder().setPrettyPrinting().create();

        municipios = gsonMunicipios.fromJson(String.valueOf(respostaMunicipios), Municipios[].class);

        final ArrayList<String> municipiosParaSpinner = new ArrayList<>();
        ArrayList<String> idMunicipios = new ArrayList<>();


        for (Municipios municipio: municipios){
            municipiosParaSpinner.add(municipio.getNome());
            idMunicipios.add(String.valueOf(municipio.getId()));
        }


        //deixar o  spinner em ordem alfabetica
        Collections.sort(municipiosParaSpinner);


        //exibir o item da lista do spinner
        municipiosParaSpinner.add(0,"Selecione o municipio");

        spinnerMunicipios.setEnabled(true);
        spinnerSubDistritos.setEnabled(false);
        subdistritosParaSpinner.add(0,"Selecione o subdistrito");
        


        ArrayAdapter<String> adapterMunicipios = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1,
                municipiosParaSpinner);

        spinnerMunicipios.setAdapter(adapterMunicipios);

    }

    private void solicitaSubDistritos(String idMunicipio) {

        String respostaSubdistrito = executaSegundoPlano("subdistrito", idMunicipio);

        Gson gsonSubdistritos = new GsonBuilder().setPrettyPrinting().create();

        Subdistrito[] subdistritos = gsonSubdistritos.fromJson(respostaSubdistrito, Subdistrito[].class);



        if (subdistritos.length >0){

            for (Subdistrito subdistrito: subdistritos){
                subdistritosParaSpinner.add(subdistrito.getNome());

            }

            //deixar o  spinner em ordem alfabetica
            Collections.sort(subdistritosParaSpinner);
            spinnerSubDistritos.setEnabled(true);
            subdistritosParaSpinner.add(0,"Selecione o subdistrito");

        }else{
            subdistritosParaSpinner.add(0,"Não existe subdistrito para esse municipio");
            spinnerSubDistritos.setEnabled(false);
        }





        ArrayAdapter<String> adapterSubdistritos = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1,
                subdistritosParaSpinner);

        spinnerSubDistritos.setAdapter(adapterSubdistritos);

    }

    private String executaSegundoPlano(String... params) {

        String respostaIBGE = null;

        SegundoPlano segundoPlano = new SegundoPlano(this);

        try {
            respostaIBGE = segundoPlano.execute(params).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return respostaIBGE;
    }

    public static void exibirProgresso(boolean exibir){

        load.setVisibility(false ? View.VISIBLE : View.GONE);
    }
}