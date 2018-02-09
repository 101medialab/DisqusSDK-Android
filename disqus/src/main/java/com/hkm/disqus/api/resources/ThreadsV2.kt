package com.hkm.disqus.api.resources

import com.hkm.disqus.api.ThreadDetails
import com.hkm.disqus.api.model.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by hayton on 8/2/2018.
 */
interface ThreadsV2 {

    @GET("threads/details.json")
    fun getThreadDetail(
            @Query("thread:ident") id: String,
            @Query("forum") forum: String
    ): Call<ThreadDetails>
}