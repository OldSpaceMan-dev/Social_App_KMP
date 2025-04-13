//
//  LoginViewModel.swift
//  iosApp
//
//  Created by Arkadiy Blank on 25.03.2025.
//  Copyright © 2025 orgName. All rights reserved.
//
import Foundation
import shared
import Combine // Import Combine


extension LoginView {
    @MainActor class LoginViewModel: ObservableObject {
        
        let objectWillChange = ObservableObjectPublisher()
        
        private let loginUseCase =  SignInUseCase()
        //private let loginUseCase = MockSignInUseCase() // Используем заглушку
        
        // Поля для UI
        @Published var email: String = ""
        @Published var password: String = ""
        @Published var isLoading: Bool = false
        @Published var errorMessage: String? = nil
        @Published var loginSuccess: Bool = false { //уведомляем LogView об изменениях
            didSet {
                objectWillChange.send()
            }
        }
        

        func onLoginClicked() async {
            isLoading = true
            errorMessage = nil
            do {
                let result = try await loginUseCase.invoke(email: email, password: password)
                loginSuccess = true
                errorMessage = nil // Убираем сообщение об ошибке, если авторизация прошла успешно
                //print("Login successful! Token: \(result.token)") // Выводим токен в консоль для проверки
                
                // Сохраняем токен в UserDefaults
                //UserDefaults.standard.set(result.token, forKey: "authToken")
            } catch {
                errorMessage = error.localizedDescription
            }
            isLoading = false
        }
        
    }
}

