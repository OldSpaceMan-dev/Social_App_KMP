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



/*
enum SwiftResult<Success, Failure: Error> {
    case success(Success)
    case failure(Failure)
}

func toSwiftResult<T>(kmmResult: shared.Result<T>) -> SwiftResult<T, Error> {
    
        return .success(kmmResult.data as! T)
}
 */


extension LoginView {
    @MainActor class LoginViewModel: ObservableObject {
        
        //let objectWillChange = ObservableObjectPublisher()
        
        //private let signInUseCase =  SignInUseCase.init()
        private let signInUseCase = KoinIOSHelper().signInUseCase()
        private let getProfileUseCase = KoinIOSHelper().getProfileUseCase()

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
        @Published var isLoggedIn = false

       

        func onLoginClicked() async {
            isLoading = true
            errorMessage = nil
            do {
                let result = try await signInUseCase.invoke(email: email, password: password)
                               
                loginSuccess = true
                errorMessage = nil /// Убираем сообщение об ошибке, если авторизация прошла успешно
                print("Login successful! Token: \(result.data?.token)") // Выводим токен в консоль для проверки
                print("All data: \(result.data)")
                print(" ")
                
                let resultGetProfile = try await getProfileUseCase.invoke(profileId: result.data?.id ?? 0)
                print("resultGetProfile: \(resultGetProfile)")
                
                /// Сохраняем токен в UserDefaults
                UserDefaults.standard.set(result.data?.token, forKey: "authToken")
                                
                //UserDefaults.standard.set(result.data?.id, forKey: "profileId")
                if let id = result.data?.id {
                    UserDefaults.standard.set(String(id), forKey: "profileId")
                }
                print("✅ Saved profileId: \(String(describing: result.data?.id))")
                
                isLoggedIn = true
                
                ///для информации
                let defaults = UserDefaults.standard
                    if let authToken = defaults.string(forKey: "authToken") {
                        print("authToken: \(authToken)")
                    } else {
                        print("authToken is not set in UserDefaults")
                    }

                
            
            } catch {
                errorMessage = error.localizedDescription
            }
            isLoading = false
        }
        
        func checkLoginState() {
                    isLoggedIn = UserDefaults.standard.string(forKey: "authToken") != nil
            }
   
        
    }
}

