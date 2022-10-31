package com.example.unittesttdd.model.dto;

import com.example.unittesttdd.model.Livro;
import com.example.unittesttdd.model.Usuario;

import java.util.List;

public class LivroUser {

    Usuario usuario;

    List<Livro> livros;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }
}
