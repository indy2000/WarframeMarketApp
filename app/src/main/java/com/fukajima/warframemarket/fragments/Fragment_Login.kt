package com.fukajima.warframemarket.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fukajima.warframemarket.CacheJWT
import com.fukajima.warframemarket.R
import com.fukajima.warframemarket.viewModels.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL


class Fragment_Login : Fragment() {

    //private var btnBackToApp: Button? = null
    private var btnLogin: Button? = null
    private var editEmail: EditText? = null
    private var editPassword: EditText? = null

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this@Fragment_Login).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //btnBackToApp = view.findViewById(R.id.btn_fragment_login)
        btnLogin = view.findViewById(R.id.btn_fragment_login_authenticate)
        editEmail = view.findViewById(R.id.edit_fragment_login_email)
        editPassword = view.findViewById(R.id.edit_fragment_login_password)

        loginViewModel.loginLiveData.observe(viewLifecycleOwner){responseApi ->
            if(responseApi.success){
                var cache = CacheJWT()
                cache.setJWT(requireContext(), responseApi.obj!!)
                Toast.makeText(context, R.string.login_success, Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
            else {
                Toast.makeText(context, responseApi.message, Toast.LENGTH_SHORT).show()
            }
        }

        btnLogin?.setOnClickListener {
            if((!editEmail?.text.toString().isNullOrEmpty()) && (!editPassword?.text.toString().isNullOrEmpty())){
                var jwt = handleJwtOnLogin()
                loginViewModel.handleLogin(jwt, editEmail?.text.toString(), editPassword?.text.toString())
            }
            else{
                Toast.makeText(requireContext(), "Please inform email and password to login!", Toast.LENGTH_SHORT).show()
            }


        }


        /*btnBackToApp?.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val connection = URL("https://warframe.market").openConnection() as HttpURLConnection
                connection.connect()

                val headerMap = connection.headerFields
                if (headerMap.contains("Set-Cookie")) {
                    var cache = CacheJWT()

                    headerMap.get("Set-Cookie")!!.firstOrNull()?.let {
                        cache.setJWT(requireContext(), it)
                    }

                    parentFragmentManager.popBackStack()
                }
            }
           // parentFragmentManager.popBackStack()

        }*/
    }

    fun handleJwtOnLogin(): String{
        var jwt = ""
        GlobalScope.launch(Dispatchers.IO) {
            val connection = URL("https://warframe.market").openConnection() as HttpURLConnection
            connection.connect()

            val headerMap = connection.headerFields
            if (headerMap.contains("Set-Cookie")) {


                headerMap.get("Set-Cookie")!!.firstOrNull()?.let {
                    jwt = it
                }

            }
        }
        return jwt
    }

}