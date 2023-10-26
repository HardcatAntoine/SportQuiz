package com.chestbox.app.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Questions(
    @SerializedName("question") @Expose val question: String? = null,
    @SerializedName("a") @Expose val a: String? = null,
    @SerializedName("b") @Expose val b: String? = null,
    @SerializedName("c") @Expose val c: String? = null,
    @SerializedName("d") @Expose val d: String? = null,
    @SerializedName("answer") @Expose val answer: Int

    )
