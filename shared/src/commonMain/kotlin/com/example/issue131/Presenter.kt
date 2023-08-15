package com.example.issue131

import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesRefinedState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

abstract class Presenter<ModelT : Any>(initialModel: ModelT) {
    private val _models = MutableStateFlow(initialModel)
    @NativeCoroutinesRefinedState
    val models: StateFlow<ModelT> = _models.asStateFlow()
    val currentModel: ModelT get() = _models.value

    @NativeCoroutineScope
    internal val iosScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    @NativeCoroutines
    abstract suspend fun start()

    fun updateModel(newModel: ModelT) = _models.update { newModel }
}


class IntPresenter : Presenter<Int>(0) {
    override suspend fun start(): Unit = coroutineScope {
        launch {
            while(isActive) {
                delay(1000)
                updateModel(models.value + 1)
            }
        }
    }
}
