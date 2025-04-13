//
//  DependencyProvider.swift
//  iosApp
//
//  Created by Arkadiy Blank on 25.03.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import shared

class DependencyProvider {
    static let shared = DependencyProvider()
    
    private init() {
        // Запустить Koin (если ещё не запущен)
        KMMHelper.shared.initializeKoin()
    }
    
    // Получаем AuthUseCase из Koin
    func provideAuthUseCase() -> SignInUseCase {
        // допустим, в Kotlin есть что-то вроде get<AuthUseCase>()
        // в Swift это будет SharedKoin().getAuthUseCase()
        // либо KoinIOSKt.authUseCase(), в зависимости от того, как вы обернули
        // См. KoinIOS.kt
        return SignInUseCase()
        // или:
        // return MyKoinKt.getKoin().getAuthUseCase()
    }
    
    func createLoginViewModel() -> LoginViewModel {
        return LoginViewModel(signInUseCase: provideAuthUseCase())
    }
}
