import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilebanking.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "MyViewModel"

class MyViewModel : ViewModel() {
    private val username = MutableLiveData<String>()
    private val accountNumber = MutableLiveData<String>()
    private val balance = MutableLiveData<Double>()
    private val db = Firebase.firestore

    fun setUsername(username : String){
        this.username.value = username
    }

    fun setAccountNumber(accNum : String){
        this.accountNumber.value = accNum
    }

    fun setBalance(bal : Double){
        this.balance.value = bal
    }

    fun getUsername() : String? {
        return this.username.value
    }
    fun getAccountNumber() : String? {
        return this.accountNumber.value
    }
    fun getBalance() : Double? {
        return this.balance.value
    }

    private val transactions : MutableLiveData<List<Transaction>> by lazy {
        MutableLiveData<List<Transaction>>().also {
            loadTransactions()
        }
    }

    fun getTransactions() : LiveData<List<Transaction>>{
        return transactions
    }

    private fun loadTransactions(){
        //TODO : load transaction from firebase
    }

}