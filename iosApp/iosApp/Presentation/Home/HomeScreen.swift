//
//  HomeScreen.swift
//  iosApp
//
//  Created by Arkadiy Blank on 25.03.2025.
//  Copyright © 2025 orgName. All rights reserved.
//



import SwiftUI

struct MainTabView: View {
    var body: some View {
        
        let userIdString = UserDefaults.standard.string(forKey: "profileId") ?? "0"
        let userId = Int64(userIdString) ?? 0
        
        
        TabView {
            HomeTabView()
                .tabItem {
                    Label("Home", systemImage: "house")
                }
            

            ProfileTabView(userId: userId)
                .tabItem {
                    Label("Profile", systemImage: "person")
                }
        }
    }
}






/*
import SwiftUI

struct HomeScreen: View {
    
    @ObservedObject var viewModel = PostsViewModel()
    @ObservedObject var profileViewModel = ProfileViewModel()

    
    @State private var isLoggedIn = true // Добавляем состояние для управления выходом
    
    var body: some View {
        if isLoggedIn {
            VStack {
                 //Text("Welcome to the Home Screen!")
                    //.font(.largeTitle)
                
                // 👤 Отображаем профиль (если есть)
                if let profile = profileViewModel.profile {
                    VStack {
                        Text("👋 Hello, \(profile.name)")
                            .font(.headline)
                        Text(" id - \(profile.id)")
                            .font(.subheadline)
                        Text(" Count Post - \(profile.postCount)")
                            .font(.subheadline)
                    }
                    
                } else {
                    Text("No profile loaded")
                        .foregroundColor(.gray)
                }
                                
                // 🔄 Загрузить профиль вручную
                Button("Load Profile") {
                    Task {
                        await profileViewModel.loadProfile()
                    }
                }
                .buttonStyle(.borderedProminent)
                                
                // 🧪 Отображаем ошибку, если она есть
                if let errorMessage = profileViewModel.errorMessage {
                    Text("Error: \(errorMessage)")
                        .foregroundColor(.red)
                        .padding()
                    
                }
                
                Divider()
                
                
                ////////////////////
                PostListView(viewModel: viewModel)
                
                ///
                //ProfileView()
                
                
                
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
 */


/*
struct HomeScreen_Previews: PreviewProvider {
    static var previews: some View {
        HomeScreen(
            viewModel: MockPostsViewModel()
        )
    }
}*/


