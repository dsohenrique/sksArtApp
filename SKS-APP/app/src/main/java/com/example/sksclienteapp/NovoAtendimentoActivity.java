package com.example.sksclienteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.sksclienteapp.model.NovoAtendimento;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class NovoAtendimentoActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextInputEditText nomeCliente;
    RadioButton cabelo;
    RadioButton barba;
    RadioButton all;
    EditText data;
    EditText hora;
    Button addAtendimento;

    Connection con;
    String un,pass,db,ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_atendimento);

        progressBar =   (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        nomeCliente =   (TextInputEditText) findViewById(R.id.nomeCliente);
        cabelo =        (RadioButton) findViewById(R.id.optionCabelo);
        barba =         (RadioButton) findViewById(R.id.optionBarba);
        all =           (RadioButton) findViewById(R.id.optionAll);
        data =          (EditText) findViewById(R.id.data);
        hora =          (EditText) findViewById(R.id.hora);
        addAtendimento =(Button) findViewById(R.id.addAtendimento);

        // Declaring Server ip, username, database name and password
        ip = "sql5047.site4now.net";
        db = "DB_A50503_sks";
        un = "DB_A50503_sks_admin";
        pass = "sks123456";
        // Declaring Server ip, username, database name and password

        addAtendimento.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String nome = nomeCliente.getText().toString();
                String dataAtendimento = data.getText().toString();
                String horaAtendimento = hora.getText().toString();
                double valor = 15.0;
                String tipoAtendimento;

                if(all.isChecked()){
                    tipoAtendimento = "Completo Barba e Cabelo";
                    valor = 27.5;
                }
                else if(barba.isChecked())
                    tipoAtendimento = "Barba";
                else
                    tipoAtendimento = "Cabelo";

                NovoAtendimento novoAtendimento = new NovoAtendimento(
                        nome,
                        tipoAtendimento,
                        dataAtendimento,
                        horaAtendimento,
                        "Aguardando confirmação",
                        valor
                );

                new AddAtendimento().execute(novoAtendimento);
            }
        });
    }
    public class AddAtendimento extends AsyncTask<NovoAtendimento,String,String>
    {
        String z = "";
        Boolean isSuccess = false;


        @Override
        protected void onPreExecute()

        {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r)
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(NovoAtendimentoActivity.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(NovoAtendimentoActivity.this , "Sua solicitação de atendimento foi enviada com sucesso!" , Toast.LENGTH_LONG).show();
                finish();
            }
        }
        @Override
        protected String doInBackground(NovoAtendimento... params)
        {
            String nomeCliente = params[0].nomeCliente;
            String data = params[0].data.concat(params[0].hora);
            String tipo = params[0].tipoAtendimento;
            double valor = params[0].valor;
            String status = params[0].status;
            if(!nomeCliente.isEmpty() && !params[0].data.isEmpty() && !params[0].hora.isEmpty()) {
                try {
                    con = Connectionclass(un, pass, db, ip);// Connect to database
                    if (con == null) {
                        z = "Conexão  null!";
                    } else {
                        String query = "INSERT INTO atendimentos(nomeCliente, valor, data, tipoAtendimento, status) VALUES (" + "'" + nomeCliente + "'" + "," + "'" + valor + "'" + "," + "'" + data + "'" + "," + "'" + tipo + "'" + "," + "'" + status + "'" + ")";
                        // String query = "INSERT INTO login(login, password) VALUES ('"+nomeCliente+"','123123123')";
                        Statement stmt = con.createStatement();
                        int rowsAfected = stmt.executeUpdate(query);
                        if (rowsAfected > 0) {
                            isSuccess = true;
                            con.close();
                        } else {
                            isSuccess = false;
                        }
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Algo deu errado, tente novamente!";
                }
            }
            else z = "Para poder atendê-lo corretamente você precisa preeencher todas as informações corretamente!";
            return z;
        }
    }
    @SuppressLint("NewApi")
    public Connection Connectionclass(String user, String password, String database, String server)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            //  ConnectionURL = "jdbc:jtds:sqlserver://sql5009.mywindowshosting.com;database=DB_A2C00B_login;user=DB_A2C00B_login_admin;password=login@123";
            // ConnectionURL = "jdbc:jtds:sqlserver://192.168.1.9;database=msss;instance=SQLEXPRESS;Network Protocol=NamedPipes";
            ConnectionURL = "jdbc:jtds:sqlserver://"+server+";database="+database+";user="+user+";password="+password+";";

            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }

    public String formatDateTime(String timeToFormat) {

        String finalDateTime = "";

        SimpleDateFormat iso8601Format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        Date date = null;
        if (timeToFormat != null) {
            try {
                date = iso8601Format.parse(timeToFormat);
            } catch (ParseException e) {
                date = null;
            }

            if (date != null) {
                long when = date.getTime();
                int flags = 0;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
                flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

                finalDateTime = android.text.format.DateUtils.formatDateTime(getApplicationContext(),
                        when + TimeZone.getDefault().getOffset(when), flags);
            }
        }
        return finalDateTime;
    }

}
