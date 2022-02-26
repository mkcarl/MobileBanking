import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilebanking.FundTransfer2
import com.example.mobilebanking.R
import com.example.mobilebanking.entities.Transaction
import com.example.mobilebanking.entities.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "MyViewModel"

class MyViewModel : ViewModel() {
    private val db = Firebase.firestore

    private val user = MutableLiveData<User>()
    private val receiverUser : MutableLiveData<User?> = MutableLiveData<User?>()


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

    fun loadReceiverUser(accNum: String, bankName : String){
        db.collection("users")
            .whereEqualTo("account_number", accNum)
            .whereEqualTo("bank_name", bankName)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null){
                    if (task.result!!.documents.isNotEmpty()) {
                        val user = task.result!!.documents[0]
                        receiverUser.value = user.toObject(User::class.java)
                    }
                }
            }
    }

    fun getReceivedUser() : LiveData<User?> = receiverUser
    fun clearReceivedUser() {
        receiverUser.value = null
    }

    // update user in firestore
    fun updateUser(){
        val allUser = mutableListOf<User>()
        db.collection("users")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    task.result!!.documents.forEach { userDocument ->
                        if (userDocument.get("account_number") == this.user.value!!.account_number){
                            userDocument.reference.update(
                                mapOf("balance" to this.user.value!!.balance)
                            )
                        } else if (userDocument.get("account_number") == this.receiverUser.value!!.account_number){
                            userDocument.reference.update(
                                mapOf("balance" to this.receiverUser.value!!.balance)
                            )
                        }
                    }
                    this.receiverUser.value = null
                }
            }

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


}