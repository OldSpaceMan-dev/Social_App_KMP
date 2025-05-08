//
//  PostItemView.swift
//  iosApp
//
//  Created by Arkadiy Blank on 01.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PostItemView: View {
    let post: Post
    let onProfileClick: (Int64) -> Void
    let onLikeClick: (Post) -> Void
    let onCommentClick: (Post) -> Void
    let onPostMoreIconClick: (Post) -> Void

    @State private var isExpanded = false // показ текста
    
    var body: some View {
        VStack(alignment: .leading) {
            PostHeaderView(
                name: post.userName,
                profileUrl: post.userImageUrl,
                date: post.createdAt,
                onProfileClick: {
                    onProfileClick(post.userId)
                },
                onPostMoreIconClick: {
                    onPostMoreIconClick(post)
                }
            )

            PostImage(imageUrl: post.imageUrl)
            .padding(.bottom, 8)

            PostLikesRowView(
                likesCount: Int(post.likesCount),
                commentsCount: Int(post.commentsCount),
                onLikeClick: {
                    onLikeClick(post)
                },
                isPostLiked: post.isLiked,
                onCommentClick: {
                    onCommentClick(post)
                }
            )

            VStack(alignment: .leading, spacing: 4) {
                Text(post.caption)
                    .font(.body)
                    .lineLimit(isExpanded ? nil : 2)
                    .animation(.easeInOut, value: isExpanded)
                
                if showMoreTextButton(text: post.caption) {
                    Button(action: {
                        isExpanded.toggle()
                    }) {
                        Text(isExpanded ? "Show less" : "Show more")
                            .font(.caption)
                            .foregroundColor(.blue)
                    }
                }
            }
            
         
        }
        .background(Color(uiColor: .systemBackground))
        .cornerRadius(10)
        //.shadow(radius: 3)
        .padding(.vertical, 8)
    }
    
    
    private func showMoreTextButton(text: String) -> Bool {
        return text.count > 120
    }
}



struct PostListItemPreview: PreviewProvider {
    static var previews: some View {
        PostItemView(
            post: Post(
                postId: 1,
                caption: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore...Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore..",
                imageUrl: "https://burst.shopifycdn.com/photos/the-eiffel-tower-paris.jpg",
                createdAt: "20 min",
                likesCount: 12,
                commentsCount: 2,
                userId: 1,
                userName: "Mr Smith",
                userImageUrl: "https://burst.shopifycdn.com/photos/the-eiffel-tower-paris.jpg",
                isLiked: true,
                isOwnPost: true
            ),
            onProfileClick: { userId in
                print("Profile Clicked for user \(userId)")
            },
            onLikeClick: { post in
                print("Like Clicked for post \(post.postId)")
            },
            onCommentClick: { post in
                print("Comment Clicked for post \(post.postId)")
            },
            onPostMoreIconClick: { post in
                print("More Icon Clicked for post \(post.postId)")
            }
        )
        .previewLayout(.sizeThatFits)
    }
}
