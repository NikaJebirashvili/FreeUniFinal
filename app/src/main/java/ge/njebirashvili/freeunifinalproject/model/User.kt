package ge.njebirashvili.freeunifinalproject.model

import ge.njebirashvili.freeunifinalproject.utils.Constants.DEFAULT_PROFILE_IMAGE


data class User (
    val uid: String = "",
    val username : String = "",
    val profilePictureUrl : String = DEFAULT_PROFILE_IMAGE,
)