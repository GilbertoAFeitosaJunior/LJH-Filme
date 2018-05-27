package br.com.filme.presenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.filme.R;
import br.com.filme.model.Filme;

public class CatalogoFilmeAdapter extends BaseAdapter {

    private List<Filme> lista;
    private LayoutInflater layoutInflater;
    private Context context;

    public CatalogoFilmeAdapter(List<Filme> lista, Context context) {
        this.lista = lista;
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.adapter_catalogo_filme, null);

        Filme filme = lista.get(position);

        ImageView imagem = (ImageView) view.findViewById(R.id.imagem);
        TextView titulo = (TextView) view.findViewById(R.id.titulo);

        Picasso.get()
                .load(filme.getImagem())
                .placeholder(android.R.drawable.ic_menu_camera)
                .into(imagem);
        titulo.setText(filme.getTitulo());


        return view;
    }



    @Override
    public int getCount() {
        return lista == null ? 0 : lista.size();
    }

    public boolean contains(Filme filme) {
        if (lista == null)
            return false;
        for (Filme entity : lista) {
            if (entity.getId() == filme.getId()) {
                return true;
            }
        }
        return false;
    }
    public void add(Filme Filme) {
        lista.add(Filme);
    }
}
