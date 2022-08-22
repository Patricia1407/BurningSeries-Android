package de.datlag.network.github

import com.hadiyarajesh.flower_core.ApiResponse
import de.datlag.model.Constants
import de.datlag.model.github.Release
import de.datlag.model.github.User
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Path
import io.michaelrocks.paranoid.Obfuscate
import kotlinx.coroutines.flow.Flow

@Obfuscate
interface GitHub {

    @Headers(["Accept: ${Constants.MEDIATYPE_GITHUB_JSON}"])
    @GET("/repos/{owner}/{repo}/releases")
    fun getReleases(
        @Path("owner") owner: String = Constants.GITHUB_OWNER,
        @Path("repo") repo: String = Constants.GITHUB_REPO
    ): Flow<ApiResponse<List<Release>>>

    @Headers(["Accept: ${Constants.MEDIATYPE_GITHUB_JSON}"])
    @GET("/user")
    fun getUser(@Header("Authorization") token: String): Flow<ApiResponse<User>>

    @Headers(["Accept: ${Constants.MEDIATYPE_GITHUB_JSON}"])
    @GET("/repos/{owner}/{repo}/contributors")
    fun getContributors(
        @Header("Authorization") token: String,
        @Path("owner") owner: String = Constants.GITHUB_OWNER,
        @Path("repo") repo: String = Constants.GITHUB_REPO
    ): Flow<ApiResponse<List<User>>>
}