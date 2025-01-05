package com.fukajima.warframemarket.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SpinnerAdapter
import androidx.appcompat.widget.SearchView
import androidx.compose.ui.text.toLowerCase
import com.fukajima.warframemarket.R
import com.fukajima.warframemarket.components.CustomSpinner
import com.fukajima.warframerepo.entity.Item
import com.fukajima.warframerepo.repository.ItemRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Market.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Market : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var searchView: SearchView? = null
    private var searchSpinner: CustomSpinner? = null
    private var itemList: List<Item> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_market, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById<SearchView>(R.id.market_searchview)
        searchView?.setOnSearchClickListener() {
            searchItems()
        }

        searchSpinner = view.findViewById<CustomSpinner>(R.id.market_searchSpinner)
        searchSpinner?.setAdapter("id", arrayOf("item_name"), mutableListOf<Item>())
        searchSpinner?.setOnSearch(object : CustomSpinner.OnItemSearch {
            override fun onSearch(busca: String?, lista: MutableList<*>?, setar: ListView?): MutableList<*>? {
                var filtered_list = (lista as MutableList<Item>?)?.filter { it.item_name!!.lowercase().contains(busca.toString().lowercase()) }
                return filtered_list?.toMutableList() ?: lista
            }
        })
        searchItems()

    }

    fun searchItems() {
        GlobalScope.launch(Dispatchers.IO) {
            val responseApi = ItemRepository(requireContext()).getItems()
            if(responseApi.success) {
                itemList = responseApi.obj ?: mutableListOf()

                withContext(Dispatchers.Main) {
                    searchSpinner?.setAdapter("id", arrayOf("item_name"), itemList)
                }
            }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_Market().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}