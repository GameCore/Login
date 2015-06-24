package com.example.login;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends ActionBarActivity {

    String txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void enviar(View vista) throws IOException {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            TextView conexion = (TextView) findViewById(R.id.conexion);
            conexion.setText("Hay Conexion a Internet");
        } else {
            TextView conexion = (TextView) findViewById(R.id.conexion);
            conexion.setText("No hay Conexion a Interne");
        }
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    //Your code goes here
                    txt = executeHttpPost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

       // String txt = metodoGet("http://test.anakel.mx/utils/webservice.php?UID=login&correo=test@gmail.com&password=123456&idDevice=PRUEBA01&arg1=Droid&arg2=JellyBean&arg3=IMEI");

        displayMessange(txt);

       /* String query =  executeHttpPost("http://test.anakel.mx/utils/webservice.php");
        displayMessange(query);*/

    }

    public void displayMessange(String message) {
        TextView query = (TextView) findViewById(R.id.query);
        query.setText(message);
    }

    public String metodoGet(String direccion){
        URL url = null;
        try {
        url = new URL(direccion);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String resultado = convertStreamToString(in);
            return resultado;




        }
        catch(Exception e){return "Error";}


    }





    public String executeHttpPost() {
        try

        {

            EditText user = (EditText) findViewById(R.id.user);
            EditText pass = (EditText) findViewById(R.id.password);
            String usuario = user.getText().toString();
            String contraseña = pass.getText().toString();

            PeticionPost post = new PeticionPost("http://test.anakel.mx/utils/webservice.php");
            post.add("UID", "login");
            post.add("correo", usuario);
            post.add("password", contraseña);
            post.add("idDevice", "PRUEBA01");
            post.add("arg1", "Droid");
            post.add("arg2", "JellyBean");
            post.add("arg3", "IMEI");
            String respuesta = post.getRespueta();
            return respuesta;
        } catch (Exception e) {
            return "Error:  " +e.toString() + "";
        }

    }





    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    public String readFully(InputStream entityResponse) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = entityResponse.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString();
    }




    }