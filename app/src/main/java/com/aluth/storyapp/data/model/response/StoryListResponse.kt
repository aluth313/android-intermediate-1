package com.aluth.storyapp.data.model.response

import com.google.gson.annotations.SerializedName

data class StoryListResponse(

	@field:SerializedName("listStory")
	val listStory: List<Story>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)