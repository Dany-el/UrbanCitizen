package com.dyablonskyi.transpod.ui.screen.user.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dyablonskyi.transpod.data.di.repository.TicketRepository
import com.dyablonskyi.transpod.data.local.db.entity.Ticket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val repo: TicketRepository
) : ViewModel() {
    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets: StateFlow<List<Ticket>> get() = _tickets.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    fun loadTickets() {
        _isLoading.value = true
        viewModelScope.launch {
            _tickets.value = repo.getAll()
            _isLoading.value = false
        }
    }

    fun insertTicket(ticket: Ticket) {
        viewModelScope.launch {
            repo.insert(ticket)
            loadTickets()
        }
    }

    fun getTicketsByStartDateInDuration(from: LocalDateTime, to: LocalDateTime): List<Ticket> {
        var list: List<Ticket> = emptyList()
        viewModelScope.launch {
            list = repo.getTicketByStartDateIn(from, to)
        }
        return list
    }
}