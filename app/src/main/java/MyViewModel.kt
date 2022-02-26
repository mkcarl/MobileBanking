import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilebanking.entities.Transaction
import com.example.mobilebanking.entities.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "MyViewModel"

class MyViewModel : ViewModel() {
    private val user = MutableLiveData<User>()

    @Deprecated("initial test, it just loads all transactions")
    val allTransactions : MutableLiveData<List<Transaction>> by lazy {
        MutableLiveData<List<Transaction>>().also{
            if (this.user.value != null) {
                loadTransactionsFromFirestore(this.user.value!!.account_number)
            }
        }
    }
    private val sentTransactions : MutableLiveData<List<Transaction>> by lazy {
        MutableLiveData<List<Transaction>>().also{
            if (this.user.value != null) {
                loadSentTransactionsFromFirestore(this.user.value!!.account_number)
            }
        }
    }

    private val receivedTransactions : MutableLiveData<List<Transaction>> by lazy {
        MutableLiveData<List<Transaction>>().also{
            if (this.user.value != null) {
                loadReceivedTransactionsFromFirestore(this.user.value!!.account_number)
            }
        }
    }


    private val db = Firebase.firestore

    fun loadUser(username: String) : LiveData<User?> {
        db.collection("users")
            .whereEqualTo("username", username)
            .addSnapshotListener{ data, e ->
                if (data != null){
                    user.value = data.documents[0].toObject(User::class.java)
                }
            }
        return user
    }

    fun getUser() : LiveData<User> = user


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


}