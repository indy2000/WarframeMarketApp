package com.fukajima.warframemarket.components;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fukajima.warframemarket.R;

import java.util.ArrayList;
import java.util.List;

public class DialogCustomSpinner<T> extends Dialog {
    private ListView lsvValores;
    private NovoStringAdapter<T> itens;
    private TextView lblNovoValor;
    private boolean insert;
    private DialogCustomSpinner.OnItemSelectedListener callBack;

    DialogCustomSpinner(Context context, NovoStringAdapter<T> itens, DialogCustomSpinner.OnItemSelectedListener callBack) {
        super(context);
        this.itens = itens;
        this.callBack = callBack;
        onCreate();
    }

    @Override
    public void setTitle(CharSequence title) {

        int value = getWindow().getWindowManager().getDefaultDisplay().getWidth() / 40;
        TextView titulo = (TextView) findViewById(R.id.custon_spinner_lblTitulo);

        titulo.setTextSize(value);
        titulo.setText(title);
        titulo.setVisibility(View.VISIBLE);
    }

    public void setTitle(Drawable imagem) {
        ImageView titulo = (ImageView) findViewById(R.id.custon_spinner_imgImagem);

        titulo.setImageDrawable(imagem);
        titulo.setVisibility(View.VISIBLE);
    }

    private void onCreate() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_spinner);

        lblNovoValor = (TextView) findViewById(R.id.custon_spinner_lblNovoValor);
        lblNovoValor.setVisibility(isInsert() ? View.VISIBLE : View.GONE);
        lblNovoValor.setOnClickListener(lblNovoValorOnClick());

        lsvValores = (ListView) findViewById(R.id.custon_spinner_lsvValores);
        lsvValores.setAdapter(itens);
        lsvValores.setOnItemClickListener(lsvValores_OnItemClick());

        ((EditText) findViewById(R.id.custon_spinner_txtBusca)).addTextChangedListener(txtBusca_TextWatcher());
    }

    private View.OnClickListener lblNovoValorOnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inserir = ((EditText) findViewById(R.id.custon_spinner_txtBusca)).getText().toString();
                for (int i = 0; i < itens.getCount(); i++)
                    if (itens.getDescricao(i).equals(inserir)) {
                        Toast.makeText(getContext(), getContext().getString(R.string.item_ja_existe), Toast.LENGTH_LONG).show();
                        return;
                    }

                if (inserir.isEmpty()) {
                    Toast.makeText(getContext(), getContext().getString(R.string.necessario_informar_item), Toast.LENGTH_LONG).show();
                } else {
                    callBack.onAdd(inserir);
                    ((EditText) findViewById(R.id.custon_spinner_txtBusca)).setText(inserir);
                }
            }
        };
    }

    private AdapterView.OnItemClickListener lsvValores_OnItemClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int retorno = -1;

                if (itens.getCount() == parent.getCount()) {
                    retorno = position;
                } else {
                    for (int i = 0; i < itens.getCount(); i++)
                        if (i != itens.getHintPosition())
                            if (itens.getItemId(i) == parent.getItemIdAtPosition(position)) {
                                retorno = i;
                                break;
                            }
                }

                callBack.onItemSelected(retorno);
                dismiss();
            }
        };
    }

    private TextWatcher txtBusca_TextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String busca = ((TextView) findViewById(R.id.custon_spinner_txtBusca)).getText().toString();
                try {
                    List<T> listaRetorno = (List<T>) callBack.onSearch(busca, itens.getAll(), lsvValores);

                    /*List<T> valores = new ArrayList<>();

                    for (int i = 0; i < itens.getCount(); i++) {
                        if (itens.getDescricao(i).toUpperCase().contains(busca.toUpperCase()))
                            if (itens.getHintPosition() != i) valores.add(itens.getItem(i));
                    }

                    NovoStringAdapter<T> adapter = new NovoStringAdapter<>(getContext(), itens.getResource(), itens.getCampoChave(), itens.getCamposDescricao(), itens.getHint(), valores);*/
                    NovoStringAdapter<T> adapter = new NovoStringAdapter<>(getContext(), itens.getResource(), itens.getCampoChave(), itens.getCamposDescricao(), itens.getHint(), listaRetorno);
                    lsvValores.setAdapter(adapter);
                } catch (Exception ex) {

                    List<T> valores = new ArrayList<>();

                    for (int i = 0; i < itens.getCount(); i++) {
                        if (itens.getDescricao(i).toUpperCase().contains(busca.toUpperCase()))
                            if (itens.getHintPosition() != i) valores.add(itens.getItem(i));
                    }

                    NovoStringAdapter<T> adapter = new NovoStringAdapter<>(getContext(), itens.getResource(), itens.getCampoChave(), itens.getCamposDescricao(), itens.getHint(), valores);
                    lsvValores.setAdapter(adapter);
                }
            }
        };
    }

    public boolean isInsert() {
        return insert;
    }

    public void setInsert(boolean insert) {
        lblNovoValor.setVisibility(insert ? View.VISIBLE : View.GONE);
        this.insert = insert;
    }

    protected interface OnItemSelectedListener {
        void onItemSelected(int position);

        List<?> onSearch(String busca, List<?> lista, ListView setar) throws Exception;

        void onAdd(String valor);
    }
}

