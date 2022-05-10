package com.arira.ngidol48.ui.activity.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.databinding.ActivityLoginBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.SweetAlert
import com.arira.ngidol48.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var users = User()
    private lateinit var viewModel: LoginViewModel

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setToolbar(getString(R.string.teks_masuk), binding.toolbar)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[LoginViewModel::class.java]
        viewModel.context = this

        /*TODO: login by google prepare*/
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth


        //sign out currentUser login when email has selected but not login to the app
        if (auth.currentUser != null){
            googleSignInClient.signOut()
            auth.signOut()
        }
        /* end of login by google prepare*/

        action()
        observeDataAuth()
    }

    private fun observeDataAuth(){
        viewModel.getLoading().observe(this){
            it.let {
                if (it != null){
                    if (it){

                        SweetAlert.dismis()

                        SweetAlert.onLoading(this)
                    }else{
                        SweetAlert.dismis()
                    }
                }
            }
        }

        viewModel.getError().observe(this){
            it.let {
                if (it != null){
                    SweetAlert.dismis()
                    SweetAlert.onFailure(this, it)

                    if (auth.currentUser != null){
                        googleSignInClient.signOut()
                        auth.signOut()
                    }

                }
            }
        }

        viewModel.getResponse().observe(this){
            it.let {
                if (it != null){

                    SweetAlert.dismis()
                    if (it.code == 1){

                        //sukses
                        pref.setUser(it.user)
                        pref.setIsLogin(true)

                        App.user = pref.getUser()
                        App.token = App.user.token_app

                        finish()
                    }
                }
            }
        }

        viewModel.needRegister().observe(this){
            it.let {
                if (it != null){
                    if (it){
                        SweetAlert.dismis()

                        //call register
                        toast.show(getString(R.string.teks_mendaftar), this)
                        viewModel.register(users)
                    }
                }
            }
        }

    }

    private fun action(){
        binding.loginBtnMasukGoogle.setOnClickListener {
            loginResult.launch(googleSignInClient.signInIntent)
        }
    }

    val loginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!

                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    //call viewmodel to login
                    if (user != null) {
                        user.let {
                            users.email = it.email.toString()
                            if (it.displayName != null){
                                users.fullname = it.displayName.toString()
                            }
                            if (it.phoneNumber != null){
                                users.phone = it.phoneNumber.toString()
                            }
                            if (it.photoUrl != null){
                                users.avatar = it.photoUrl.toString()
                            }



                            it.email?.let { viewModel.login(users) }
                        }
                    }else{
                        toast.show(getString(R.string.teks_gagal_masuk_dengan_google), this)
                    }

                } else {
                    SweetAlert.onFailure(this, task.exception?.message)
                }
            }
    }

}