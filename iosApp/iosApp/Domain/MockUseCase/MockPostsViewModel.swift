//
//  MockPostsViewModel.swift
//  iosApp
//
//  Created by Arkadiy Blank on 02.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import shared
import Combine

class MockPostsViewModel: ObservableObject {
    @Published var posts: [Post] = [
        Post(postId: 1, caption: "This is a mock post!", imageUrl: "", createdAt: "1 hour ago", likesCount: 10, commentsCount: 2, userId: 1, userName: "Mock User", userImageUrl: nil, isLiked: false, isOwnPost: false),
        Post(postId: 2, caption: "Another mock post!", imageUrl: "", createdAt: "2 hours ago", likesCount: 5, commentsCount: 1, userId: 2, userName: "Another User", userImageUrl: nil, isLiked: true, isOwnPost: false)
    ]
    @Published var isLoading = false
    @Published var errorMessage: String? = nil
    @Published var isLoggedIn = true
    
    func getPosts() {
            isLoading = false
   }
         
   func logOut() {
   }
}
