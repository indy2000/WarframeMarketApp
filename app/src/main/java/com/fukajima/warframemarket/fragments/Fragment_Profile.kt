package com.fukajima.warframemarket.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fukajima.warframemarket.CacheJWT
import com.fukajima.warframemarket.R
import com.fukajima.warframemarket.enums.OnlineStatusEnum
import com.fukajima.warframemarket.enums.OrderTypeEnum
import com.fukajima.warframemarket.viewModels.ItemOrderViewModel
import com.fukajima.warframemarket.viewModels.ProfileViewModel
import com.fukajima.warframerepo.entity.Item
import com.fukajima.warframerepo.entity.ItemData
import com.fukajima.warframerepo.entity.ItemOrder
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

    val profileViewModel: ProfileViewModel by lazy{
        ViewModelProvider(this@Fragment_Profile).get(ProfileViewModel::class.java)
    }

    val itemOrderViewModel: ItemOrderViewModel by lazy{
        ViewModelProvider(this@Fragment_Profile).get(ItemOrderViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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

        profileViewModel.profileLiveData.observe(viewLifecycleOwner){responseApi ->
            if(responseApi.success){

                txtUseName?.text = responseApi.obj!!.ingameName
                txtReputation?.text = responseApi.obj!!.reputation.toString()
                txtPlatform?.text = responseApi.obj!!.platform
                txtLanguage?.text = responseApi.obj!!.locale


            }
            else {
                Toast.makeText(context, responseApi.message, Toast.LENGTH_SHORT).show()
            }
        }

        itemOrderViewModel.getItemOrderSignInUser(CacheJWT().getJWT(requireContext()))

        itemOrderViewModel.userItemOrderLiveData.observe(viewLifecycleOwner){userOrderList ->
            if(userOrderList.success) {
                if(!userOrderList.obj.isNullOrEmpty()) {
                    list = userOrderList.obj!!.toMutableList()

                    recyclerView?.adapter = Fragment_Profile.ProfileAdapter(requireContext(), list)

                }
            }
            else {
                    Toast.makeText(requireContext(), userOrderList.message ,Toast.LENGTH_LONG).show()
                    //Log.e(Fragment_Market.FRAGMENT_MARKET_TAG, userOrderList.message, userOrderList.exception)
            }
        }

    }



    class ProfileAdapter(val context: Context, val itemOrders: List<ItemData>) :
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

            holder.txtOrderType.text = itemOrder.type.toString()
            holder.txtItemPrice.text = itemOrder.platinum.toString()
            holder.txtItemQuantity.text = itemOrder.quantity.toString()
            holder.txtItemName.text = itemOrder.id.toString()
            holder.txtVisibility.text = itemOrder.visible.toString()


            //changeReputationColor(itemOrder.user?.reputation, holder.txvUserReputation, holder.imageUserReputation)
            //changeUserStatusColor(itemOrder.user?.status.toString(), holder.txvUserStatus)
            //changeOrderTypeColor_Name(itemOrder.order_type.toString(), holder.txvOrderType)
            //changeProfilePic(itemOrder.user?.getAvatarAssetUrl(), holder.imgProfilePic)

            holder.btnSold.setOnClickListener {
                //TODO: Implement Logic

            }

            holder.btnEdit.setOnClickListener {
                //TODO: Implement Logic
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
        }




    }

}