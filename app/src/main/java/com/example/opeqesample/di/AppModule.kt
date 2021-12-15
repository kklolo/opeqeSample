package com.example.opeqesample.di


import com.example.opeqesample.utils.AppConfig.BASE_URL
import com.example.opeqesample.repository.api.MainRetrofitApi
import com.example.opeqesample.utils.TLSSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {



    @Singleton
    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun provideRetrofitclient(logging: HttpLoggingInterceptor): OkHttpClient {


        var client: OkHttpClient? = OkHttpClient()

        try {
            val tlsSocketFactory = TLSSocketFactory()
            if (tlsSocketFactory.trustManager != null) {
                client = OkHttpClient.Builder().addInterceptor(logging)
                        .sslSocketFactory(tlsSocketFactory, tlsSocketFactory.trustManager!!)
                        .build()
            }
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        }

        return client!!
    }


    @Singleton
    @Provides
    fun provideGsonFactory(): GsonConverterFactory =
            GsonConverterFactory.create()


   @Singleton
    @Provides
    fun provideMainRetrofitApiBuilder(client: OkHttpClient, gsonFactory: GsonConverterFactory): MainRetrofitApi =

            Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(gsonFactory)
                    .client(client).build().create(MainRetrofitApi::class.java)




}




