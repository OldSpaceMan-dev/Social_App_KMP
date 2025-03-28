package com.example.socialapp.di

import org.koin.core.context.startKoin
import kotlin.experimental.ExperimentalObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName(name = "KoinHelperKt")
fun initKoin() {
    startKoin {
        modules(getSharedModules())
    }
}