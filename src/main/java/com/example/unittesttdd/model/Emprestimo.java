package com.example.unittesttdd.model;

import java.time.LocalDateTime;
import java.util.List;

public class Emprestimo {

    private Usuario user;
    private LocalDateTime dataEmprestimo;
    private LocalDateTime dataPrevista;
    private LocalDateTime dataDevolucao;
    private List<Livro> livros;
    private Double valorEmprestimo;

    public Emprestimo(Usuario user, LocalDateTime dataEmprestimo,
                      LocalDateTime dataDevolucao, List<Livro> livros, Double valorEmprestimo) {
        this.user = user;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
        this.livros = livros;
        this.valorEmprestimo = valorEmprestimo;
    }

    public Emprestimo(Usuario user) {
        this.user = user;
    }

    public Emprestimo() {
    }

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

    public void addLivro(Livro livro){
        this.livros.add(livro);
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
