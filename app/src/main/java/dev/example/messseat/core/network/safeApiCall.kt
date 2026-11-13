package dev.example.messseat.core.network

import kotlinx.coroutines.CancellationException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(block: suspend () -> T): ApiResult<T> {
    return try {
        ApiResult.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        val (message, code) = parseError(e)
        ApiResult.Error(message, code, e.code())
    } catch (_: IOException) {
        ApiResult.Error("Cannot reach the server. Check your connection and try again.")
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Something went wrong. Please try again.")
    }
}


private fun parseError(e: HttpException): Pair<String, String?> {
    val body = runCatching { e.response()?.errorBody()?.string() }.getOrNull()
    if (body.isNullOrBlank()) return "Something went wrong(${e.code()})." to null
    return try {
        val json = JSONObject(body)
        val code = json.optString("code").ifBlank { null }
        val fieldErrors = json.optJSONObject("fieldErrors")
        if (fieldErrors != null && fieldErrors.length() > 0) {
            val key = fieldErrors.keys().next()
            return ("$key: ${fieldErrors.getString(key)}") to code
        }

        val message = json.optString("message").ifBlank { "Request failed (${e.code()})." }
        message to code
    } catch (_: Exception) {
        "Something went wrong(${e.code()})." to null
    }
}