package com.arira.ngidol48.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ActivityLoginBinding
import com.arira.ngidol48.helper.BaseActivity
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

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setToolbar(getString(R.string.teks_masuk), binding.toolbar)

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
                            users.fullname = it.displayName.toString()
                            users.phone = it.phoneNumber.toString()
                            users.avatar = it.photoUrl.toString()
                            it.email?.let { it1 -> viewModel.loginEmail(it1) }
                        }
                    }else{
                        toast.show(getString(R.string.teks_gagal_masuk_dengan_google), this)
                    }

                } else {

                }
            }
    }

}