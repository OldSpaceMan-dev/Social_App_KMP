package com.example.socialapp.common.util

import kotlinx.coroutines.CoroutineDispatcher


internal interface DispatcherProvider {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
}

//define expect(ожидать) fun to provide dispatcher

internal expect fun provideDispatcher(): DispatcherProvider

