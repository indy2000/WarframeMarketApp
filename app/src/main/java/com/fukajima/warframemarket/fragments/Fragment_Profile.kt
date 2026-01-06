package com.fukajima.warframemarket.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.material3.Snackbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fukajima.warframemarket.CacheJWT
import com.fukajima.warframemarket.R
import com.fukajima.warframemarket.components.CustomSpinner
import com.fukajima.warframemarket.enums.OnlineStatusEnum
import com.fukajima.warframemarket.enums.OrderTypeEnum
import com.fukajima.warframemarket.viewModels.ItemOrderViewModel
import com.fukajima.warframemarket.viewModels.ItemViewModel
import com.fukajima.warframemarket.viewModels.ProfileViewModel
import com.fukajima.warframerepo.entity.Item
import com.fukajima.warframerepo.entity.ItemData
import com.fukajima.warframerepo.entity.ItemOrder
import com.fukajima.warframerepo.entity.ItemOrderEditRequest
import com.fukajima.warframerepo.entity.PlaceOrderRequest
import com.fukajima.warframerepo.entity.UserData
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Profile : Fragment() {
    // TODO: Rename and change types of parameters

    var txtUseName: TextView? = null
    var txtReputation: TextView? = null
    var txtPlatform: TextView? = null
    var txtLanguage: TextView? = null
    var recyclerView: RecyclerView? = null
    var list: MutableList<ItemData> = mutableListOf()
    var dialogEditOrder: AlertDialog? = null

    val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this@Fragment_Profile).get(ProfileViewModel::class.java)
    }

    val itemOrderViewModel: ItemOrderViewModel by lazy {
        ViewModelProvider(this@Fragment_Profile).get(ItemOrderViewModel::class.java)
    }

    val itemViewModel: ItemViewModel by lazy{
        ViewModelProvider(this@Fragment_Profile).get(ItemViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtUseName = view.findViewById(R.id.txt_fragment_profile_username)
        txtReputation = view.findViewById(R.id.txt_fragment_profile_reputation)
        txtPlatform = view.findViewById(R.id.txt_fragment_profile_platform)
        txtLanguage = view.findViewById(R.id.txt_fragment_profile_language)
        recyclerView = view.findViewById(R.id.recycler_fragment_profile)

        recyclerView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        profileViewModel.getUserProfile(CacheJWT().getJWT(requireContext()))

        profileViewModel.profileLiveData.observe(viewLifecycleOwner) { responseApi ->
            if (responseApi.success) {

                txtUseName?.text = responseApi.obj!!.ingameName
                txtReputation?.text = responseApi.obj!!.reputation.toString()
                txtPlatform?.text = responseApi.obj!!.platform
                txtLanguage?.text = responseApi.obj!!.locale


            } else {
                Toast.makeText(context, responseApi.message, Toast.LENGTH_SHORT).show()
            }
        }

        itemOrderViewModel.getItemOrderSignInUser(CacheJWT().getJWT(requireContext()))

        itemOrderViewModel.userItemOrderLiveData.observe(viewLifecycleOwner) { userOrderList ->
            if (userOrderList.success) {
                if (!userOrderList.obj.isNullOrEmpty()) {
                    list = userOrderList.obj!!.toMutableList()

                    recyclerView?.adapter = object : Fragment_Profile.ProfileAdapter(requireContext(), list){
                        override fun handleSoldOrder(quantity: Int, id: String) {
                            itemOrderViewModel.soldItemOrder(quantity, CacheJWT().getJWT(requireContext()), id)
                        }

                        override fun openAlertDialog(order: ItemData) {
                            alertDialogEditOrder(order)
                        }

                        override fun handleDeleteOrder(id: String) {
                            itemOrderViewModel.deleteItemOrder(id, CacheJWT().getJWT(requireContext()))
                        }


                    }

                }
            } else {
                Toast.makeText(requireContext(), userOrderList.message, Toast.LENGTH_LONG).show()
                //Log.e(Fragment_Market.FRAGMENT_MARKET_TAG, userOrderList.message, userOrderList.exception)
            }
        }

        itemOrderViewModel.soldItemOrderLiveData.observe(viewLifecycleOwner){
            if(it.success){
                itemOrderViewModel.getItemOrderSignInUser(CacheJWT().getJWT(requireContext()))
                Toast.makeText(context,R.string.order_sold, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        itemOrderViewModel.editItemOrderLiveData.observe(viewLifecycleOwner){
            if(it.success){
                itemOrderViewModel.getItemOrderSignInUser(CacheJWT().getJWT(requireContext()))
                Toast.makeText(context,R.string.order_edited, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        itemOrderViewModel.deleteItemOrderLiveData.observe(viewLifecycleOwner){
            if(it.success){
                itemOrderViewModel.getItemOrderSignInUser(CacheJWT().getJWT(requireContext()))
                Toast.makeText(context,R.string.order_deleted, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }

    }


    abstract class ProfileAdapter(val context: Context, val itemOrders: List<ItemData>) :
        RecyclerView.Adapter<Fragment_Profile.ProfileAdapter.ProfileItemHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Fragment_Profile.ProfileAdapter.ProfileItemHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_fragment_profile_order, parent, false)
            return ProfileItemHolder(view)
        }

        override fun getItemCount(): Int {
            return itemOrders.size
        }

        override fun onBindViewHolder(holder: ProfileItemHolder, position: Int) {
            val itemOrder = itemOrders[position]

            holder.txtOrderType.text = if (itemOrder.type == "sell") "wts" else "wtb"
            holder.txtItemPrice.text = itemOrder.platinum.toString()
            holder.txtItemQuantity.text = itemOrder.quantity.toString()
            holder.txtItemName.text = itemOrder.item_name
            holder.txtVisibility.text = if (itemOrder.visible) context.getString(R.string.visible) else context.getString(R.string.hidden)
            handleOrderTextColors(holder.txtOrderType.text.toString(), holder.txtOrderType, holder.txtVisibility.text.toString(), holder.txtVisibility)


            //changeReputationColor(itemOrder.user?.reputation, holder.txvUserReputation, holder.imageUserReputation)
            //changeUserStatusColor(itemOrder.user?.status.toString(), holder.txvUserStatus)
            //changeOrderTypeColor_Name(itemOrder.order_type.toString(), holder.txvOrderType)
            //changeProfilePic(itemOrder.user?.getAvatarAssetUrl(), holder.imgProfilePic)

            holder.btnSold.setOnClickListener {
                handleSoldOrder(itemOrder.quantity, itemOrder.id)
            }

            holder.btnEdit.setOnClickListener {
                openAlertDialog(itemOrder)
            }

            holder.btnDelete.setOnClickListener {
                var dialog = AlertDialog.Builder(context)
                    .setTitle(R.string.delete_message)
                    .setPositiveButton(R.string.yes, object: DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            handleDeleteOrder(itemOrder.id)
                        }
                    })
                    .setNegativeButton(R.string.no, object: DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog?.dismiss()
                        }

                    })
                    .setCancelable(true)
                    .create()

                dialog.show()

            }

        }

        inner class ProfileItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imgProfilePic = itemView.findViewById<ImageView>(R.id.image_view_fragment_profile)
            val txtOrderType = itemView.findViewById<TextView>(R.id.txt_card_view_fragment_wts)
            val txtItemName = itemView.findViewById<TextView>(R.id.txt_card_view_fragment_item_title)
            val txtItemQuantity = itemView.findViewById<TextView>(R.id.txt_card_view_fragment_item_quantity)
            val txtItemPrice = itemView.findViewById<TextView>(R.id.txt_card_view_fragment_platinum_quantity)
            val txtVisibility = itemView.findViewById<TextView>(R.id.txt_card_view_fragment_visibility)
            val btnSold = itemView.findViewById<MaterialButton>(R.id.btn_profile_order_sold)
            val btnEdit = itemView.findViewById<MaterialButton>(R.id.btn_profile_order_edit)
            val btnDelete = itemView.findViewById<ImageButton>(R.id.btn_profile_order_delete)
        }


        fun handleOrderTextColors(orderType: String, orderTypeView: TextView, orderVisibility: String, orderVisibilityView: TextView) {
            when (orderVisibility) {
                "Visible" -> orderVisibilityView.setTextColor(ContextCompat.getColor(context, R.color.green_user_online))
                "Hidden" -> orderVisibilityView.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
            when (orderType) {
                "wts" -> orderTypeView.setTextColor(ContextCompat.getColor(context, R.color.pink))
                "wtb" -> orderTypeView.setTextColor(ContextCompat.getColor(context, R.color.green_high_reputation))

            }
        }

        abstract fun handleSoldOrder(quantity: Int, id: String)

        abstract fun openAlertDialog(order:ItemData)

        abstract fun handleDeleteOrder(id: String)



    }

    fun alertDialogEditOrder(actualOrder:ItemData){
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_order, null)

        val postButton = dialogView.findViewById<Button>(R.id.btn_dialog_edit_order_post)
        val closeButton = dialogView.findViewById<Button>(R.id.btn_dialog_edit_order_close)
        val closeXButton = dialogView.findViewById<ImageButton>(R.id.image_button_dialog_edit_order_close)
        val itemName = dialogView.findViewById<TextView>(R.id.txt_view_dialog_edit_order_item_desc)
        val itemImage = dialogView.findViewById<ImageView>(R.id.image_view_dialog_edit_order)
        val radBtnVisible = dialogView.findViewById<RadioButton>(R.id.rad_btn_dialog_edit_order_visible)
        val radBtnHidden = dialogView.findViewById<RadioButton>(R.id.rad_btn_dialog_edit_order_hidden)
        val editPricePerUnit = dialogView.findViewById<EditText>(R.id.edit_text_dialog_edit_order_price)
        val editItemQuantity = dialogView.findViewById<EditText>(R.id.edit_text_dialog_edit_order_quantity)
        val cache = CacheJWT()




        dialogEditOrder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true) // permite fechar clicando fora
            .create()

        itemName.text = actualOrder.item_name.toString()
        editItemQuantity.setText(actualOrder.quantity.toString())
        editPricePerUnit.setText(actualOrder.platinum.toString())
        if(actualOrder.visible) radBtnVisible.isChecked = true else radBtnHidden.isChecked = true

        itemViewModel.getItemById(actualOrder.itemId)

        itemViewModel.itemDbLiveData.observe(viewLifecycleOwner){
            if(it != null){
                Picasso
                    .get()
                    .load(it.getItemAssetUrl())
                    .into(itemImage)
            }

        }

        //editItemQuantity.text.toString().toIntOrNull(actualOrder.quantity)
        //editPricePerUnit.text.toString().toIntOrNull(actualOrder.platinum)



        //Picasso
        //    .get()
        //    .load(it.getItemAssetUrl())
        //    .into(itemImage)

        postButton.setOnClickListener {
            var JWT = cache.getJWT(requireContext())
            var order = ItemOrderEditRequest()
            order.quantity = editItemQuantity.text.toString().toInt()
            order.visible = radBtnVisible.isChecked
            order.platinum = editPricePerUnit.text.toString().toInt()
            //order.item_id = selectedItem?.id

            JWT = JWT.split(";").first(){it.contains("JWT=")}
            itemOrderViewModel.editItemOrder(order, JWT, actualOrder.id)

        }

        closeButton.setOnClickListener {
            dialogEditOrder?.dismiss()

        }

        closeXButton.setOnClickListener {
            dialogEditOrder?.dismiss()

        }

        //radBtnVisible.isChecked = true

        dialogEditOrder?.show()

    }


}