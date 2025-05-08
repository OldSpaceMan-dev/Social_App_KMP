//
//  ProfileView.swift
//  iosApp
//
//  Created by Arkadiy Blank on 06.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ProfileView: View {
    
    //@ObservedObject var viewModel = ProfileViewModel()
    @Environment(\.colorScheme) var colorScheme

    
    let profile: Profile
    let posts: [Post]

    let onProfileButtonClick: () -> Void
    let onProfileClick: (Int64) -> Void
    let onLikeClick: (Post) -> Void
    let onCommentClick: (Post) -> Void
    let onPostMoreIconClick: (Post) -> Void
    
    let onLoadMorePosts: (() -> Void)?

    
    
    var buttonText: String {
            if profile.isOwnProfile {
                return "Edit profile"
            } else if profile.isFollowing {
                return "Unfollow"
            } else {
                return "Follow"
            }
        }

    var isOutlined: Bool {
        profile.isOwnProfile || profile.isFollowing
    }

    var isFilled: Bool {
        !profile.isOwnProfile && !profile.isFollowing
    }

    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading) {
                // MARK: - Top profile info
                HStack(alignment: .center) {
                    // Avatar
                    ProfileImage(imageUrl: profile.imageUrl, size: 90)
                        .clipShape(Circle())
                    
                    Spacer()

                    // Stats
                    HStack(spacing: 24) {
                        statView(count: Int64(profile.postCount), label: "Posts")
                        statView(count: Int64(profile.followersCount), label: "Followers")
                        statView(count: Int64(profile.followingCount), label: "Following")
                    }
                    
                    Spacer()
                }
                .padding(.horizontal)
                
                
                HStack(alignment: .top) {
                    // MARK: - Name + Bio
                    VStack(alignment: .leading, spacing: 4) {
                        Text(profile.name)
                            .font(.headline)
                        Text(profile.bio)
                            .font(.subheadline)
                            .foregroundColor(.gray)
                        
                    }
                    Spacer() // 🚀 добавим отступ между текстом и кнопкой
                    
                    // MARK: - Edit Profile Button
                    Button(action: onProfileButtonClick) {
                        Text(buttonText)
                            .font(.subheadline)
                            .foregroundColor(Color.primary)
                            .padding(.horizontal, 12)
                            .padding(.vertical, 6)
                            .background(
                                isFilled ? Color.accentColor : Color.clear
                            )
                            .overlay(
                                RoundedRectangle(cornerRadius: 12)
                                    .stroke(
                                        colorScheme == .dark ? Color.white.opacity(0.8) : Color.black.opacity(0.8),
                                        lineWidth: isOutlined ? 1 : 0
                                    )
                                //.stroke(Color.black, lineWidth: isOutlined ? 1 : 0)
                            )
                    }
                    .cornerRadius(12)
                }
                .padding(.horizontal)
                
                // MARK: - Post layout toggle
                /*ну
                HStack {
                    Image(systemName: "list.bullet")
                    Spacer()
                    Image(systemName: "square.grid.2x2")
                }
                .padding(.horizontal, 32)
                .padding(.vertical, 8)
                .foregroundColor(.black)
                */
                // MARK: - Post list
                LazyVStack {
                    ForEach(posts.indices, id: \.self) { index in
                        let post = posts[index]
                        PostItemView(
                            post: post,
                            onProfileClick: onProfileClick,
                            onLikeClick: onLikeClick,
                            onCommentClick: onCommentClick,
                            onPostMoreIconClick: onPostMoreIconClick
                        )
                        .onAppear {
                            if index == posts.count - 1 {
                                onLoadMorePosts?()
                            }
                        }
                        
                    }
                }
            }
        }
        
        
    }
    
    private func statView(count: Int64, label: String) -> some View {
        VStack {
            Text("\(count)")
                .font(.headline)
            Text(label)
                .font(.caption)
                .foregroundColor(.gray)
        }
    }

    
}

// MARK: - Preview


struct ProfileView_Previews: PreviewProvider {
    static var previews: some View {
        let sampleProfile = Profile(
            id: 1,
            name: "Jane Doe",
            bio: "iOS Developer and Swift enthusiast.",
            imageUrl: "https://burst.shopifycdn.com/photos/woman-in-glasses.jpg",
            followersCount: 12,
            followingCount: 150,
            isFollowing: false,
            isOwnProfile: false,
            postCount: 1
        )

        let samplePosts: [Post] = [
            Post(
                postId: 1,
                caption: "Exploring the mountains 🏞️",
                imageUrl: "https://burst.shopifycdn.com/photos/mountain-landscape.jpg",
                createdAt: "2h ago",
                likesCount: 34,
                commentsCount: 5,
                userId: 1,
                userName: "Jane Doe",
                userImageUrl: "https://burst.shopifycdn.com/photos/woman-in-glasses.jpg",
                isLiked: false,
                isOwnPost: true
            ),
            Post(
                postId: 2,
                caption: "Just finished a SwiftUI course!",
                imageUrl: "https://burst.shopifycdn.com/photos/writing-code.jpg",
                createdAt: "1d ago",
                likesCount: 78,
                commentsCount: 12,
                userId: 1,
                userName: "Jane Doe",
                userImageUrl: "https://burst.shopifycdn.com/photos/woman-in-glasses.jpg",
                isLiked: true,
                isOwnPost: true
            )
        ]

        return ProfileView(
            profile: sampleProfile,
            posts: samplePosts,
            onProfileButtonClick: {},
            onProfileClick: { _ in },
            onLikeClick: { _ in },
            onCommentClick: { _ in },
            onPostMoreIconClick: { _ in },
            onLoadMorePosts: {}
        )
        .previewDisplayName("ProfileView Preview")
    }
}


