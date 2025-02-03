package com.fukajima.warframemarket.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.fukajima.warframemarket.R
import com.fukajima.warframemarket.components.CustomSpinner
import com.fukajima.warframemarket.enums.OnlineStatusEnum
import com.fukajima.warframemarket.enums.OrderTypeEnum
import com.fukajima.warframemarket.enums.SortOrderEnum
import com.fukajima.warframemarket.viewModels.ItemOrderViewModel
import com.fukajima.warframemarket.viewModels.ItemViewModel
import com.fukajima.warframerepo.entity.Item
import com.fukajima.warframerepo.entity.ItemOrder
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso

class Fragment_Market : Fragment() {
    private var searchSpinner: CustomSpinner? = null
    private var itemList: List<Item> = mutableListOf()
    private var recyclerView: RecyclerView? = null
    private var progress: ProgressBar? = null
    private var shimmerSpinner: ShimmerFrameLayout? = null
    private var txvItemLabel: TextView? = null
    private var imageViewItem: ImageView? = null
    private var btnSortStatus: MaterialButton? = null
    private var btnSortReputation: MaterialButton? = null
    private var btnSortPrice: MaterialButton? = null
    private var btnSortQuantity: MaterialButton? = null
    private lateinit var sortingButtonsLayout: View
    private lateinit var btnFilter: LinearLayout
    private lateinit var fabPlaceOrder: FloatingActionButton
    private var list: MutableList<ItemOrder> = mutableListOf()
    private var listaFiltrada: MutableList<ItemOrder> = mutableListOf()

    private var selectedItem: Item? = null

    private var quantitySortingValue = SortOrderEnum.DEFAULT
    private var priceSortingValue = SortOrderEnum.ASCENDING
    private var userStatusSortingValue = SortOrderEnum.DESCENDING
    private var userReputationSortingValue = SortOrderEnum.DEFAULT

    private val itemViewModel: ItemViewModel by lazy {
        ViewModelProvider(this@Fragment_Market).get(ItemViewModel::class.java)
    }

    private val itemOrderViewModel: ItemOrderViewModel by lazy {
        ViewModelProvider(this@Fragment_Market).get(ItemOrderViewModel::class.java)
    }

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

        progress = view.findViewById(R.id.loading_frag_market)
        shimmerSpinner = view.findViewById(R.id.market_shimmer_spinner)
        txvItemLabel = view.findViewById(R.id.txv_market_item_label)
        imageViewItem = view.findViewById(R.id.market_iv_item)
        btnFilter = view.findViewById(R.id.linear_layout_fragment_market_filter)
        fabPlaceOrder = view.findViewById(R.id.float_act_btn_fragment_market)

        sortingButtonsLayout = view.findViewById(R.id.sorting_view_frag_market)
        btnSortStatus = view.findViewById(R.id.btn_market_sort_user_status)
        setSortButtonState(btnSortStatus, userStatusSortingValue)
        btnSortStatus?.setOnClickListener(sortingButtonListener())

        btnSortReputation = view.findViewById(R.id.btn_market_sort_reputation)
        setSortButtonState(btnSortReputation, userReputationSortingValue)
        btnSortReputation?.setOnClickListener(sortingButtonListener())

        btnSortPrice = view.findViewById(R.id.btn_market_sort_price)
        setSortButtonState(btnSortPrice, priceSortingValue)
        btnSortPrice?.setOnClickListener(sortingButtonListener())

