package com.tecgus.segundoplano_comibge;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class SegundoPlano extends AsyncTask<String, Void, String> {

    MainActivity mainActivity = new MainActivity();


    private Context spContext;

    public SegundoPlano(Context context){
        spContext = context;
    }

    @Override
    protected void onPreExecute() {

        mainActivity.exibirProgresso(true);

    }

    @Override
    protected String doInBackground(String... params) {

        //estados
        if (params[0].equals("estado")){
            StringBuilder respostaEstados = new StringBuilder();

            try {
                URL urlEstados = new URL("https://servicodados.ibge.gov.br/api/v1/localidades/estados/");

                HttpURLConnection conexao = (HttpURLConnection) urlEstados.openConnection();

                conexao.setRequestMethod("GET");
                conexao.setRequestProperty("Content-type", "application/json");
                conexao.setDoOutput(true);
                conexao.setConnectTimeout(3000);
                conexao.connect();

                Scanner scanner = new Scanner(urlEstados.openStream());
                while (scanner.hasNext()){
                    respostaEstados.append(scanner.next());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return respostaEstados.toString();

            //cidades
        }else  if (params[0].equals("municipio")){
            StringBuilder respostaMunicipios = new StringBuilder();

            try {
                URL urlMunicipios = new URL("https://servicodados.ibge.gov.br/api/v1/localidades/estados/"+params[1] +"/municipios");

                HttpURLConnection conexao = (HttpURLConnection) urlMunicipios.openConnection();

                conexao.setRequestMethod("GET");
                conexao.setRequestProperty("Content-type", "application/json");
                conexao.setDoOutput(true);
                conexao.setConnectTimeout(3000);
                conexao.connect();

                Scanner scanner = new Scanner(urlMunicipios.openStream());
                while (scanner.hasNext()){
                    respostaMunicipios.append(scanner.next());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return respostaMunicipios.toString();

            //bairros
        }else  if (params[0].equals("subdistrito")){
            StringBuilder respostaSubdistritos = new StringBuilder();

            try {
                URL urlSubdistritos = new URL("https://servicodados.ibge.gov.br/api/v1/localidades/municipios/"+params[1] +"/subdistritos");

                HttpURLConnection conexao = (HttpURLConnection) urlSubdistritos.openConnection();

                conexao.setRequestMethod("GET");
                conexao.setRequestProperty("Content-type", "application/json");
                conexao.setDoOutput(true);
                conexao.setConnectTimeout(3000);
                conexao.connect();

                Scanner scanner = new Scanner(urlSubdistritos.openStream());
                while (scanner.hasNext()){
                    respostaSubdistritos.append(scanner.next());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return respostaSubdistritos.toString();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String dados) {
        mainActivity.exibirProgresso(false);
    }
}
