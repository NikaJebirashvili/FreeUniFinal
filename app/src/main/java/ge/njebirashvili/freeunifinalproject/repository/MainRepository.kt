package ge.njebirashvili.freeunifinalproject.repository

import android.net.Uri
import android.text.BoringLayout
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import ge.njebirashvili.freeunifinalproject.model.Message
import ge.njebirashvili.freeunifinalproject.model.UpdateProfile
import ge.njebirashvili.freeunifinalproject.model.User
import ge.njebirashvili.freeunifinalproject.utils.Constants.DEFAULT_PROFILE_IMAGE
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
        user
    }

    suspend fun toggleFollowForUser(uid: String): Boolean = withContext(Dispatchers.IO) {
        var isFollowing = false
        firestore.runTransaction { transaction ->
            val currentUid = auth.uid!!
            val currentUser =
                transaction.get(usersRef.document(currentUid)).toObject(User::class.java)!!
            isFollowing = uid in currentUser.follows
            val newFollows: List<String> =
                if (!isFollowing) currentUser.follows + uid else currentUser.follows
            transaction.update(usersRef.document(currentUid), "follows", newFollows)
        }.await()
        !isFollowing
    }

    suspend fun toggleLastSentMessage(message: Message) = withContext(Dispatchers.IO) {
        firestore.runTransaction { transaction ->
            transaction.update(usersRef.document(auth.uid!!), "lastSentMessage", message.message)
            transaction.update(
                usersRef.document(auth.uid!!),
                "lastSentMessageDate",
                message.sentDate
            )
            transaction.update(
                usersRef.document(message.sender),
                "lastSentMessage",
                message.message
            )
            transaction.update(
                usersRef.document(message.sender),
                "lastSentMessageDate",
                message.sentDate
            )
        }
    }


    suspend fun searchUser(query: String) = withContext(Dispatchers.IO) {
        val userResult = usersRef.whereGreaterThanOrEqualTo(
            "username",
            query
        )
            .get().await().toObjects(User::class.java)
        userResult
    }

    suspend fun addChatMessage(message: String, uid: String) = withContext(Dispatchers.IO) {
        val newMessage = Message(message, auth.uid!!, System.currentTimeMillis())
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

    suspend fun getAllUser() = withContext(Dispatchers.IO) {
        val usersList = usersRef.get().await().toObjects(User::class.java)
        usersList
    }

    suspend fun userLogOut() = withContext(Dispatchers.IO) {
        auth.signOut()
    }


    suspend fun updateProfile(UpdateProfile: UpdateProfile)=
        withContext(Dispatchers.IO) {
            val imageUrl = UpdateProfile.profilePictureUrl?.let { uri ->
                updateProfilePicture(uri).toString()
            }
            val map = mutableMapOf(
                "username" to UpdateProfile.username,
                "whatIDo" to UpdateProfile.whatIDo
            )
            imageUrl?.let { url ->
                map["profilePictureUrl"] = url
            }
            usersRef.document(auth.uid!!).update(map.toMap()).await()
            Any()
        }

    private suspend fun updateProfilePicture(imageUri: Uri) = withContext(Dispatchers.IO) {
        val user = getUser(auth.uid!!)
        if (user?.profilePictureUrl != DEFAULT_PROFILE_IMAGE) {
            storage.getReferenceFromUrl(user!!.profilePictureUrl).delete().await()
        }
        val url = storage.getReference(auth.uid!!).putFile(imageUri)
            .await().metadata?.reference?.downloadUrl?.await()
        url
    }


}