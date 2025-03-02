package com.prafullkumar.trainxai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.prafullkumar.common.data.local.UserDataDao
import com.prafullkumar.common.domain.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ChatMessage(
    val content: String,
    val isUser: Boolean,
    val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
)

class AIViewModel(
    private val userDao: UserDataDao
) : ViewModel() {

    private val user = userDao.getUserDataFlow().map { list ->
        list.map { it.toUserData() }.firstOrNull() ?: UserData()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserData())

    private val generativeModel = MutableStateFlow<GenerativeModel?>(null)
    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    init {
        viewModelScope.launch {
            user.collect { userData ->
                if (userData.userName.isNotBlank()) {
                    generativeModel.value = GenerativeModel(
                        modelName = "gemini-2.0-flash",
                        apiKey = "AIzaSyBjrl6BChFKUd6YJfAHs7s15rqgLLZE5uY",
                        systemInstruction = getSystemPrompt()
                    )
                }
                _loading.update { false }
            }
        }
    }

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _chatHistory = MutableStateFlow<List<String>>(emptyList())
    val chatHistory: StateFlow<List<String>> = _chatHistory.asStateFlow()

    fun sendMessage(userMessage: String) {
        if (generativeModel.value == null || userMessage.isBlank()) {
            return
        }
        viewModelScope.launch {
            _messages.value += ChatMessage(userMessage, true)

            try {
                val response = generativeModel.value!!.generateContent(userMessage).text
                    ?: "Sorry, I couldn't process that."
                _messages.value += ChatMessage(response, false)
                if (_chatHistory.value.none { it == userMessage }) {
                    _chatHistory.value += userMessage
                }
            } catch (e: Exception) {
                _messages.value += ChatMessage("Error: ${e.message}", false)
            }
        }
    }

    private fun getSystemPrompt(): Content {
        val userName = user.value.userName
        val userWeight = user.value.userWeight
        val userHeight = user.value.userHeight
        val userGoal = user.value.userGoal
        val userGender = user.value.userGender
        val userAge = user.value.userAge
        val userActivityLevel = user.value.userActivityLevel
        return content {
            text(
                """
        You are TrainX AI, a specialized fitness and health assistant created by TrainX developers. Your purpose is to help users achieve their health and fitness goals by providing personalized advice, motivation, and information.
        
        When interacting with the user, refer to them by their name (${userName}) and personalize your responses based on their specific profile:
        - Weight: ${userWeight}kg
        - Height: ${userHeight}cm
        - Goal: ${userGoal}
        - Gender: ${userGender}
        - Age: ${userAge}
        - Activity Level: ${userActivityLevel}
        
        Your expertise is strictly limited to:
        - Workout recommendations and exercise techniques
        - Nutrition guidance and meal planning based on their goals
        - Rest and recovery strategies
        - Progress tracking and goal setting
        - Motivation and habit formation
        - Water intake recommendations
        - Macro and calorie tracking support
        
        Always tailor your advice to the user's specific goals, fitness level, and personal data. When suggesting workouts, consider their activity level, age, and physical limitations.
        
        If asked about your identity, only respond with: "I am TrainX AI, created by TrainX developers."
        
        Never provide medical diagnoses or treatment recommendations. For any medical concerns, advise the user to consult a healthcare professional.
        
        Do not engage in discussions unrelated to health, fitness, nutrition, and wellness. If the user asks questions outside your domain of expertise, politely redirect the conversation back to fitness and health topics.
        
        Maintain a motivational, supportive tone while being factually accurate and evidence-based in your recommendations.
        """
            )
        }
    }
}