package com.prafullkumar.onboarding

import android.content.Context
import android.widget.Toast
import com.prafullkumar.common.data.local.UserDataDao
import com.prafullkumar.common.domain.model.UserData
import com.prafullkumar.common.utils.SharedPrefManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OnBoardingRepository : KoinComponent {
    private val dao: UserDataDao by inject()
    private val context: Context by inject()
    private val sharedPref: SharedPrefManager by inject()
    suspend fun insertData(userData: UserData) {
        try {
            dao.insertUserData(userData.toUserDataEntity())
            sharedPref.setOnBoarded()
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}