package de.datlag.network.burningseries

import com.hadiyarajesh.flower_core.ApiResponse
import de.datlag.model.Constants
import de.datlag.model.burningseries.allseries.GenreData
import de.datlag.model.burningseries.home.HomeData
import de.datlag.model.burningseries.series.SeriesData
import de.datlag.model.burningseries.stream.Stream
import de.datlag.model.video.InsertStream
import de.jensklingenberg.ktorfit.http.*
import io.michaelrocks.paranoid.Obfuscate
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Obfuscate
interface BurningSeries {
	
	@Headers(["Accept: ${Constants.MEDIATYPE_JSON}"])
	@GET("/bs/home")
	fun getHomeData(): Flow<ApiResponse<HomeData>>

	@Headers(["Accept: ${Constants.MEDIATYPE_JSON}"])
	@GET("/bs/series/{series}/")
	fun getSeriesData(
		@Path("series") series: String
	): Flow<ApiResponse<SeriesData>>

	@Headers(["Accept: ${Constants.MEDIATYPE_JSON}"])
	@GET("/bs/series/{series}/{season}/")
	fun getSeriesData(
		@Path("series") series: String,
		@Path("season") season: String
	): Flow<ApiResponse<SeriesData>>

	@Headers(["Accept: ${Constants.MEDIATYPE_JSON}"])
	@GET("/bs/series/{series}/{season}/{lang}/")
	fun getSeriesData(
		@Path("series") series: String,
		@Path("season") season: String,
		@Path("lang") language: String
	): Flow<ApiResponse<SeriesData>>

	@Headers(["Accept: ${Constants.MEDIATYPE_JSON}"])
	@GET("/bs/all")
	fun getAllSeries(): Flow<ApiResponse<List<GenreData>>>

	@GET("/bs/video/count")
	fun getSeriesCount(): Flow<ApiResponse<String>>

	@POST("/bs/video")
	fun saveScraped(@Body body: Any): Flow<ApiResponse<InsertStream>>

	@Headers(["Accept: ${Constants.MEDIATYPE_JSON}"])
	@POST("/bs/video/streams")
	fun getStreams(@Body body: Any): Flow<ApiResponse<List<Stream>>>

	@PATCH("/bs/video")
	fun patchStream(@Body body: Any): Flow<ApiResponse<InsertStream>>
}