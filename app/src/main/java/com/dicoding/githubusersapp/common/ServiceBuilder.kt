package com.dicoding.githubusersapp.common

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.retrofit2protobuf.SendProtobufToFlipperFromRetrofit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    val networkFlipperPlugin = NetworkFlipperPlugin()
    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(FlipperOkhttpInterceptor(networkFlipperPlugin))
        .addInterceptor{ chain->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header(
                    "Authorization",
                    "token ${Constant.API_KEY}"
                )
            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service:Class<T>):T{
        SendProtobufToFlipperFromRetrofit("https://api.github.com/", service.javaClass)
        return retrofit.create(service)
    }
}
