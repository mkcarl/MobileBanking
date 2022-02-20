import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "MyViewModel"

class MyViewModel : ViewModel() {
    private val username = MutableLiveData<String>()
    private val accountNumber = MutableLiveData<String>()
    private val balance : MutableLiveData<Double> by lazy {
        MutableLiveData<Double>().also {
            loadBalanceFromFirestore(this.getAccountNumber()!!)
        }
    }
    private val bankName = MutableLiveData<String>()
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

    fun setBankName(bankName : String){
        this.bankName.value = bankName
    }

    fun getUsername() : String? {
        return this.username.value
    }
    fun getAccountNumber() : String? {
        return this.accountNumber.value
    }

    fun getBalance() : LiveData<Double> {
        return this.balance
    }

    fun getBankName() : String? {
        return this.bankName.value
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

    private fun loadBalanceFromFirestore(accountNumber:String){
        db.collection("users")
            .whereEqualTo("account_number", accountNumber)
            .addSnapshotListener { users, e ->
                users?.documents?.forEach {
                    this.balance.value = it.getDouble("balance")
                }
            }
    }

}