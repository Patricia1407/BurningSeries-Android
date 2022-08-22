package de.datlag.network.adblock

import com.hadiyarajesh.flower_core.ApiResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Url
import io.ktor.utils.io.core.*
import io.michaelrocks.paranoid.Obfuscate
import kotlinx.coroutines.flow.Flow
import java.io.ByteArrayInputStream

@Obfuscate
interface AdBlock {

    @GET
    fun getAdBlockList(@Url url: String): Flow<ApiResponse<Input>>
}