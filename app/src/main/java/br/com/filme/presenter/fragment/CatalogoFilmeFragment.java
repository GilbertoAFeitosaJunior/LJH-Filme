package br.com.filme.presenter.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.filme.R;
import br.com.filme.model.interfaces.Constants;
import br.com.filme.model.Filme;
import br.com.filme.presenter.FilmeBo;
import br.com.filme.presenter.adapter.CatalogoFilmeAdapter;
import br.com.filme.view.DetalhesFilmeActivity;
import mobi.stos.httplib.HttpAsync;
import mobi.stos.httplib.inter.FutureCallback;


public class CatalogoFilmeFragment extends Fragment implements Constants {

    private ListView listViewFilmes;
    private CatalogoFilmeAdapter adapter;
    private List<Filme> filmeList;


    private FilmeBo filmeBo;

    private boolean hasMore;
    private int page;
    private boolean loading;
    private Context context;
    private ProgressDialog progressDialog;
    boolean continuar, opc;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        continuar = true;

        hasMore = true;
        page = 0;
        opc = true;

        context = getContext();
        filmeBo = new FilmeBo(context);
        filmeList = new ArrayList<>();

        getReadInServer();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_catalogo_filme, null);
        listViewFilmes = (ListView) view.findViewById(R.id.listViewFilmes);


        listViewFilmes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Filme filme = filmeList.get(position);
                Intent intent = new Intent(context, DetalhesFilmeActivity.class);
                intent.putExtra("filme", filme);
                startActivity(intent);
            }
        });

        listViewFilmes.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (loading && hasMore) {
                    if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
                        loading = false;
                        page++;
                        getReadInServer();
                    }
                }
            }
        });

        return view;
    }


    private void getReadInServer() {
        try {

            String url = getString(R.string.url_catalogo_filme) + "page=" + page + "&size=" + TAMANHO_LISTA;
            HttpAsync httpAsync = new HttpAsync(new URL(url));
            httpAsync.setDebug(true);

            httpAsync.get(new FutureCallback() {
                @Override
                public void onBeforeExecute() {
                    if (continuar) {
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage(getString(R.string.carregando));
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(true);
                        progressDialog.show();
                        continuar = false;
                    }
                }

                @Override
                public void onSuccess(int responseCode, Object object) {
                    if (responseCode == 200) {
                        try {
                            JSONArray jsonArray = (JSONArray) object;

                            if (jsonArray.length() == 0) {
                                hasMore = false;
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Filme filme = new Filme();
                                filme.set_id(jsonObject.getString("_id"));
                                filme.setTitulo(jsonObject.getString("name"));
                                filme.setImagem(jsonObject.getString("url"));

                                filmeList.add(filme);
                            }


                            filmeBo.clean();
                            filmeBo.insert(filmeList);
                            createAdapter();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onAfterExecute() {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        continuar = false;
                    }

                }

                @Override
                public void onFailure(Exception exception) {

                    if(opc){
                        Toast.makeText(context, R.string.erro_internet, Toast.LENGTH_SHORT).show();
                        filmeList = filmeBo.list();
                        createAdapter();
                    }

                    exception.getMessage();
                }

            });
        } catch (Exception e) {
            e.getMessage();
        }
    }


    private synchronized void createAdapter() {

        if (page == 0) {
            filmeList = filmeBo.list();
            adapter = new CatalogoFilmeAdapter(filmeList, context);
            listViewFilmes.setAdapter(adapter);
        } else {
            for (Filme filme : filmeList) {
                if (!adapter.contains(filme)) {
                    adapter.add(filme);
                }
            }
            adapter.notifyDataSetChanged();
        }
        loading = true;
        if(opc){
            opc =false;
        }

    }

}
