//
//  HomeScreen.swift
//  iosApp
//
//  Created by Arkadiy Blank on 25.03.2025.
//  Copyright © 2025 orgName. All rights reserved.
//



import SwiftUI

struct HomeScreen: View {
    
    @State private var isLoggedIn = true // Добавляем состояние для управления выходом
    
    var body: some View {
        if isLoggedIn {
                VStack {
                    Text("Welcome to the Home Screen!")
                        .font(.largeTitle)

                    Button("Logout") {
                        // Удаляем токен из UserDefaults
                        UserDefaults.standard.removeObject(forKey: "authToken")
                        isLoggedIn = false // Переключаем состояние, чтобы вернуться на экран логина
                    }
                    .buttonStyle(.bordered)
                }
            } else {
                LoginView() // Возвращаемся на экран логина, если пользователь вышел из аккаунта
            }
    }
    
}

struct HomeScreen_Previews: PreviewProvider {
    static var previews: some View {
        HomeScreen()
    }
}
