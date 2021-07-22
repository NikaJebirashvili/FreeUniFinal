package ge.njebirashvili.mysdf.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ge.njebirashvili.freeunifinalproject.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
){
    suspend fun register(
        email : String,
        username : String,
        password : String,
    ) : AuthResult {
        return withContext(Dispatchers.IO){
                val result = auth.createUserWithEmailAndPassword(email,password).await()
                val uid = result.user?.uid!!
                val user = User(uid,username)
                firestore.collection("users").document(uid).set(user).await()
                result
        }
    }

    suspend fun login(email: String,password: String) : AuthResult {
        return withContext(Dispatchers.IO){
                val result = auth.signInWithEmailAndPassword(email,password).await()
                result
        }

    }
}