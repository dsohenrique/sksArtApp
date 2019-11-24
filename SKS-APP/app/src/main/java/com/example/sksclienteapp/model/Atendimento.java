package com.example.sksclienteapp.model;

public class Atendimento {
    public String nomeCliente;
    public String tipoAtendimento;
    public String data;
    public String status;
    public Double valor;

    public Atendimento(String nomeCliente,String tipoAtendimento,String data,String status,Double valor){
        this.nomeCliente = nomeCliente;
        this.tipoAtendimento = tipoAtendimento;
        this.data = data;
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

    public String getStatus() {
        return status;
    }

    public Double getValor() {
        return valor;
    }
}
