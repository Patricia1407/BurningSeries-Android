package de.datlag.network.adblock

import com.hadiyarajesh.flower_core.Resource
import com.hadiyarajesh.flower_core.networkResource
import de.datlag.model.Constants
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.create
import io.ktor.util.*
import io.michaelrocks.paranoid.Obfuscate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Named

@Obfuscate
class AdBlockRepository @Inject constructor(
    @Named("ktorAdBlock") private val service: Ktorfit
) {

    fun getAdBlockList(): Flow<Resource<InputStream>> = flow<Resource<InputStream>> {
        networkResource(makeNetworkRequest = {
            service.create<AdBlock>().getAdBlockList(Constants.URL_ADBLOCK_LIST)
        }).collect {
            when (it.status) {
                is Resource.Status.LOADING -> emit(Resource.loading(null))
                is Resource.Status.ERROR -> {
                    val errorStatus = it.status as Resource.Status.ERROR
                    emit(Resource.error(errorStatus.message, errorStatus.statusCode, null))
                }
                is Resource.Status.SUCCESS -> {
                    val successStatus = it.status as Resource.Status.SUCCESS
                    emit(Resource.success(successStatus.data.asStream()))
                }
                is Resource.Status.EMPTY -> {
                    emit(Resource.empty())
                }
            }
        }
    }.flowOn(Dispatchers.IO)
}