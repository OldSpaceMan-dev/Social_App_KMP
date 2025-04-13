//
//  PostListView.swift
//  iosApp
//
//  Created by Arkadiy Blank on 02.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//


///Логика пернесена в Home Tab View - возможно полезен для профиля
////Удалить 
/*
import SwiftUI
import shared

struct PostListView: View {
    @ObservedObject var viewModel = PostsViewModel()

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
            }
            .navigationTitle("Posts")
            .task {
                await viewModel.loadInitialPosts()
            }
        }
    }
}
*/
