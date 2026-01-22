package com.fukajima.warframemarket.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.fukajima.warframemarket.R
import com.fukajima.warframemarket.components.CustomSpinner
import com.fukajima.warframemarket.viewModels.ContractsViewModel
import com.fukajima.warframerepo.entity.ContractWeapon
import com.fukajima.warframerepo.entity.Item
import com.fukajima.warframerepo.entity.KuvaWeapon
import com.fukajima.warframerepo.entity.RivenWeapon
import com.fukajima.warframerepo.entity.SistersWeapon

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Contracts.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Contracts : Fragment() {
    lateinit var category_spinner: CustomSpinner
    lateinit var weapon_spinner: CustomSpinner
    private var selectedCategory: String? = null
    private var selectedItem: Any? = null
    lateinit var itemCategory: String

    private val contractsViewModel:ContractsViewModel by lazy {
        ViewModelProvider(this@Fragment_Contracts).get(ContractsViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment__contracts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        category_spinner = view.findViewById(R.id.contracts_category_searchSpinner)
        category_spinner.setAdapter(null, null, mutableListOf<String>(getString(R.string.riven), getString(R.string.kuva), getString(R.string.sister)))
        category_spinner.setOnItemSelected(object: CustomSpinner.OnItemSelected {
            override fun onItemSelected(position: Int): Int {
                selectedCategory = category_spinner?.selectedItem as String?
                selectedCategory?.let {
                    itemCategory = it
                    contractsViewModel.getContractByCategory(itemCategory)
                }
                return 0
            }
        })

        contractsViewModel.contractLiveData.observe(viewLifecycleOwner){
            if(it.success){
                weapon_spinner?.setAdapter("id", arrayOf("weaponName"), it.obj)

            }else{
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }

        }

        weapon_spinner = view.findViewById(R.id.contracts_weapon_searchSpinner)
        weapon_spinner?.setAdapter("id", arrayOf("weaponName"), mutableListOf<ContractWeapon>())
        weapon_spinner?.setOnSearch(object : CustomSpinner.OnItemSearch {
            override fun onSearch(busca: String?, lista: MutableList<*>, setar: ListView?): MutableList<*>? {
                var filtered_list = when(itemCategory){
                    getString(R.string.sister) -> (lista as? MutableList<SistersWeapon>?)?.filter { it.i18n.en.name!!.lowercase().contains(busca.toString().lowercase()) }
                    getString(R.string.kuva) -> (lista as? MutableList<KuvaWeapon>?)?.filter { it.i18n.en.name!!.lowercase().contains(busca.toString().lowercase()) }
                    getString(R.string.riven) -> (lista as? MutableList<RivenWeapon>?)?.filter { it.i18n.en.name!!.lowercase().contains(busca.toString().lowercase()) }
                    else -> mutableListOf<Any>()
                }

                return filtered_list?.toMutableList() ?: lista
            }
        })
        //searchItems()

        weapon_spinner?.setOnItemSelected(object: CustomSpinner.OnItemSelected {
            override fun onItemSelected(position: Int): Int {
                when(weapon_spinner?.selectedItem){
                    is KuvaWeapon -> selectedItem = weapon_spinner?.selectedItem as KuvaWeapon
                    is SistersWeapon -> selectedItem = weapon_spinner?.selectedItem as SistersWeapon
                    is RivenWeapon -> selectedItem = weapon_spinner?.selectedItem as RivenWeapon
                }

                selectedItem?.let {
                    //showRecyclerLoading(true)
                    //itemOrderViewModel.getItemOrders(it.url_name!!)
                    //itemOrderViewModel.getItemOrdersV2(it.url_name!!)
                }
                return 0
            }
        })


    }
}