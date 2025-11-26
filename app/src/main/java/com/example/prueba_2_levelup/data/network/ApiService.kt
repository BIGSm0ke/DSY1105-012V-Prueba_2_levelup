// app/src/main/java/com/example/prueba_2_levelup/data/network/ApiService.kt
package com.example.prueba_2_levelup.data.network

import android.content.Context
import com.example.prueba_2_levelup.util.PreferencesManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// --- DTOs de Autenticación (¡ELIMINADOS DE AQUÍ, AHORA ESTÁN EN ARCHIVOS SEPARADOS!) ---
// La compilación buscará los imports de LoginRequest y AuthResponse.

private const val BASE_URL = "http://3.131.93.189:8085/"

interface ProductoApiService {

    // NUEVO ENDPOINT DE LOGIN
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("api/usuarios/{id}")
    suspend fun getUsuarioById(@Path("id") id: Long): UsuarioDTO

    // ENDPOINTS DE PRODUCTO
    @GET("api/productos")
    suspend fun getProductos(): List<NetworkProduct>

    @POST("api/productos")
    suspend fun crearProducto(@Body producto: NetworkProduct): NetworkProduct

    @PUT("api/productos/{id}")
    suspend fun actualizarProducto(
        @Path("id") id: Long,
        @Body producto: NetworkProduct
    ): NetworkProduct

    @DELETE("api/productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Long): Response<Void>
}

object ApiClient {

    fun createProductoService(context: Context): ProductoApiService {

        val prefsManager = PreferencesManager(context)

        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()

            val token: String? = runBlocking {
                prefsManager.authTokenFlow.firstOrNull()
            }

            val requestBuilder = originalRequest.newBuilder()

            if (token != null && token.isNotBlank()) {
                requestBuilder.header("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()

        return retrofit.create(ProductoApiService::class.java)
    }
}