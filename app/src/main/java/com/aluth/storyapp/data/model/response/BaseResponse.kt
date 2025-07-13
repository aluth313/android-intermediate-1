package com.aluth.storyapp.data.model.response

import com.google.gson.annotations.SerializedName

data class BaseResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
