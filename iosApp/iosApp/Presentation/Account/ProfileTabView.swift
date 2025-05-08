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
    //@ObservedObject var viewModel = OwnProfileViewModel()
    //@ObservedObject var postsViewModel = PostsViewModel()
    
    
    
    let userId: Int64
    @StateObject var postsViewModel: UserPostsViewModel
    @StateObject var userProfileViewModel: UserProfileViewModel
    
    @State private var isEditingProfile = false // состояние навигации
    @State private var showToast = false


    init(userId: Int64) {
        self.userId = userId
        _postsViewModel = StateObject(wrappedValue: UserPostsViewModel(userId: userId))
        _userProfileViewModel = StateObject(wrappedValue: UserProfileViewModel(userId: userId))
    }

    
    var body: some View {
        ZStack {
            if let profile = userProfileViewModel.profile { //viewModel.profile {
                ProfileView(
                    profile: profile,
                    posts: postsViewModel.posts,
                    onProfileButtonClick: {
                        if profile.isOwnProfile {
                            isEditingProfile = true
                        } else {
                            //TODO for follow/unfollow
                        }
                    },
                    onProfileClick: { _ in },
                    onLikeClick: { post in
                        Task {
                            await postsViewModel.toggleLike(post: post)
                        }
                        print("ProfileTabView: Liked post \(post.postId), isLiked: \(post.isLiked)")
                    },
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
                .sheet(isPresented: $isEditingProfile) {
                    EditProfileView(
                        profile: profile,
                        userId: profile.id,
                        onProfileUpdated: {
                            Task {
                                await userProfileViewModel.loadProfile()
                                await postsViewModel.loadInitialPosts()
                            }
                            isEditingProfile = false
                        }
                    )
                }
               
            } else if let error = userProfileViewModel.errorMessage { //viewModel.errorMessage {
                Text("Error: \(error)")
                    .foregroundColor(.red)
                    .padding()
            } else {
                ProgressView("Loading Profile...")
                    .onAppear {
                        Task {
                            await userProfileViewModel.loadProfile()//viewModel.loadProfile()
                        }
                    }
            }
            // Отображение тоста
            if let toast = postsViewModel.toastMessage, showToast {
                ToastView(
                    message: toast.text,
                    isError: toast.isError,
                    isShowing: $showToast
                )
                
            }
        }
        //MARK: -Показ тоста
        .onReceive(postsViewModel.$toastMessage) { newValue in
            if newValue != nil {
                withAnimation(.easeInOut) {
                    showToast = true
                }
            } else {
                withAnimation(.easeInOut) {
                    showToast = false
                }
            }
            
        }
        
    }
}
