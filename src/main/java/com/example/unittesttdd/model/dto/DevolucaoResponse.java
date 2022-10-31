package com.example.unittesttdd.model.dto;

import com.example.unittesttdd.model.Livro;
import com.example.unittesttdd.model.Usuario;

import java.time.LocalDateTime;
import java.util.List;

public class DevolucaoResponse {

    private int qtdLivrosComUser;
    private LocalDateTime dataDevolucao;
    private List<Livro> livrosDevolvidos;
    private Double valorASerPago;

    public int getQtdLivrosComUser() {
        return qtdLivrosComUser;
    }

    public void setQtdLivrosComUser(int qtdLivrosComUser) {
        this.qtdLivrosComUser = qtdLivrosComUser;
    }

    public LocalDateTime getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDateTime dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public List<Livro> getLivrosDevolvidos() {
        return livrosDevolvidos;
    }

    public void setLivrosDevolvidos(List<Livro> livrosDevolvidos) {
        this.livrosDevolvidos = livrosDevolvidos;
    }

    public Double getValorASerPago() {
        return valorASerPago;
    }

    public void setValorASerPago(Double valorASerPago) {
        this.valorASerPago = valorASerPago;
    }
}
