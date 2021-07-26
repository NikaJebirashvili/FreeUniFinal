package ge.njebirashvili.freeunifinalproject.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import ge.njebirashvili.freeunifinalproject.utils.Constants.DEFAULT_PROFILE_IMAGE
import java.io.Serializable

@IgnoreExtraProperties
data class User (
    val uid: String = "",
    val username : String = "",
    val whatIDo : String = "",
    val profilePictureUrl : String = DEFAULT_PROFILE_IMAGE,
    var follows : List<String> = listOf(),
    @get:Exclude
    var isFollowing : Boolean = false
) : Serializable