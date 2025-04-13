//
//  ProfileTabView.swift
//  iosApp
//
//  Created by Arkadiy Blank on 09.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import shared

struct ProfileTabView: View {
    @ObservedObject var viewModel = ProfileViewModel()
    //@ObservedObject var postsViewModel = PostsViewModel()
    
    let userId: Int64
    @StateObject var postsViewModel: UserPostsViewModel

    init(userId: Int64) {
        self.userId = userId
        _postsViewModel = StateObject(wrappedValue: UserPostsViewModel(userId: userId))
    }

    
    var body: some View {
        NavigationView {
            if let profile = viewModel.profile {
                ProfileView(
                    profile: profile,
                    posts: postsViewModel.posts,
                    onProfileButtonClick: {},
                    onProfileClick: { _ in },
                    onLikeClick: { _ in },
                    onCommentClick: { _ in },
                    onPostMoreIconClick: { _ in },
                    onLoadMorePosts: {
                        Task {
                            await postsViewModel.loadMorePosts()
                        }
                    }
                )
                .navigationTitle("Profile")
                .onAppear {
                    Task {
                        await postsViewModel.loadInitialPosts()
                    }
                }
            } else if let error = viewModel.errorMessage {
                Text("Error: \(error)")
                    .foregroundColor(.red)
                    .padding()
            } else {
                ProgressView("Loading Profile...")
                    .onAppear {
                        Task {
                            await viewModel.loadProfile()
                        }
                    }
            }
        }
        
    }
}
