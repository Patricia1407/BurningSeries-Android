package de.datlag.network.github

import com.apollographql.apollo3.ApolloClient
import com.hadiyarajesh.flower_core.Resource
import com.hadiyarajesh.flower_core.networkResource
import de.datlag.model.Constants
import de.datlag.model.github.Release
import de.datlag.model.github.User
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.create
import io.michaelrocks.paranoid.Obfuscate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

@Obfuscate
class GitHubRepository @Inject constructor(
    @Named("ktorGitHub") private val ktor: Ktorfit,
    @Named("githubApollo") private val apolloClient: ApolloClient
) {

    val service = ktor.create<GitHub>()

    fun getReleases(): Flow<List<Release>> = flow<List<Release>> {
        networkResource(
            makeNetworkRequest = {
                service.getReleases()
            }
        ).collect {
            when (it.status) {
                is Resource.Status.SUCCESS -> emit(((it.status as Resource.Status.SUCCESS).data).toMutableList().filterNot { release -> release.isDraft })
                else -> emit(emptyList())
            }
        }
    }.flowOn(Dispatchers.IO)

    fun getUser(token: String): Flow<User?> = flow {
        networkResource(
            makeNetworkRequest = {
                service.getUser("token $token")
            }
        ).collect {
            when (it.status) {
                is Resource.Status.SUCCESS -> emit((it.status as Resource.Status.SUCCESS).data)
                is Resource.Status.ERROR -> emit(null)
                else -> {  }
            }
        }
    }.flowOn(Dispatchers.IO)

    fun isSponsoring(login: String, token: String) : Flow<Boolean> = flow<Boolean> {
        val apolloClientWithToken = apolloClient.newBuilder().addHttpHeader("Authorization", "Bearer $token").build()
        val response = apolloClientWithToken.query(SponsoringQuery(login)).execute()
        val data = response.data?.user?.sponsoring?.nodes?.mapNotNull { it?.onUser?.login } ?: listOf()
        emit(data.any { it.equals(Constants.GITHUB_OWNER, true) })
    }.flowOn(Dispatchers.IO)

    fun isContributor(login: String, token: String): Flow<Boolean> = flow {
        networkResource(makeNetworkRequest = {
            service.getContributors("token $token")
        }).collect {
            when (it.status) {
                is Resource.Status.SUCCESS -> {
                    val contributors = (it.status as Resource.Status.SUCCESS).data
                    emit(contributors.any { user -> user.login.equals(login, true) })
                }
                is Resource.Status.ERROR -> {
                    emit(false)
                }
                else -> { }
            }
        }
    }.flowOn(Dispatchers.IO)
}