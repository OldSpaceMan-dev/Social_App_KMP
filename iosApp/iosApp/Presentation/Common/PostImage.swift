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

    var body: some View {
        AsyncImage(url: URL(string: (imageUrl ?? "").toCurrentUrl())) { phase in
            switch phase {
            case .empty:
                Image(systemName: "photo")
                    .resizable()
                    .scaledToFit()
                    .frame(maxWidth: .infinity)
                    .foregroundColor(.gray)
            case .success(let image):
                image
                    .resizable()
                    .scaledToFit()
                    .frame(maxWidth: .infinity)
            case .failure(_):
                Image(systemName: "photo")
                    .resizable()
                    .scaledToFit()
                    .frame(maxWidth: .infinity)
                    .foregroundColor(.gray)
            @unknown default:
                EmptyView()
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


/*
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
                        /*case .empty:
                            ProgressView() // Пока идет загрузка
                        */
                         case .success(let image):
                            image
                                .resizable()
                                .scaledToFit() // Показываем полностью, не обрезая
                                .frame(maxWidth: .infinity)
                                .onAppear {
                                    imageLoaded = true // Отметка, что картинка загрузилась
                                }
                        case .failure:
                            Image(systemName: "photo") // Картинка по умолчанию при ошибке
                                .resizable()
                                .scaledToFit()
                                .frame(maxWidth: .infinity)
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
                    .scaledToFit()
                    .frame(maxWidth: .infinity)
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
*/
