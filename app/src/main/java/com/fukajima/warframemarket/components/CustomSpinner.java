package com.fukajima.warframemarket.components;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;


import com.fukajima.warframemarket.R;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinner extends AppCompatSpinner {

    private int MODE_DIALOG = 0;
    private int MODE_DROPDOWN = 1;
    private int MODE_THEME = -1;

    private String dialogTitulo = "";
    private int dialogImagem = 0;
    private String hint = "";
    private boolean insert = false;
    private CustomSpinner.OnItemSearch callBack;
    private CustomSpinner.OnItemSelected callBackItemSelected;
    private OnNewValue callBackNew;
    private DialogCustomSpinner lDialog = null;

    private int mode = MODE_DIALOG;
    private boolean searchDialog = true;

    public CustomSpinner(Context context) {
        this(context, null);
    }

    public CustomSpinner(Context context, int mode) {
        this(context, null, R.attr.spinnerStyle, mode);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.spinnerStyle);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        this(context, attrs, defStyleAttr, mode, null);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
        if (mode == MODE_THEME) {
            TypedArray aa = null;
            try {
                //Logica antiga
                //int[] ATTRS_ANDROID_SPINNERMODE = {android.R.attr.spinnerMode};
                //Descomentar linha de cima e passar ATTRS_ANDROID_SPINNERMODE no lugar do "new int[]{android.R.attr.spinnerMode}"
                aa = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.spinnerMode}, defStyleAttr, 0);
                if (aa.hasValue(0)) {
                    mode = aa.getInt(0, MODE_DIALOG);
                }
            } catch (Exception e) {

            } finally {
                if (aa != null) {
                    aa.recycle();
                }
            }
        }
        this.mode = mode;
        this.iniciar(context, attrs);
    }

    private void iniciar(Context context, AttributeSet attrs) {
        try {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomSpinner);
            hint = a.hasValue(R.styleable.CustomSpinner_hintText) ? a.getString(R.styleable.CustomSpinner_hintText) : "";
            if (a.hasValue(R.styleable.CustomSpinner_searchDialog)) {
                searchDialog = a.getBoolean(R.styleable.CustomSpinner_searchDialog, true);
            }

            if(a.hasValue(R.styleable.CustomSpinner_titleText)) {
                dialogTitulo = a.getString(R.styleable.CustomSpinner_titleText);
            }

            a.recycle();
        } catch (Exception e) {
            hint = "";
        }

        this.setAdapter(null, null, new ArrayList<>());
        if (isInEditMode()) {
            ArrayList<String> lista = new ArrayList<>();
            if (!hint.isEmpty()) lista.add(hint);

            this.setAdapter(null, null, lista);
            if (lista.size() > 0) setSelection(0);
        }
    }

    public Object getSelectedItem(boolean showException) throws Exception {
        Object retorno = null;

        int hintPosition = ((NovoStringAdapter<String>) this.getAdapter()).getHintPosition();
        int position = this.getSelectedItemPosition();

        if ((hint.isEmpty()) || ((!hint.isEmpty()) && (hintPosition != position))) {

            retorno = super.getSelectedItem();
        } else {
            if (showException) {
                String mensagem = String.format(getContext().getString(R.string.eh_necesario_selecionar), hint);
                throw new Exception(mensagem);
            }
        }

        return retorno;
    }

    public void setHint(String value) {
        this.hint = value;
        NovoStringAdapter adapter = (NovoStringAdapter) getAdapter();
        this.setAdapter(adapter.getCampoChave(), adapter.getCamposDescricao(), adapter.getAll());

    }

    private void setHintListaVazia() {
        NovoStringAdapter adapter = (NovoStringAdapter) getAdapter();
        adapter.setHintListaVazia();
    }

    public String getHint() {
        return this.hint;
    }

    public void setAdapter(String chave, String[] descricao, List<?> objects) {

        if (objects == null) objects = new ArrayList<Object>();
        if (!isEnabled()) setEnabled(true);
        final NovoStringAdapter<?> adapter = new NovoStringAdapter<>(getContext(), R.layout.custom_spinner_layout, chave, descricao, hint, objects);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        adapter.setResourceDescricao(R.id.CustomSpinnerLayout_txtTexto, new NovoStringAdapter.onCreate() {
            @Override
            public void onCreateView(int position, View view) {
                boolean mostrarTitulo = (adapter.getHintPosition() != position) || ((!adapter.hasValue()) && (adapter.getHintPosition() == position));
                ((TextView) view.findViewById(R.id.CustomSpinnerLayout_txtTitulo)).setText(mostrarTitulo ? hint : "");
            }
        });

        this.setAdapter(adapter);
    }

    public void setAdapter(String chave, String[] descricao, List<?> objects, Boolean habilitado) {

        if (objects == null) objects = new ArrayList<Object>();
        if (!isEnabled() && habilitado) setEnabled(true);
        final NovoStringAdapter<?> adapter = new NovoStringAdapter<>(getContext(), R.layout.custom_spinner_layout, chave, descricao, hint, objects);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        adapter.setResourceDescricao(R.id.CustomSpinnerLayout_txtTexto, new NovoStringAdapter.onCreate() {
            @Override
            public void onCreateView(int position, View view) {
                boolean mostrarTitulo = (adapter.getHintPosition() != position) || ((!adapter.hasValue()) && (adapter.getHintPosition() == position));
                ((TextView) view.findViewById(R.id.CustomSpinnerLayout_txtTitulo)).setText(mostrarTitulo ? hint : "");
            }
        });

        this.setAdapter(adapter);
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        this.settingAdapter = true;

        super.setAdapter(adapter);
        NovoStringAdapter<String> stringAdapter = ((NovoStringAdapter<String>) adapter);
        if (stringAdapter.getHintPosition() > -1)
            this.setSelection(stringAdapter.getHintPosition(), false);
    }

    public void setAdapter(SpinnerAdapter adapter, String titulo) {
        dialogTitulo = titulo;
        this.setAdapter(adapter);
    }

    public void setAdapter(SpinnerAdapter adapter, int imagem) {
        dialogImagem = imagem;
        this.setAdapter(adapter);
    }

    private Boolean doOnItemSelectedOnSetAdapter = true;

    public void setDoOnItemSelectedOnSetAdapter(Boolean value) {
        doOnItemSelectedOnSetAdapter = value;
    }

    private Boolean settingAdapter = false;

    public Boolean isSettingAdapter() {
        return settingAdapter;
    }

    @Override
    public void setOnItemSelectedListener(@Nullable final OnItemSelectedListener listener) {
        super.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if ((!doOnItemSelectedOnSetAdapter) && (settingAdapter)) return;
                    if (listener != null) listener.onItemSelected(adapterView, view, i, l);
                } finally {
                    settingAdapter = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                try {
                    if ((!doOnItemSelectedOnSetAdapter) && (settingAdapter)) return;
                    if (listener != null) listener.onNothingSelected(adapterView);
                } finally {
                    settingAdapter = false;
                }
            }
        });
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener l) {
        super.setOnItemClickListener(l);
    }

    @Override
    public boolean performClick() {
        if (/*(mode != MODE_DROPDOWN) && */(searchDialog)) {
            boolean handled = false;
            if (!handled) {
                handled = true;

                Context context = getContext();
                NovoStringAdapter<?> cAdapter = (NovoStringAdapter<?>) getAdapter();
                NovoStringAdapter<?> adapter = new NovoStringAdapter<>(context, R.layout.simple_spinner_dropdown_item, cAdapter.getCampoChave(), cAdapter.getCamposDescricao(), hint, cAdapter.getAll());
                if (adapter.getCount() > 1 || this.isInsert()) {
                    lDialog = new DialogCustomSpinner(context, adapter, dialog_callback());
                    lDialog.setInsert(isInsert());

                    if (!dialogTitulo.isEmpty())
                        lDialog.setTitle(dialogTitulo);
                    else if (dialogImagem > 0)
                        lDialog.setTitle(context.getResources().getDrawable(dialogImagem));
                    else lDialog.setTitle(getContext().getString(R.string.select));

                    lDialog.show();
                }
            }
            return handled;
        } else {
            return super.performClick();
        }
    }

    public void setOnSearch(CustomSpinner.OnItemSearch busca) {
        callBack = busca;
    }

    public void setOnItemSelected(CustomSpinner.OnItemSelected itemSelecionado) {
        callBackItemSelected = itemSelecionado;

        if(!searchDialog) {
            this.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                    try {
                        callBackItemSelected.onItemSelected(pos);
                    } finally {

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    private DialogCustomSpinner.OnItemSelectedListener dialog_callback() {
        return new DialogCustomSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                setSelection(position);
                callBackItemSelected.onItemSelected(position);
            }

            @Override
            public List<?> onSearch(String busca, List<?> lista, ListView setar) throws Exception {
                return callBack.onSearch(busca, lista, setar);

            }

            @Override
            public void onAdd(String valor) {
                callBackNew.onAdd(valor);

                if (lDialog != null) {
                    setSelection(0);

                    lDialog.dismiss();
                    lDialog = null;
                }
            }
        };
    }

    public boolean isInsert() {
        return insert;
    }

    public void setInsert(boolean insert) {
        this.insert = insert;
    }

    public boolean isSearcheable() {
        return searchDialog;
    }

    public void setSearcheable(boolean searchDialog) {
        this.searchDialog = searchDialog;
    }

    public void setOnNewValue(OnNewValue callBackNew) {
        this.callBackNew = callBackNew;
    }

    public interface OnItemSearch {
        List<?> onSearch(String busca, List<?> lista, ListView setar) throws Exception;
    }

    public interface OnItemSelected {
        int onItemSelected(int position);
    }

    public interface OnNewValue {
        void onAdd(String valor);
    }

    public List<?> getAll() {
        List<?> lista = null;
        try {
            lista = ((NovoStringAdapter<?>) getAdapter()).getAll();
        } catch (Exception ignore) {
        }
        return lista;
    }

}

