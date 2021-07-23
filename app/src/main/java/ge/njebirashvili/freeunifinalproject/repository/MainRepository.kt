package ge.njebirashvili.freeunifinalproject.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import ge.njebirashvili.freeunifinalproject.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val auth : FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage : FirebaseStorage
) {

    private val usersRef = firestore.collection("users")


    suspend fun getUsers(uids : List<String>) = withContext(Dispatchers.IO) {
            val usersList = usersRef.whereIn("uid",uids).get().await().toObjects(User::class.java)
            usersList
        }

    suspend fun getUser(uid: String) = withContext(Dispatchers.IO) {
            val user = usersRef.document(uid).get().await().toObject(User::class.java)
            val currentUid = auth.uid!!
            val currentUser = usersRef.document(currentUid).get().await().toObject(User::class.java)!!
            user?.isFollowing = uid in currentUser.follows
            user
        }


    suspend fun searchUser(query : String)  = withContext(Dispatchers.IO) {
            val userResult = usersRef.whereGreaterThanOrEqualTo("username",
                query.uppercase(Locale.ROOT)
            )
                .get().await().toObjects(User::class.java)
            userResult
    }



}