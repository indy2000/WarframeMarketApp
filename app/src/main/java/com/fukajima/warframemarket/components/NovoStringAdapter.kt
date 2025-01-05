package com.fukajima.warframemarket.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fukajima.warframemarket.R
import java.lang.reflect.Field

class NovoStringAdapter<T : Any> : ArrayAdapter<T> {
    protected var resourceDescricao: Int = 0
    protected var callback: onCreate? = null
    protected val _itens = mutableListOf<PropertyAdapter<T>>()

    var campoChave: String? = null
        get() = field
        protected set(value) {
            field = value
        }

    var camposDescricao: Array<String>? = null
        get() = field
        protected set(value) {
            field = value
        }

    var hint: String
        get() = field
        protected set(value) {
            field = value
        }

    var resource: Int
        get() = field
        protected set(value) {
            field = value
        }

    var hintPosition: Int = -1
        get() = field
        protected set(value) {
            field = value
        }

    fun hasValue() = _itens.firstOrNull { p ->
        p.objeto != null
    } != null

    fun setValue(values: List<T>) {
        _itens.addAll(values.map { PropertyAdapter(getDescricao(it), it) })
        this.setHint()
    }

    fun setHintListaVazia() {
        /*
        if (this.hintPosition >= 0) {
            _itens.removeAt(this.hintPosition)
        }
        _itens.add(this.hintPosition, PropertyAdapter("Lista Vazia", null))
         */
        notifyDataSetChanged()
    }

    private fun setHint(atualiza: Boolean = false) {
        if (hint.trim().isNotEmpty()) {
            this.hintPosition = _itens.size
            _itens.add(this.hintPosition, PropertyAdapter(hint.trim(), null))
        }
    }

    override fun add(value: T?) {
        value?.let {
            val consulta = if (campoChave == null) this._itens.firstOrNull { f -> f == value }
            else {
                var chave: Any? = null
                try {
                    var field: Field? = null
                    field = try {
                        value.javaClass.getDeclaredField(campoChave!!)
                    } catch (e: Exception) {
                        value.javaClass.superclass?.getDeclaredField(campoChave!!)
                    }

                    field?.isAccessible = true
                    chave = field?.get(value).toString()
                    field?.isAccessible = false
                } catch (e: Exception) {
                    chave = null
                }

                this._itens.firstOrNull { f ->
                    f.objeto?.javaClass?.getDeclaredField(campoChave!!) == chave
                }
            }

            if (consulta == null) {
                if (hintPosition > -1) this._itens.removeAt(hintPosition)
                this._itens.add(0, PropertyAdapter(/*getChave(value), */getDescricao(value), value))
                this.setHint(true)
                notifyDataSetChanged()
            }

        }
    }


    fun getDescricao(value: T?): String {
        if (this.hint.isNotEmpty() && (value == null)) return hint

        var retorno = ""
        try {
            for (campo: String in this.camposDescricao!!) {
                try {
                    var field: Field? = null
                    field = try {
                        value?.javaClass?.getDeclaredField(campo)
                    } catch (e: Exception) {
                        value?.javaClass?.superclass?.getDeclaredField(campo)
                    }
                    field?.isAccessible = true
                    retorno += " " + field?.get(value).toString()
                    field?.isAccessible = false
                } catch (e: Exception) {
                    retorno += "" + campo.trim()
                }
            }

        } catch (e: Exception) {
            retorno = value as String
        }
        return retorno.trim()
    }


    fun setResourceDescricao(resource: Int, callback: NovoStringAdapter.onCreate) {
        this.resourceDescricao = resource
        this.callback = callback
    }

    fun getDescricao(position: Int): String {
        return this._itens[position].descricao ?: ""
    }

    fun getAll(): List<T> {
        return _itens.filter { it.objeto != null }.mapNotNull { it.objeto }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View? = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.support_simple_spinner_dropdown_item,
            parent,
            false
        )

        try {
            val retorno: TextView = view as TextView
            retorno.setTextColor(ContextCompat.getColor(context, R.color.gray))

            if (hintPosition != position) {
                retorno.setTextColor(ContextCompat.getColor(context, R.color.gray))
            }
            retorno.text = _itens[position].descricao.toString()
        } catch (ignored: Exception) {

        }
        return view
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View? =
            convertView ?: LayoutInflater.from(context).inflate(this.resource, parent, false)
        var key: String? = null

        try {
            key = _itens[position].descricao
        } catch (ignored: Exception) {
        }

        val retorno: TextView?
        if (resourceDescricao > 0) {
            retorno = view?.findViewById<View>(resourceDescricao) as TextView
            callback?.onCreateView(position, view)
        } else retorno = view as TextView

        if (key != null) retorno.text = key

        retorno.setTextColor(ContextCompat.getColor(context, R.color.gray))
        if (hintPosition != position) {
            retorno.setTextColor(ContextCompat.getColor(context, R.color.gray))
        } else if (!hasValue()) {
            retorno.text = "Lista Vazia"
        }

        return view
    }

    override fun getCount(): Int {
        return this._itens.size
    }

    override fun getItem(position: Int): T? {
        return try {
            _itens[position].objeto
        } catch (e: Exception) {
            super.getItem(position)!!
        }
    }

    override fun getItemId(position: Int): Long {
        var retorno: Long

        lateinit var item: NovoStringAdapter.PropertyAdapter<T>

        if(_itens.size > position)
        {
            item = _itens[position]
        }

        try {
            val field =
                item.objeto?.javaClass?.getDeclaredField(this.campoChave!!) ?: throw Exception()

            field.isAccessible = true

            retorno = if (field.type == String::class.java)
                field.get(item.objeto).toString().hashCode().toLong()
            else
                java.lang.Long.valueOf(field.get(item.objeto).toString())
            field.isAccessible = false
        } catch (e: Exception) {

            retorno = 0

            if (campoChave == null) {
                retorno = item.descricao.hashCode().toLong()
            }
        }
        return retorno
    }

    private var isdialog = true

    fun setIsDialog(value: Boolean) {
        isdialog = value
    }

    constructor(context: Context, resource: Int, objects: List<T>) : this(
        context,
        resource,
        null,
        null,
        objects
    )

    constructor(
        context: Context,
        resource: Int,
        campoChave: String?,
        descricao: Array<String>?,
        objects: List<T>
    ) : this(context, resource, campoChave, descricao, "", objects)

    constructor(
        context: Context,
        resource: Int,
        campoChave: String?,
        camposDescricao: Array<String>?,
        hint: String,
        objects: List<T>
    ) : super(context, resource, objects) {
        this.camposDescricao = camposDescricao
        this.campoChave = campoChave

        this.resource = resource
        this.hint = hint

        this.setValue(objects)
    }

    interface onCreate {
        fun onCreateView(position: Int, view: View)
    }

    data class PropertyAdapter<T : Any?>(
        val descricao: String? = null,
        val objeto: T? = null

    )

}