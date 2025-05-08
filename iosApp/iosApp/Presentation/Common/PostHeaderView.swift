//
//  CircleImage.swift
//  iosApp
//
//  Created by Arkadiy Blank on 01.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PostHeaderView: View {
    let name: String
    let profileUrl: String?
    let date: String
    let onProfileClick: () -> Void
    let onPostMoreIconClick: () -> Void

    var body: some View {
        HStack(alignment: .center, spacing: 8) {
            ProfileImage(imageUrl: profileUrl)
           

            VStack(alignment: .leading) {
                Text(name)
                    .font(.headline)
                    .onTapGesture {
                        onProfileClick()
                    }
                Text(date)
                    .font(.subheadline)
                    .foregroundColor(.gray)
            }
            Spacer()

            Button(action: onPostMoreIconClick) {
                Image(systemName: "ellipsis")
                    .foregroundColor(.gray)
            }
        }
        .padding(.horizontal, 16)
        .padding(.top, 8)
    }
}





struct PostHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        PostHeaderView(
            name: "Mr Smith",
            profileUrl: "https://images.pexels.com/photos/1704488/pexels-photo-1704488.jpeg",
            date: "20 min",
            onProfileClick: {},
            onPostMoreIconClick: {}
        )
        .previewLayout(.sizeThatFits)
    }
}
