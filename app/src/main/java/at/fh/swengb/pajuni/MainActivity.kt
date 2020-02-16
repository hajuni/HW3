package at.fh.swengb.pajuni

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    companion object{
        val TOKEN = "TOKEN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)

        if(sharedPreferences.getString(TOKEN,null)!= null){
            val intent = Intent(this, NoteListActivity::class.java)
            startActivity(intent)
        }
        else{
            Log.e("Token","No token")
        }

        loginButton.setOnClickListener {
            if(loginUsername.text.toString().isNotEmpty() and loginPassword.text.toString().isNotEmpty()) {
                val authRequest = AuthRequest(loginUsername.text.toString(), loginPassword.text.toString())
                login(authRequest,
                    success = {
                        sharedPreferences.edit().putString(TOKEN, it.token).apply()
                        val intent = Intent(this, NoteListActivity::class.java)
                        startActivity(intent) },
                    error = {
                        Log.e("Problem", it)})
            }
            else {
                Toast.makeText(this, "USERNAME and PASSWORD", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun login ( request: AuthRequest, success: (response: AuthResponse) -> Unit, error: (errorMessage: String) -> Unit) {

        NoteApi.retrofitService.login(request).enqueue(object: retrofit2.Callback<AuthResponse>{
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                error("Failed log-In")
            }

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    success(responseBody)
                }
                else {
                    error("ERROR")
                }

            }
        })
    }
}