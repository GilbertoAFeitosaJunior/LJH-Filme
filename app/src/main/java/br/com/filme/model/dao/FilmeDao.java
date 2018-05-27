package br.com.filme.model.dao;

import android.content.Context;

import br.com.filme.model.Filme;
import mobi.stos.podataka_lib.repository.AbstractRepository;

public class FilmeDao extends AbstractRepository<Filme> {

    public FilmeDao(Context context) {
        super(context, Filme.class);
    }
}
