package de.datlag.network.github

import com.hadiyarajesh.flower.ApiResponse
import de.datlag.model.Constants
import de.datlag.model.github.Release
import io.michaelrocks.paranoid.Obfuscate
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

@Obfuscate
interface GitHub {

    @Headers("Accept: ${Constants.MEDIATYPE_GITHUB_JSON}")
    @GET("/repos/{owner}/{repo}/releases")
    fun getReleases(
        @Path("owner") owner: String = Constants.GITHUB_OWNER,
        @Path("repo") repo: String = Constants.GITHUB_REPO
    ): Flow<ApiResponse<List<Release>>>
}