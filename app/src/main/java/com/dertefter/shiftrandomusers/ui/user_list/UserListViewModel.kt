package com.dertefter.shiftrandomusers.ui.user_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.shiftrandomusers.data.model.User
import com.dertefter.shiftrandomusers.data.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>?>(null)
    val users: StateFlow<List<User>?> = _users

    init {
        viewModelScope.launch {
            usersRepository.getUsersFlow().collect {
                _users.value = it
            }
        }
    }


    private val _uiStatus = MutableStateFlow(UiStatus.DONE)

    val uiStatus: StateFlow<UiStatus> = _uiStatus

    fun refreshUsers(count: Int = 20) {
        viewModelScope.launch {
            _uiStatus.value = UiStatus.LOADING
            val result = usersRepository.updateUsers(count)
            _uiStatus.value = if (result.isSuccess) UiStatus.DONE else UiStatus.ERROR
        }
    }
}