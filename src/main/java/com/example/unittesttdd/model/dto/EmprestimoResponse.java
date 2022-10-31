package com.example.unittesttdd.model.dto;

import com.example.unittesttdd.model.Livro;
import com.example.unittesttdd.model.Usuario;

import java.time.LocalDateTime;
import java.util.List;

public class EmprestimoResponse {

    private Usuario user;
    private LocalDateTime dataEmprestimo;
    private LocalDateTime dataPrevista;
    private LocalDateTime dataDevolucao;
    private List<Livro> livros;
    private Double valorEmprestimo;

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public LocalDateTime getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDateTime dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDateTime getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista( ) {
        /** sempre será 7 dias após a data do empréstimo */
        this.dataPrevista = this.getDataEmprestimo().plusDays(7);
    }

    public LocalDateTime getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDateTime dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    public Double getValorEmprestimo() {
        return valorEmprestimo;
    }

    public void setValorEmprestimo(Double valorEmprestimo) {
        this.valorEmprestimo = valorEmprestimo;
    }
}
