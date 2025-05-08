//
//  PostLikesCommentView.swift
//  iosApp
//
//  Created by Arkadiy Blank on 02.05.2025.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import shared


struct PostLikesRowView: View {
    let likesCount: Int
    let commentsCount: Int
    let onLikeClick: () -> Void
    let isPostLiked: Bool
    let onCommentClick: () -> Void

    var body: some View {
        HStack {
            Button(action: onLikeClick) {
                Image(systemName: isPostLiked ? "heart.fill" : "heart")
                    .foregroundColor(isPostLiked ? .red : .gray)
            }
            Text("\(likesCount)")

            Spacer()

            Button(action: onCommentClick) {
                Image(systemName: "bubble.right")
                    .foregroundColor(.gray)
            }
            Text("\(commentsCount)")
        }
        .padding(.horizontal, 16)
        .padding(.bottom, 8)
    }
}

extension AsyncImage {
    func circleImageModifier(size: CGFloat) -> some View {
        self
            .frame(width: size, height: size)
            .clipShape(Circle())
    }
}




struct PostLikesRowView_Previews: PreviewProvider {
    static var previews: some View {
        PostLikesRowView(
            likesCount: 12,
            commentsCount: 2,
            onLikeClick: {},
            isPostLiked: true,
            onCommentClick: {}
        )
        .previewLayout(.sizeThatFits)
    }
}
