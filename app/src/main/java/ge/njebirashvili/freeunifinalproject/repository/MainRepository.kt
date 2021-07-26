package ge.njebirashvili.freeunifinalproject.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import ge.njebirashvili.freeunifinalproject.model.Message
import ge.njebirashvili.freeunifinalproject.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val realtime: FirebaseDatabase
) {

    private val usersRef = firestore.collection("users")
    private val chatMessages = mutableListOf<Message>()


    suspend fun getUsers(uids: List<String>) = withContext(Dispatchers.IO) {
        val usersList = usersRef.whereIn("uid", uids).get().await().toObjects(User::class.java)
        usersList
    }

    suspend fun getUser(uid: String) = withContext(Dispatchers.IO) {
        val user = usersRef.document(uid).get().await().toObject(User::class.java)
        val currentUid = auth.uid!!
        val currentUser = usersRef.document(currentUid).get().await().toObject(User::class.java)!!
        user?.isFollowing = uid in currentUser.follows
        user
    }


    suspend fun searchUser(query: String) = withContext(Dispatchers.IO) {
        val userResult = usersRef.whereGreaterThanOrEqualTo(
            "username",
            query.uppercase(Locale.ROOT)
        )
            .get().await().toObjects(User::class.java)
        userResult
    }

    suspend fun addChatMessage(message: String, uid: String) = withContext(Dispatchers.IO) {
        val newMessage = Message(message, auth.uid!!,System.currentTimeMillis())
        realtime.getReference(auth.uid!!).child(uid).child("message" + System.currentTimeMillis())
            .setValue(newMessage)
        realtime.getReference(uid).child(auth.uid!!).child("message" + System.currentTimeMillis())
            .setValue(newMessage)
        chatMessages.add(newMessage)
        chatMessages
    }

    suspend fun getChatMessages(uid: String) = withContext(Dispatchers.IO) {
        val result = realtime.getReference("${auth.uid}/$uid").get().await()
        for (message in result.children) {
            val mes = message.getValue(Message::class.java)
            chatMessages.add(mes!!)
        }
        chatMessages

    }


}