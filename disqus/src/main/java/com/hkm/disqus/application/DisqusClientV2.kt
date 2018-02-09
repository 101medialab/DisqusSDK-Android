package com.hkm.disqus.application

import android.text.TextUtils
import com.google.gson.GsonBuilder
import com.hkm.disqus.BuildConfig
import com.hkm.disqus.DisqusConstants
import com.hkm.disqus.api.ThreadDetails
import com.hkm.disqus.api.gson.ApplicationsUsageDeserializer
import com.hkm.disqus.api.gson.BlacklistsEntryTypeAdapterFactory
import com.hkm.disqus.api.gson.PostTypeAdapterFactory
import com.hkm.disqus.api.gson.ThreadTypeAdapterFactory
import com.hkm.disqus.api.model.applications.Usage
import com.hkm.disqus.api.model.threads.Thread
import com.hkm.disqus.api.resources.ThreadsV2
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by hayton on 8/2/2018.
 */
class DisqusClientV2 {
    private val USER_AGENT = String.format("Disqus Android/%s", BuildConfig.VERSION_NAME)
    private var apiKey: String = ""
    private var apiSecret: String = ""
    private var accessToken: String = ""
    private var referer: String = ""
    private lateinit var retrofit: Retrofit
    private lateinit var httpClient: OkHttpClient
    private lateinit var apiInterface: ThreadsV2
    private lateinit var loggingInterceptor: HttpLoggingInterceptor
    val BASE_URL = "https://disqus.com/api/3.0/"

    companion object {
        @JvmStatic
        fun getInstance(): DisqusClientV2 {
            return DisqusClientV2()
        }
    }

    init {
        initHttpClient()
        initRetrofit()
    }

    fun initHttpClient() {
        loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        httpClient = OkHttpClient().newBuilder()
                .addInterceptor(getRequestInterceptor())
                .addInterceptor(loggingInterceptor)
                .build()
    }

    fun initRetrofit() {
        val gsonsetup = GsonBuilder()
                .setDateFormat(DisqusConstants.DATE_FORMAT)
                .registerTypeAdapter(Usage::class.java, ApplicationsUsageDeserializer())
                .registerTypeAdapterFactory(BlacklistsEntryTypeAdapterFactory())
                .registerTypeAdapterFactory(PostTypeAdapterFactory())
                .registerTypeAdapterFactory(ThreadTypeAdapterFactory())
                .create()
        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gsonsetup))
                .client(httpClient)
                .build()
        apiInterface = retrofit.create(ThreadsV2::class.java)
    }

    fun setApiKey(apiKey: String) {
        this.apiKey = apiKey
    }

    fun setApiSecret(apiSecret: String) {
        this.apiSecret = apiSecret
    }

    fun setAccessToken(accessToken: String) {
        this.accessToken = accessToken
    }

    fun setReferer(referer: String) {
        this.referer = referer
    }

    fun getRequestInterceptor(): Interceptor {
        return object: Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val originalRequest = chain.request()
                val url = originalRequest.url()
                val urlBuilder = url.newBuilder()
                if (!TextUtils.isEmpty(apiKey)) {
                    urlBuilder.addQueryParameter("api_key", apiKey)
                } else if (!TextUtils.isEmpty(apiSecret)) {
                    urlBuilder.addQueryParameter("api_secret", apiSecret)
                }
                if (!TextUtils.isEmpty(accessToken)) {
                    urlBuilder.addQueryParameter("access_token", accessToken)
                }
                if (!TextUtils.isEmpty(referer)) {
                    urlBuilder.addQueryParameter("referer", referer)
                }
                val requestBuilder = originalRequest.newBuilder()
                requestBuilder.addHeader("User-Agent", USER_AGENT)
                requestBuilder.url(urlBuilder.build())

                val newRequest = requestBuilder.build()
                return chain.proceed(newRequest)
            }
        }
    }

    fun getThreadDetail(id: String, forum: String, callback: Callback<ThreadDetails>) {
        apiInterface.getThreadDetail(id, forum).enqueue(callback)
    }
}