package at.fh.swengb.pajuni

import com.squareup.moshi.JsonClass

@JsonClass (generateAdapter = true)

class AuthResponse(val token: String) {
}