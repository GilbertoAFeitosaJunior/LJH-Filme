package br.com.filme.presenter;

import android.content.Context;

import br.com.filme.model.Filme;
import br.com.filme.model.dao.FilmeDao;
import mobi.stos.podataka_lib.interfaces.IOperations;
import mobi.stos.podataka_lib.service.AbstractService;

public class FilmeBo extends AbstractService<Filme> {

    private FilmeDao dao;

    public FilmeBo(Context context) {
        this.dao = new FilmeDao(context);
    }

    @Override
    protected IOperations<Filme> getDao() {
        return dao;
    }
}
