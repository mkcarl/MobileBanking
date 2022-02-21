import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilebanking.entities.Transaction
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
    @Deprecated("initial test, it just loads all transactions")
    val allTransactions : MutableLiveData<List<Transaction>> by lazy {
        MutableLiveData<List<Transaction>>().also{
            loadTransactionsFromFirestore(this.getAccountNumber()!!)
        }
    }
    private val sentTransactions : MutableLiveData<List<Transaction>> by lazy {
        MutableLiveData<List<Transaction>>().also{
            loadSentTransactionsFromFirestore(this.getAccountNumber()!!)
        }
    }

    private val receivedTransactions : MutableLiveData<List<Transaction>> by lazy {
        MutableLiveData<List<Transaction>>().also{
            loadReceivedTransactionsFromFirestore(this.getAccountNumber()!!)
        }
    }


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

    @Deprecated("initial test, it just loads all transactions")
    fun getTransactions() : LiveData<List<Transaction>>{
        return this.allTransactions
    }

    fun getSentTransactions() : LiveData<List<Transaction>>{
        return this.sentTransactions
    }

    fun getReceivedTransactions() : LiveData<List<Transaction>>{
        return this.receivedTransactions
    }

    @Deprecated("initial test, it just loads all transactions")
    private fun loadTransactionsFromFirestore(accNum: String){
        db.collection("transactions")
            .addSnapshotListener{ t, e ->
                if (e != null){
                    Log.w(TAG, "Failed", e)
                }

                if (t != null){
                    val allTransactions = mutableListOf<Transaction>()
                    t.documents.forEach {
                        if (it.getString("sender_acc") == accNum || it.getString("receiver_acc") == accNum){
                            allTransactions.add(it.toObject(Transaction::class.java)!!)
                        }
                    }
                    this.allTransactions.value = allTransactions.sortedByDescending { it.datetime }
                }
            }
    }

    private fun loadSentTransactionsFromFirestore(accNum: String){
        db.collection("transactions")
            .addSnapshotListener{ t, e ->
                if (e != null){
                    Log.w(TAG, "Failed", e)
                }

                if (t != null){
                    val allTransactions = mutableListOf<Transaction>()
                    t.documents.forEach {
                        if (it.getString("sender_acc") == accNum){
                            allTransactions.add(it.toObject(Transaction::class.java)!!)
                        }
                    }
                    this.sentTransactions.value = allTransactions.sortedByDescending { it.datetime }
                }
            }
    }

    private fun loadReceivedTransactionsFromFirestore(accNum: String){
        db.collection("transactions")
            .addSnapshotListener{ t, e ->
                if (e != null){
                    Log.w(TAG, "Failed", e)
                }

                if (t != null){
                    val allTransactions = mutableListOf<Transaction>()
                    t.documents.forEach {
                        if (it.getString("receiver_acc") == accNum){
                            allTransactions.add(it.toObject(Transaction::class.java)!!)
                        }
                    }
                    this.receivedTransactions.value = allTransactions.sortedByDescending { it.datetime }
                }
            }
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