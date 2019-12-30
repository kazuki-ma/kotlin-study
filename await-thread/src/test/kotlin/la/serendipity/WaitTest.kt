package la.serendipity

import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.Executors
import kotlin.coroutines.experimental.migration.toExperimentalCoroutineContext

class WaitTest {
    companion object {
        val log = mu.KotlinLogging.logger {}
    }


    @Test
    fun testWait() {
        val api = Retrofit.Builder()
                .baseUrl("https://api.ipify.org")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor(log::info)
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build())
                .build()
                .create(WebApi::class.java)

        Executors.newSingleThreadExecutor().asCoroutineDispatcher()

        Unconfined.toExperimentalCoroutineContext()

        val map = GlobalScope.async(context = Unconfined) {
            val ip = api.getIp()
            log.info { ip.toString() }
            ip
        }

        log.info { runBlocking { map.await() } }
    }


    interface WebApi {
        @GET("/")
        suspend fun getIp(@Query("format") format: String = "json"): Map<String, String>
    }
}
