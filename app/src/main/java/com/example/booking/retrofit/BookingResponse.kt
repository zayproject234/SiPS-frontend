package com.example.booking.retrofit

import com.google.gson.annotations.SerializedName

data class BookingResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataBooking(

	@field:SerializedName("duration")
	val duration: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("paymentProof")
	val paymentProof: String? = null,

	@field:SerializedName("notes")
	val notes: String? = null,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@field:SerializedName("totalPrice")
	val totalPrice: String? = null,

	@field:SerializedName("bandName")
	val bandName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("transactionCode")
	val transactionCode: String? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