        btnSortQuantity = view.findViewById(R.id.btn_market_sort_quantity)
        setSortButtonState(btnSortQuantity, quantitySortingValue)
        btnSortQuantity?.setOnClickListener(sortingButtonListener())

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
                selectedItem = searchSpinner?.selectedItem as Item?
                selectedItem?.let {
                    showRecyclerLoading(true)
                    itemOrderViewModel.getItemOrders(it.url_name!!)
                    /*GlobalScope.launch(Dispatchers.IO) {
                        var orderListResponse = ItemRepository(requireContext()).getItemOrders(it.url_name!!)

                        if(orderListResponse.success) {
                            withContext(Dispatchers.Main) {
                                if(!orderListResponse.obj.isNullOrEmpty()) {
                                    recyclerView?.adapter = MarketAdapter(requireContext(), orderListResponse.obj!!, it)
                                    showRecyclerLoading(false)

                                    txvItemLabel?.text = selectedItem.item_name.toString()
                                    txvItemLabel?.visibility = View.VISIBLE
                                    btnFilter.visibility = View.VISIBLE
                                    Picasso
                                        .get()
                                        .load(selectedItem.getItemAssetUrl())
                                        .into(imageViewItem)
                                    imageViewItem?.visibility = View.VISIBLE
                                    //recyclerView?.adapter?.notifyDataSetChanged()
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
                    }*/
                }
                return 0
            }
        })

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_frag_market)
        recyclerView?.apply {
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            visibility = View.VISIBLE
        }

        itemViewModel.itemLiveData.observe(viewLifecycleOwner) {responseApi ->
            if(responseApi.success) {
                itemList = responseApi.obj ?: mutableListOf()

                searchSpinner?.visibility = View.VISIBLE
                searchSpinner?.setAdapter("id", arrayOf("item_name"), itemList)
                shimmerSpinner?.apply {
                    stopShimmer()
                    visibility = View.GONE
                }
            }
        }

        itemOrderViewModel.itemOrderLiveData.observe(viewLifecycleOwner) { orderListResponse ->
            if(orderListResponse.success) {
                if(!orderListResponse.obj.isNullOrEmpty()) {
                    list = orderListResponse.obj!!.toMutableList()
                    selectedItem?.let {
                        //TODO: arrumar essa chamada do doFilter() para receber filtros persistidos do diálogo ao fazer nova requisição
                        listaFiltrada = doFilter(OrderTypeEnum.sellers, OnlineStatusEnum.all, null, null)
                        val listaOrdenada = doSort(userStatusSortingValue, userReputationSortingValue, priceSortingValue, quantitySortingValue)

                        recyclerView?.adapter = MarketAdapter(requireContext(), listaOrdenada, it)
                        showRecyclerLoading(false)

                        txvItemLabel?.text = it.item_name.toString()
                        txvItemLabel?.visibility = View.VISIBLE
                        Picasso
                            .get()
                            .load(it.getItemAssetUrl())
                            .into(imageViewItem)
                        imageViewItem?.visibility = View.VISIBLE
                        btnFilter.visibility = View.VISIBLE
                        sortingButtonsLayout.visibility = View.VISIBLE
                    }
                }
                else {
                    Toast.makeText(requireContext(), requireContext().getString(R.string.no_results_returned) ,Toast.LENGTH_LONG).show()
                }
            }
            else {
                Toast.makeText(requireContext(), orderListResponse.message ,Toast.LENGTH_LONG).show()
                Log.e(FRAGMENT_MARKET_TAG, orderListResponse.message, orderListResponse.exception)
            }
        }

        btnFilter.setOnClickListener{
            openAlertDialogFilter()
        }

        fabPlaceOrder.setOnClickListener {
            openAlertDialogPlaceOrder()
        }

    }

    fun doFilter(orderType: OrderTypeEnum, onlineStatus: OnlineStatusEnum, maxPl:Int?, minPl:Int?) : MutableList<ItemOrder>{
        var listaFiltrada = list.filter { it.platinum!! >= (minPl?:0 ) && it.platinum!! <= (maxPl?: 9999) }.toMutableList()
        when(orderType){
            OrderTypeEnum.buyers -> listaFiltrada = listaFiltrada.filter { it.order_type == "buy" }.toMutableList()
            OrderTypeEnum.sellers -> listaFiltrada = listaFiltrada.filter { it.order_type == "sell" }.toMutableList()
            }
        when(onlineStatus){
            OnlineStatusEnum.onSite -> listaFiltrada = listaFiltrada.filter { it.user?.status == "online" || it.user?.status == "ingame"}.toMutableList()
            OnlineStatusEnum.inGame -> listaFiltrada = listaFiltrada.filter { it.user?.status == "ingame" }.toMutableList()
            else -> {}
        }
        //recyclerView?.adapter =
        //    selectedItem?.let { MarketAdapter(requireContext(), listaFiltrada, it) }
        return listaFiltrada
    }

    fun doSort(statusSorting: SortOrderEnum, reputationSorting: SortOrderEnum, priceSorting: SortOrderEnum, quantitySorting: SortOrderEnum) : MutableList<ItemOrder>{
        var listaOrdenada = if (listaFiltrada.isNullOrEmpty()) list else listaFiltrada

        listaOrdenada = listaOrdenada.sortedWith( compareBy<ItemOrder>{it.order_type}
            .let { comparator ->
                when(statusSorting){
                    SortOrderEnum.ASCENDING -> comparator.thenBy { it.user?.status }
                    SortOrderEnum.DESCENDING -> comparator.thenByDescending { it.user?.status }
                    else -> comparator
                }
            }
            .let { comparator ->
                when(reputationSorting) {
                    SortOrderEnum.ASCENDING -> comparator.thenBy { it.user?.reputation }
                    SortOrderEnum.DESCENDING -> comparator.thenByDescending { it.user?.reputation }
                    else -> comparator
                }
            }
            .let { comparator ->
                when(priceSorting) {
                    SortOrderEnum.ASCENDING -> comparator.thenBy { it.platinum }
                    SortOrderEnum.DESCENDING -> comparator.thenByDescending { it.platinum }
                    else -> comparator
                }
            }
            .let { comparator ->
                when(quantitySorting) {
                    SortOrderEnum.ASCENDING -> comparator.thenBy { it.quantity }
                    SortOrderEnum.DESCENDING -> comparator.thenByDescending { it.quantity }
                    else -> comparator
                }
            }
        ).toMutableList()

        return listaOrdenada
    }

    fun setSortButtonState(button: MaterialButton?, sortState: SortOrderEnum) {
        when(sortState){
            SortOrderEnum.DEFAULT -> {
                button?.setIconTintResource(R.color.invisible)
            }
            SortOrderEnum.ASCENDING -> {
                button?.setIconTintResource(R.color.green_high_reputation)
                button?.setIconResource(android.R.drawable.arrow_up_float)
            }
            SortOrderEnum.DESCENDING -> {
                button?.setIconTintResource(R.color.lighter_orange)
                button?.setIconResource(android.R.drawable.arrow_down_float)
            }
        }
    }

    fun sortingButtonListener() = View.OnClickListener {
        val currentSortValue = when(it.id) {
            R.id.btn_market_sort_user_status -> userStatusSortingValue
            R.id.btn_market_sort_reputation -> userReputationSortingValue
            R.id.btn_market_sort_price -> priceSortingValue
            R.id.btn_market_sort_quantity -> quantitySortingValue
            else -> null
        }

        val enumValues = SortOrderEnum.values().toMutableList()
        val enumCurrentIndex = enumValues.indexOf(currentSortValue)
        var nextIndexValue = if(enumCurrentIndex >= (enumValues.size - 1)) 0 else enumCurrentIndex + 1

        when(it.id) {
            R.id.btn_market_sort_user_status -> userStatusSortingValue = enumValues[nextIndexValue]
            R.id.btn_market_sort_reputation -> userReputationSortingValue = enumValues[nextIndexValue]
            R.id.btn_market_sort_price -> priceSortingValue = enumValues[nextIndexValue]
            R.id.btn_market_sort_quantity -> quantitySortingValue = enumValues[nextIndexValue]
        }

        setSortButtonState(it as MaterialButton, enumValues[nextIndexValue])
        var listaOrdenada = doSort(userStatusSortingValue, userReputationSortingValue, priceSortingValue, quantitySortingValue)
        recyclerView?.adapter =
            selectedItem?.let { MarketAdapter(requireContext(), listaOrdenada, selectedItem!!) }
    }

    fun openAlertDialogPlaceOrder(){
        val dialogView = layoutInflater.inflate(R.layout.dialog_place_order, null)

        val postButton = dialogView.findViewById<Button>(R.id.btn_dialog_place_order_post)
        val closeButton = dialogView.findViewById<Button>(R.id.btn_dialog_place_order_close)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true) // permite fechar clicando fora
            .create()

        postButton.setOnClickListener {

            dialog.dismiss()

        }

        closeButton.setOnClickListener {
            dialog.dismiss()

        }

        dialog.show()
    }

    fun openAlertDialogFilter(){
        val dialogView = layoutInflater.inflate(R.layout.dialog_market_filter, null)

        val confirmButton = dialogView.findViewById<Button>(R.id.btn_dialog_market_confirm)
        val closeButton = dialogView.findViewById<Button>(R.id.btn_dialog_market_close)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true) // permite fechar clicando fora
            .create()

        var enumOnlineStatusEnum = OnlineStatusEnum.all
        var enumOrderType = OrderTypeEnum.sellers
        var radGroupOrderType = dialogView.findViewById<RadioGroup>(R.id.rad_group_dialog_market_order_type)
        var radGroupOnlineStatus = dialogView.findViewById<RadioGroup>(R.id.rad_group_dialog_market_online_status)
        var editTextMaxPl = dialogView.findViewById<EditText>(R.id.edit_text_dialog_market_max_price)
        var editTextMinPl = dialogView.findViewById<EditText>(R.id.edit_text_dialog_market_min_price)

        radGroupOrderType.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rad_btn_dialog_market_buyers -> enumOrderType = OrderTypeEnum.buyers
                R.id.rad_btn_dialog_market_sellers -> enumOrderType = OrderTypeEnum.sellers
            }
        }

        radGroupOnlineStatus.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rad_btn_dialog_market_status_all -> enumOnlineStatusEnum = OnlineStatusEnum.all
                R.id.rad_btn_dialog_market_status_in_game -> enumOnlineStatusEnum = OnlineStatusEnum.inGame
                R.id.rad_btn_dialog_market_status_on_site -> enumOnlineStatusEnum = OnlineStatusEnum.onSite
            }
        }


        confirmButton.setOnClickListener {
            var maxPl: String? = null
            var minPl: String? = null

            if (!editTextMaxPl.text.isNullOrEmpty()){
                maxPl = editTextMaxPl.text.toString()
            }

            if (!editTextMinPl.text.isNullOrEmpty()){
                minPl = editTextMinPl.text.toString()
            }

            listaFiltrada = doFilter(enumOrderType, enumOnlineStatusEnum, maxPl?.toInt(), minPl?.toInt())
            listaFiltrada = doSort(userStatusSortingValue, userReputationSortingValue, priceSortingValue, quantitySortingValue)
            recyclerView?.adapter =
                selectedItem?.let { MarketAdapter(requireContext(), listaFiltrada, it) }
            dialog.dismiss()

        }

        closeButton.setOnClickListener {
            dialog.dismiss()

        }

        dialog.show()

    }

    fun showRecyclerLoading(visible: Boolean) {
        if(visible){
            recyclerView?.visibility = View.GONE
            txvItemLabel?.visibility = View.INVISIBLE
            imageViewItem?.visibility = View.INVISIBLE
            progress?.visibility = View.VISIBLE
        }
        else {
            recyclerView?.visibility = View.VISIBLE
            progress?.visibility = View.GONE
        }
    }

    fun searchItems() {
        searchSpinner?.visibility = View.GONE
        shimmerSpinner?.startShimmer()

        itemViewModel.getItems()
        /*GlobalScope.launch(Dispatchers.IO) {
            val responseApi = ItemRepository(requireContext()).getItems()
            if(responseApi.success) {
                itemList = responseApi.obj ?: mutableListOf()

                withContext(Dispatchers.Main) {
                    searchSpinner?.visibility = View.VISIBLE
                    searchSpinner?.setAdapter("id", arrayOf("item_name"), itemList)
                    shimmerSpinner?.apply {
                        stopShimmer()
                        visibility = View.GONE
                    }
                }
            }
        }*/
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

        fun changeReputationColor(reputation:Int?, txtReputation:TextView?, imgViewReputation:ImageView){
            reputation?.let {
                when{
                    reputation < 5 -> {txtReputation?.setTextColor(ContextCompat.getColor(context, R.color.grey_reputation))
                                    Picasso.get().load(R.drawable.ic_reputaion_apathy).into(imgViewReputation)
                                    imgViewReputation.setColorFilter(ContextCompat.getColor(context, R.color.grey_reputation))}
                    reputation >= 5 -> {txtReputation?.setTextColor(ContextCompat.getColor(context, R.color.green_high_reputation))
                                    Picasso.get().load(R.drawable.ic_reputation_happy).into(imgViewReputation)
                                    imgViewReputation.setColorFilter(ContextCompat.getColor(context, R.color.green_high_reputation))}
                    else -> return
                }
            }

        }

        fun changeUserStatusColor(status:String?, txtStatus:TextView?){
            status?.let {
                when (status) {
                    "online" -> txtStatus?.setTextColor(ContextCompat.getColor(context, R.color.green_user_online))
                    "offline" -> txtStatus?.setTextColor(ContextCompat.getColor(context, R.color.red))
                    "ingame" ->  {txtStatus?.setTextColor(ContextCompat.getColor(context, R.color.asset_purple))
                                txtStatus?.text = "online in game"}
                    else -> return
                }
            }
        }

        fun changeOrderTypeColor_Name(orderType:String?, txtOrderType:TextView?){
            when (orderType) {
                "sell" ->{ txtOrderType?.text="wts"
                            txtOrderType?.setTextColor(ContextCompat.getColor(context, R.color.pink))}
                "buy" -> {txtOrderType?.text="wtb"
                    txtOrderType?.setTextColor(ContextCompat.getColor(context, R.color.green_high_reputation))}
                else -> return
            }
        }

        fun changeProfilePic(avatar:String?, imgViewProfile:ImageView){
            if (avatar.isNullOrEmpty())   {
                Picasso.get()
                    .load(R.drawable.logo_alpha)
                    .into(imgViewProfile)
            }
            else {
                Picasso.get()
                    .load(avatar)
                    .error(R.drawable.logo_alpha)
                    .into(imgViewProfile)
            }


        }


        override fun onBindViewHolder(holder: MarketItemHolder, position: Int) {
            val itemOrder = itemOrders[position]

            holder.txvOrderType.text = itemOrder.order_type.toString()
            holder.txvUsername.text = itemOrder.user?.ingame_name.toString()
            holder.txvItemPrice.text = itemOrder.platinum.toString()
            holder.txvItemQuantity.text = itemOrder.quantity.toString()
            holder.txvUserStatus.text = itemOrder.user?.status.toString()
            holder.txvUserReputation.text = itemOrder.user?.reputation.toString()

            changeReputationColor(itemOrder.user?.reputation, holder.txvUserReputation, holder.imageUserReputation)
            changeUserStatusColor(itemOrder.user?.status.toString(), holder.txvUserStatus)
            changeOrderTypeColor_Name(itemOrder.order_type.toString(), holder.txvOrderType)
            changeProfilePic(itemOrder.user?.getAvatarAssetUrl(), holder.imageUserImage)

            holder.btnBuy.setOnClickListener {
                //TODO: Create clipbboard logic
                Toast.makeText(context, "/w ${itemOrder.user?.ingame_name.toString()} Hello! I want to buy ${selectedItem.item_name.toString()}!", Toast.LENGTH_LONG).show()
            }

        }
        inner class MarketItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txvOrderType = itemView.findViewById<TextView>(R.id.txt_view_order_type)
            val imageUserImage = itemView.findViewById<ImageView>(R.id.image_view_card_market)
            val txvUsername = itemView.findViewById<TextView>(R.id.txt_view_userName)
            val txvUserStatus = itemView.findViewById<TextView>(R.id.txt_view_userStatus)
            val txvUserReputation = itemView.findViewById<TextView>(R.id.txt_view_userReputation)
            val imageUserReputation = itemView.findViewById<ImageView>(R.id.image_view_userReputation)
            val txvItemPrice = itemView.findViewById<TextView>(R.id.txt_view_item_price)
            val txvItemQuantity = itemView.findViewById<TextView>(R.id.txt_view_item_quantity)
            val btnBuy = itemView.findViewById<LinearLayout>(R.id.linear_layout_card_view_market_buy_btn)
        }
    }
}