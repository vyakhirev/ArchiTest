package com.mikhail.vyakhirev.utils

import java.io.IOException
import com.mikhail.vyakhirev.data.model.Result

suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>, errorMessage: String): Result<T> = try {
    call.invoke()
} catch (e: Exception) {
    Result.Error(IOException(errorMessage, e))
}
val <T> T.exhaustive: T get() = this