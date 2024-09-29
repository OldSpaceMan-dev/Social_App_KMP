package com.example.socialapp.android.common.util

import com.example.socialapp.account.domain.model.Profile
import com.example.socialapp.android.common.util.Constants.EVENT_BUS_BUFFER_CAPACITY
import com.example.socialapp.common.domain.model.Post
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow



// object создает синглтон, который имеет единственный экземпляр в приложении

object EventBus {
    //приватное неизменяемое свойство _events
    //MutableSharedFlow<Event>: Это поток, который позволяет нескольким подписчикам получать события
    //extraBufferCapacity - кол-во событий в буфере которые ожидают обработки
    private val _events = MutableSharedFlow<Event>(extraBufferCapacity = EVENT_BUS_BUFFER_CAPACITY)
    //Преобразует MutableSharedFlow в SharedFlow,
    // что позволяет сделать поток только для чтения из внешнего кода
    val events = _events.asSharedFlow()


    suspend fun send(event: Event) {
        //Эмитирует (отправляет) событие в MutableSharedFlow, делая его доступным для всех подписчиков.
        _events.emit(event)
    }
}

//запечатанный интерфейс - ограничивает реализацию только в этом файле
sealed interface Event{

    data class PostUpdated(val post: Post): Event

    data class ProfileUpdated(val profile: Profile): Event
}