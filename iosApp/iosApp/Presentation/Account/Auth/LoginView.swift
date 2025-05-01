//
//  LoginView.swift
//  iosApp
//
//  Created by Arkadiy Blank on 25.03.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct LoginView: View {
    @StateObject var viewModel = LoginViewModel()
    
    //@State private var isLoggedIn = false // Добавляем состояние для управления переходом
    
    var body: some View {
        VStack {
            if viewModel.isLoggedIn {
                //HomeScreen() // Отображаем HomeScreen, если пользователь залогинен
                MainTabView() // 🔁 Переходим к табам после успешного логина
            } else {
                
                VStack(spacing: 16) {
                    TextField("Email", text: $viewModel.email)
                        .textFieldStyle(.roundedBorder)
                    
                    SecureField("Password", text: $viewModel.password)
                        .textFieldStyle(.roundedBorder)
                    
                    if viewModel.isLoading {
                        ProgressView("Logging in...")
                    } else {
                        Button("Login") {
                            Task {
                                await viewModel.onLoginClicked()
                            }
                        }
                        .buttonStyle(.borderedProminent)
                    }
                    
                    if let error = viewModel.errorMessage {
                        Text(error)
                            .foregroundColor(.red)
                    }
                    
                }
                .padding()
                //.onChange(of: viewModel.loginSuccess) { newValue in // Отслеживаем изменение loginSuccess
                  //  if newValue {
                    //    viewModel.isLoggedIn = true
                    //}
                //}
            }
        }
        .onAppear { // Проверяем при появлении LoginView
            viewModel.checkLoginState() // Проверяем при появлении LoginView
        }
    }
    
}


struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView()
    }
}
