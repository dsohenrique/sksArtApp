package com.example.sksclienteapp.model;

public class NovoAtendimento {
    public String nomeCliente;
    public String tipoAtendimento;
    public String data;
    public String hora;
    public String status;
    public Double valor;

    public NovoAtendimento(String nomeCliente,String tipoAtendimento,String data,String hora,String status,Double valor){
        this.nomeCliente = nomeCliente;
        this.tipoAtendimento = tipoAtendimento;
        this.data = data;
        this.hora = hora;
        this.status = status;
        this.valor = valor;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getTipo() {
        return tipoAtendimento;
    }

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    public String getStatus() {
        return status;
    }

    public Double getValor() {
        return valor;
    }
}
