package com.fukajima.warframemarket.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.SpinnerAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.compose.ui.text.toLowerCase
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fukajima.warframemarket.R
import com.fukajima.warframemarket.components.CustomSpinner
import com.fukajima.warframerepo.entity.Item
import com.fukajima.warframerepo.entity.ItemOrder
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
    private var searchSpinner: CustomSpinner? = null
    private var itemList: List<Item> = mutableListOf()
    private var recyclerView: RecyclerView? = null

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

        searchSpinner = view.findViewById<CustomSpinner>(R.id.market_searchSpinner)
        searchSpinner?.setAdapter("id", arrayOf("item_name"), mutableListOf<Item>())
        searchSpinner?.setOnSearch(object : CustomSpinner.OnItemSearch {
            override fun onSearch(busca: String?, lista: MutableList<*>?, setar: ListView?): MutableList<*>? {
                var filtered_list = (lista as MutableList<Item>?)?.filter { it.item_name!!.lowercase().contains(busca.toString().lowercase()) }
                return filtered_list?.toMutableList() ?: lista
            }
        })
        searchItems()

        searchSpinner?.setOnItemSelected(object: CustomSpinner.OnItemSelected {
            override fun onItemSelected(position: Int): Int {
                var selectedItem = searchSpinner?.selectedItem as Item?
                selectedItem?.let {
                    GlobalScope.launch(Dispatchers.IO) {
                        var orderListResponse = ItemRepository(requireContext()).getItemOrders(it.url_name!!)

                        if(orderListResponse.success) {
                            withContext(Dispatchers.Main) {
                                if(!orderListResponse.obj.isNullOrEmpty()) {
                                    recyclerView?.apply {
                                        itemAnimator = DefaultItemAnimator()
                                        setHasFixedSize(true)
                                        adapter =  MarketAdapter(requireContext(), orderListResponse.obj!!, it)
                                        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                                    }
                                    recyclerView?.adapter?.notifyDataSetChanged()
                                }
                                else {
                                    Toast.makeText(requireContext(), requireContext().getString(R.string.no_results_returned) ,Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), orderListResponse.message ,Toast.LENGTH_LONG).show()
                                Log.e(FRAGMENT_MARKET_TAG, orderListResponse.message, orderListResponse.exception)
                            }
                        }
                    }
                }
                return 0
            }
        })

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_frag_market)
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
        const val FRAGMENT_MARKET_TAG = "FRAGMENT_MARKET"
    }

    class MarketAdapter(val context: Context, val itemOrders: List<ItemOrder>, val selectedItem: Item) :
        RecyclerView.Adapter<MarketAdapter.MarketItemHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketItemHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_fragment_market, parent, false)
            return MarketItemHolder(view)
        }

        override fun getItemCount(): Int {
            return itemOrders.size
        }

        override fun onBindViewHolder(holder: MarketItemHolder, position: Int) {
            val itemOrder = itemOrders[position]

            holder.txvOrderType.text = itemOrder.order_type.toString()
            holder.txvUsername.text = itemOrder.user?.ingame_name.toString()
            holder.txvItemPrice.text = itemOrder.platinum.toString()
            holder.txvItemQuantity.text = itemOrder.quantity.toString()
            holder.txvUserStatus.text = itemOrder.user?.status.toString()
            holder.txvUserReputation.text = itemOrder.user?.reputation.toString()

            holder.btnBuy.setOnClickListener {
                //TODO: Create clipbboard logic
                Toast.makeText(context, "/w ${itemOrder.user?.ingame_name.toString()} Hello! I want to buy ${selectedItem.item_name.toString()}!", Toast.LENGTH_LONG).show()
            }

        }
        inner class MarketItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txvOrderType = itemView.findViewById<TextView>(R.id.txt_view_order_type)
            val imageItemImage = itemView.findViewById<ImageView>(R.id.image_view_card_market)
            val txvUsername = itemView.findViewById<TextView>(R.id.txt_view_userName)
            val txvUserStatus = itemView.findViewById<TextView>(R.id.txt_view_userStatus)
            val txvUserReputation = itemView.findViewById<TextView>(R.id.txt_view_userReputation)
            val imageUserReputation = itemView.findViewById<ImageView>(R.id.image_view_userReputation)
            val txvItemPrice = itemView.findViewById<TextView>(R.id.txt_view_item_price)
            val txvItemQuantity = itemView.findViewById<TextView>(R.id.txt_view_item_quantity)
            val btnBuy = itemView.findViewById<ConstraintLayout>(R.id.const_layout_card_view_market)
        }
    }
}