import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.LocalTime

data class Transaction(
//    val date : LocalDate,
//    val time : LocalTime,
    val sender : String,
    val receiver : String,
    val datetime : Timestamp,
    val amount : Double,
    val details : String
) {
}