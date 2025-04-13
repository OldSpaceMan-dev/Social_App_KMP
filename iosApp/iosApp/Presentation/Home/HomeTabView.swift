//
//  HomeTabView.swift
//  iosApp
//
//  Created by Arkadiy Blank on 09.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct HomeTabView: View {
    @ObservedObject var viewModel = PostsViewModel()
    @ObservedObject var profileViewModel = ProfileViewModel()
    
    @AppStorage("authToken") var authToken: String?
    
    var body: some View {
        NavigationView {
            
            List(viewModel.posts, id: \.postId) { post in
                
                PostItemView(
                    post: post,
                    onProfileClick: { userId in
                        // TODO: Handle profile click
                        print("Profile Clicked for user \(userId)")
                    },
                    onLikeClick: { post in
                        // TODO: Handle like click
                        print("Like Clicked for post \(post.postId)")
                    },
                    onCommentClick: { post in
                        // TODO: Handle comment click
                        print("Comment Clicked for post \(post.postId)")
                    },
                    onPostMoreIconClick: { post in
                        // TODO: Handle more icon click
                        print("More Icon Clicked for post \(post.postId)")
                    }
                )
                .listRowSeparator(.hidden) // убирает разделители
                .listRowInsets(EdgeInsets()) // убирает отступы внутри ячеек
                .onAppear {
                    // Загружаем следующие посты, когда появляется последний элемент
                    if post == viewModel.posts.last && !viewModel.endReached && !viewModel.isLoading {
                        Task {
                            await viewModel.loadMorePosts()
                        }
                    }
                }
                
                //
                
            }
            .listStyle(PlainListStyle()) // минимальный стиль
            .navigationTitle("Home")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button("Logout") {
                        authToken = nil // Удаляем токен — пользователь считается разлогинен
                    }
                    .buttonStyle(.bordered)
                }
                
            }
            .task {
                print("HomeTabView: loading initial posts")
                await viewModel.loadInitialPosts()
            }
            
        }
        
    }
    
}



