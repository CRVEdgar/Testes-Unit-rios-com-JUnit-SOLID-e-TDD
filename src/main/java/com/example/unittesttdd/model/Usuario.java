package com.example.unittesttdd.model;

import java.util.Objects;

public class Usuario {
    private String name;
    private String matricula;
    private boolean emDebito;
    private int numeroDeEmprestimos;

    public Usuario() {
    }

    public Usuario(String name, String matricula, boolean emDebito, int numeroDeEmprestimos) {
        this.name = name;
        this.matricula = matricula;
        this.emDebito = emDebito;
        this.numeroDeEmprestimos = numeroDeEmprestimos;
    }

    public Usuario(String name, String matricula) {
        this.name = name;
        this.matricula = matricula;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public boolean isEmDebito() {
        return emDebito;
    }

    public void negativarCadastro() {
        this.emDebito = true;
    }

    public int getNumeroDeEmprestimos() {
        return numeroDeEmprestimos;
    }

    public void setNumeroDeEmprestimos(int numeroDeEmprestimos) {
        this.numeroDeEmprestimos = numeroDeEmprestimos;
    }

    public void realizarEmprestimos(){
        this.numeroDeEmprestimos++;
    }

    public void userDevolverLivro(){
        this.numeroDeEmprestimos--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return getMatricula().equals(usuario.getMatricula());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMatricula());
    }
}
