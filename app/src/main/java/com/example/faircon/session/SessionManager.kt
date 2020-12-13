package com.example.faircon.session

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.faircon.models.AuthToken
import com.example.faircon.persistence.AuthTokenDao
import com.example.faircon.util.Constants.Companion.TAG
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    private val authTokenDao: AuthTokenDao,
) {
    private val _cachedToken = MutableLiveData<AuthToken>()

    val cachedToken: LiveData<AuthToken>
        get() = _cachedToken

    fun login(newValue: AuthToken) {
        setValue(newValue)
    }

    fun logout() {
        Log.d(TAG, "logout: ")

        CoroutineScope(IO).launch {
            var errorMessage: String? = null
            try {
                _cachedToken.value!!.account_pk?.let { token ->
                    authTokenDao.nullifyToken(token)
                } ?: throw CancellationException("Token Error. Logging out user.")
            } catch (e: CancellationException) {
                Log.e(TAG, "CancellationException: ${e.message}")
                errorMessage = e.message
            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message}")
                errorMessage = errorMessage + "\n" + e.message
            } finally {
                errorMessage?.let {
                    Log.e(TAG, "finally: logout: $errorMessage")
                }
                Log.d(TAG, "logout: finally")
                setValue(null)
            }
        }
    }

    fun setValue(newValue: AuthToken?) {
        GlobalScope.launch(Main) {
            if (_cachedToken.value != newValue) {
                _cachedToken.value = newValue
            }
        }
    }
}