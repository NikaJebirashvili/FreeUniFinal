package ge.njebirashvili.freeunifinalproject.model

import android.net.Uri

data class UpdateProfile(
     val username: String = "",
     val whatIDo: String = "",
     val profilePictureUrl: Uri? = null,
)