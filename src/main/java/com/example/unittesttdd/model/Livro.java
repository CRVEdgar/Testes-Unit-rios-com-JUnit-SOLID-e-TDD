package com.example.unittesttdd.model;

import java.util.List;

public class Livro {

    private String autor;
    private String titulo;
    private boolean emprestado;
    private boolean reservado;
    private List<Emprestimo> historico;

    public String getAutor() {
        return autor;
    }

    public Livro(String autor, String titulo, boolean emprestado, boolean reservado, List<Emprestimo> historico) {
        this.autor = autor;
        this.titulo = titulo;
        this.emprestado = emprestado;
        this.reservado = reservado;
        this.historico = historico;
    }

    public Livro(String titulo, boolean emprestado, boolean reservado) {
        this.titulo = titulo;
        this.emprestado = emprestado;
        this.reservado = reservado;
    }

    public Livro() {
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isEmprestado() {
        return emprestado;
    }

    public void emprestar() {
        this.emprestado = true;
    }

    public boolean isReservado() {
        return reservado;
    }

    public void reservar( ) {
        this.reservado = true;
    }

    public void devolver(){
        this.reservado = false;
        this.emprestado = false;
    }

    public List<Emprestimo> getHistorico() {
        return historico;
    }

}
