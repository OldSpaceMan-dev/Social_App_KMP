//
//  HomeScreen.swift
//  iosApp
//
//  Created by Arkadiy Blank on 25.03.2025.
//  Copyright © 2025 orgName. All rights reserved.
//



import SwiftUI

struct MainTabView: View {
    @AppStorage("authToken") var authToken: String?
    
    
    @State private var selectedTab: Tab = .home // 🔥 добавить выбранную вкладку
    
    enum Tab {
            case home
            case create
            case profile
        }

    
    //прозрачный таббар
    init() {
        let appearance = UITabBarAppearance()
        //appearance.configureWithTransparentBackground() ///полносьью прозрачный фон
        appearance.configureWithDefaultBackground()
        
        appearance.backgroundEffect = nil //отключает блюр
        appearance.backgroundColor = UIColor.systemBackground.withAlphaComponent(0.9)
        
        /// Кастомизация иконок и текста
        let itemAppearance = appearance.stackedLayoutAppearance

        // Нормальное состояние
        itemAppearance.normal.iconColor = UIColor.label.withAlphaComponent(0.5)
        itemAppearance.normal.titleTextAttributes = [
            .foregroundColor: UIColor.label.withAlphaComponent(0.9),
            .font: UIFont.systemFont(ofSize: 11, weight: .medium)
        ]

        
        // Выбранное состояние
        itemAppearance.selected.iconColor = UIColor.label.withAlphaComponent(1.0)
        itemAppearance.selected.titleTextAttributes = [
            .foregroundColor: UIColor.label.withAlphaComponent(1.0),
            .font: UIFont.systemFont(ofSize: 11, weight: .medium)
        ]

        
        
        UITabBar.appearance().standardAppearance = appearance
        if #available(iOS 15.0, *) {
            UITabBar.appearance().scrollEdgeAppearance = appearance
        }
    }
    
    
    
    var body: some View {
        
        if authToken == nil {
            LoginView()
        } else {
            
            let userIdString = UserDefaults.standard.string(forKey: "profileId") ?? "0"
            let userId = Int64(userIdString) ?? 0
            

            TabView(selection: $selectedTab) {
                
                
                HomeTabView()
                    .tabItem {
                        Label("Home", systemImage: "house")
                    }
                    .tag(Tab.home)
                
                
                CreatePostView(selectedTab: $selectedTab)
                    .tabItem {
                        Image(systemName: "plus.circle")
                        Text("Create")
                    }
                    .tag(Tab.create)
                
                NavigationStack {
                    ProfileTabView(userId: userId)
                }
                .tabItem {
                    Label("Profile", systemImage: "person")
                }
                .tag(Tab.profile)
                
                
            }
            
        }
    }
}



