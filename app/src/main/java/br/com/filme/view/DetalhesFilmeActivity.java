package br.com.filme.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URL;

import br.com.filme.R;
import br.com.filme.model.Filme;
import mobi.stos.httplib.HttpAsync;
import mobi.stos.httplib.inter.FutureCallback;

public class DetalhesFilmeActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView, textView2;
    private Filme filme;
    private String description;

    private boolean continuar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        filme = (Filme) getIntent().getSerializableExtra("filme");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(filme.getTitulo());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        imageView = (ImageView) this.findViewById(R.id.imagem);
        textView = (TextView) this.findViewById(R.id.descricao);
        textView2 = (TextView) this.findViewById(R.id.titulo2);

        textView2.setText(filme.getTitulo());

        Picasso.get()
                .load(filme.getImagem())
                .placeholder(android.R.drawable.ic_menu_camera)
                .into(imageView);

        continuar = true;

        getReadInServer(filme.get_id());
    }


    private void getReadInServer(String _id) {
        try {
            String url = getString(R.string.url_detalhe_filme) + _id;
            HttpAsync httpAsync = new HttpAsync(new URL(url));
            httpAsync.setDebug(true);

            httpAsync.get(new FutureCallback() {
                @Override
                public void onBeforeExecute() {
                }

                @Override
                public void onSuccess(int responseCode, Object object) {

                    if (responseCode == 200) {
                        try {
                            JSONObject jsonObject = (JSONObject) object;
                            if (jsonObject != null) {
                                description = jsonObject.getString("description");
                                textView.setText(description);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onAfterExecute() {
                }

                @Override
                public void onFailure(Exception exception) {
                    exception.getMessage();
                }

            });
        } catch (Exception e) {
            e.getMessage();
        }
    }


}
