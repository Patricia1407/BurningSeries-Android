package de.datlag.burningseries.module

import com.apollographql.apollo3.ApolloClient
import com.hadiyarajesh.flower_ktorfit.FlowerResponseConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.datlag.model.Constants
import de.datlag.network.adblock.AdBlock
import de.datlag.network.burningseries.BurningSeries
import de.datlag.network.github.GitHub
import de.datlag.network.video.VideoScraper
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.create
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.michaelrocks.paranoid.Obfuscate
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Obfuscate
object NetworkModule {

	val jsonBuilder = Json {
		ignoreUnknownKeys = true
		isLenient = true
		encodeDefaults = true
	}

	@Provides
	@Singleton
	fun provideJsonBuilder(): Json = jsonBuilder

	@Provides
	@Named(Constants.NAMED_JSON)
	fun provideMediaType(): MediaType = Constants.MEDIATYPE_JSON.toMediaType()

	@Provides
	@Singleton
	fun provideHttpClient() = HttpClient(Android) {
		install(HttpTimeout) {
			connectTimeoutMillis = TimeUnit.MINUTES.toMillis(3)
			requestTimeoutMillis = TimeUnit.MINUTES.toMillis(3)
			socketTimeoutMillis = TimeUnit.MINUTES.toMillis(3)
		}
		install(ContentNegotiation) {
			json(jsonBuilder)
		}
	}

	@Provides
	@Singleton
	@Named("ktorBS")
	fun provideBurningSeriesService(
		client: HttpClient
	) = Ktorfit("https://api.datlag.dev/", client)
		.addResponseConverter(FlowerResponseConverter())

	@Provides
	@Singleton
	fun provideVideoScraper() = VideoScraper()

	@Provides
	@Singleton
	@Named("ktorAdBlock")
	fun provideAdBlockService(
		client: HttpClient
	) = Ktorfit("/", client)
		.addResponseConverter(FlowerResponseConverter())

	@Provides
	@Singleton
	@Named("ktorGitHub")
	fun provideGitHubService(
		client: HttpClient
	) = Ktorfit(Constants.API_GITHUB, client)
		.addResponseConverter(FlowerResponseConverter())

	@Provides
	@Singleton
	@Named("anilistApollo")
	fun provideAniListApolloClient() = ApolloClient.Builder().serverUrl("https://graphql.anilist.co").build()

	@Provides
	@Singleton
	@Named("githubApollo")
	fun provideGitHubApolloClient() = ApolloClient.Builder().serverUrl("https://api.github.com/graphql").build()
}
