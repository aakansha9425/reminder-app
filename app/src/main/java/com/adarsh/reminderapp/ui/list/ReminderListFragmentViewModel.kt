package com.adarsh.reminderapp.ui.list

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.adarsh.reminderapp.DataState
import com.adarsh.reminderapp.data.ReminderModel
import com.adarsh.reminderapp.repository.ReminderRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReminderListFragmentViewModel
@ViewModelInject constructor(
    private val repository: ReminderRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _dataState = MutableLiveData<DataState<List<ReminderModel>>>()
    val dataState: LiveData<DataState<List<ReminderModel>>>
        get() = _dataState

    fun setState(state: ReminderListState) {
        viewModelScope.launch {
            when(state) {
                is ReminderListState.GetListState -> {
                    repository.getAllReminders().collect {
                        withContext(viewModelScope.coroutineContext) {
                            _dataState.value = it
                        }
                    }
                }
                is ReminderListState.None -> {}
            }
        }
    }

    sealed class ReminderListState {
        object GetListState : ReminderListState()
        object None : ReminderListState()
    }
}