package com.example.sksclienteapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sksclienteapp.model.Atendimento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
{
    // Declaring layout button, edit texts
    ProgressBar progressBar;
    FloatingActionButton novoAtendimento;
    ListView listaAtendimentos;
    private ArrayList<Atendimento> atendimentosArrayList;
    private SyncData.MyAppAdapter myAppAdapter;
    // End Declaring layout button, edit texts

    // Declaring connection variables
    Connection con;
    String un,pass,db,ip;
    String usernam,passwordd;
    //End Declaring connection variables

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting values from button, texts and progress bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        novoAtendimento = (FloatingActionButton ) findViewById(R.id.novoAtendimento);
        listaAtendimentos = (ListView) findViewById(R.id.listaAtendimentos);
        progressBar.setVisibility(View.VISIBLE);
        atendimentosArrayList = new ArrayList<Atendimento>();
        // End Getting values from button, texts and progress bar

        // Declaring Server ip, username, database name and password
        ip = "sql5047.site4now.net";
        db = "DB_A50503_sks";
        un = "DB_A50503_sks_admin";
        pass = "sks123456";
        // Declaring Server ip, username, database name and password

        // Calling Async Task
        SyncData orderData = new SyncData();
        orderData.execute("");

        novoAtendimento.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            startActivity(new Intent(MainActivity.this,NovoAtendimentoActivity.class));
            }
        });
    }

    private class SyncData extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                con = Connectionclass(un, pass, db, ip);// Connect to database
                if (con == null) {
                    //z = "Conexão  null!";TODO:Adicionar método para mensagens
                } else {
                    String query = "SELECT * FROM atendimentos";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null) {
                        while (rs.next()) {
                            try {
                                atendimentosArrayList.add(new Atendimento(
                                        rs.getString("nomeCliente"),
                                        rs.getString("tipoAtendimento"),
                                        rs.getString("data"),
                                        rs.getString("status"),
                                        rs.getDouble("valor")
                                        )
                                );
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        con.close();
                    } else {
                        //TODO: Adicionar tratamento para caso nenhum resultado seja retornado.
                    }
                }
            } catch (Exception ex) {
                //TODO: Adicionar tratamento para caso nenhum resultado seja retornado.
            }

            return "Deu bom";
        }

        @Override
        protected void onPostExecute(String r) {
            progressBar.setVisibility(View.GONE);
            try {
                myAppAdapter = new MyAppAdapter(atendimentosArrayList, MainActivity.this);
                listaAtendimentos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listaAtendimentos.setAdapter(myAppAdapter);
            } catch (Exception ex) {
            }
        }

        @SuppressLint("NewApi")
        public Connection Connectionclass(String user, String password, String database, String server) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection connection = null;
            String ConnectionURL = null;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                ConnectionURL = "jdbc:jtds:sqlserver://" + server + ";database=" + database + ";user=" + user + ";password=" + password + ";";

                connection = DriverManager.getConnection(ConnectionURL);
            } catch (SQLException se) {
                Log.e("error here 1 : ", se.getMessage());
            } catch (ClassNotFoundException e) {
                Log.e("error here 2 : ", e.getMessage());
            } catch (Exception e) {
                Log.e("error here 3 : ", e.getMessage());
            }
            return connection;
        }

        public class MyAppAdapter extends BaseAdapter {
            public class ViewHolder {
                TextView dataAtendimentoItem;
                TextView statusItem;
                TextView valorItem;
            }

            public List<Atendimento> atendimentos;

            public Context context;
            ArrayList<Atendimento> arraylist;

            private MyAppAdapter(List<Atendimento> apps, Context context) {
                this.atendimentos = apps;
                this.context = context;
                arraylist = new ArrayList<Atendimento>();
                arraylist.addAll(atendimentos);
            }

            @Override
            public int getCount() {
                return atendimentos.size();
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) // inflating the layout and initializing widgets
            {

                View rowView = convertView;
                ViewHolder viewHolder= null;
                if (rowView == null)
                {
                    LayoutInflater inflater = getLayoutInflater();
                    rowView = inflater.inflate(R.layout.list_content, parent, false);
                    viewHolder = new ViewHolder();
                    viewHolder.dataAtendimentoItem = (TextView) rowView.findViewById(R.id.dataAtendimentoItem);
                    viewHolder.valorItem = (TextView) rowView.findViewById(R.id.valorItem);
                    viewHolder.statusItem = (TextView) rowView.findViewById(R.id.statusItem);
                    rowView.setTag(viewHolder);
                }
                else
                {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                // here setting up names and images
                viewHolder.dataAtendimentoItem.setText(atendimentos.get(position).data+"");
                viewHolder.statusItem.setText(atendimentos.get(position).status+"");
                viewHolder.valorItem.setText(atendimentos.get(position).valor+"");

                return rowView;
            }
        }
    }
}
