//
//  Untitled.swift
//  iosApp
//
//  Created by Arkadiy Blank on 06.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//


import SwiftUI

struct PostImage: View {
    let imageUrl: String?

    @State private var imageLoaded = false
    @State private var timerFinished = false

    var body: some View {
        ZStack {
            if imageLoaded {
                AsyncImage(
                    url: URL(string: (imageUrl ?? "").toCurrentUrl()),
                    content: { phase in
                        switch phase {
                        case .empty:
                            ProgressView() // Пока идет загрузка
                        case .success(let image):
                            image
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                                .frame(maxWidth: .infinity, maxHeight: 300)
                                .clipped()
                                .onAppear {
                                    imageLoaded = true // Отметка, что картинка загрузилась
                                }
                        case .failure:
                            Image(systemName: "photo") // Картинка по умолчанию при ошибке
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(maxWidth: .infinity, maxHeight: 300)
                                .foregroundColor(.gray)
                        @unknown default:
                            EmptyView()
                        }
                    }
                )
            }
             else if timerFinished {
                Image(systemName: "photo") // Картинка по умолчанию, если не загрузилась за 1 сек
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(maxWidth: .infinity, maxHeight: 300)
                    .foregroundColor(.gray)
            }
              else {
                  ProgressView() // 1 секунду
                     .onAppear {
                         DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                             timerFinished = true
                             imageLoaded = true
                         }
                     }
                     
                     
                    
            }
        }
        .frame(maxWidth: .infinity, maxHeight: 300)
    }
}


struct PostImage_Previews: PreviewProvider {
    static var previews: some View {
        PostImage(imageUrl: "")
            .previewLayout(.sizeThatFits)
    }
}
