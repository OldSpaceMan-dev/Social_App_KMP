//
//  ProfileImage.swift
//  iosApp
//
//  Created by Arkadiy Blank on 06.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct ProfileImage: View {
    let imageUrl: String?
    var size: CGFloat = 30

    var body: some View {
        AsyncImage(url: URL(string: (imageUrl ?? "").toCurrentUrl())) { phase in
            switch phase {
            case .empty:
                Image(systemName: "person.circle.fill")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: size, height: size)
                    .foregroundColor(.gray)
            case .success(let image):
                image
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .frame(width: size, height: size)
                    .clipShape(Circle())
            case .failure(_):
                Image(systemName: "person.circle.fill")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: size, height: size)
                    .foregroundColor(.gray)
            @unknown default:
                EmptyView()
            }
        }
        .clipShape(Circle())
    }
}

struct ProfileImage_Previews: PreviewProvider {
    static var previews: some View {
        ProfileImage(imageUrl: "")
            .previewLayout(.sizeThatFits)
    }
}



/*
import SwiftUI

struct ProfileImage: View {
    let imageUrl: String?
    var size: CGFloat = 30


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
                                .frame(width: size, height: size)
                        */
                         case .success(let image):
                            image
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                                .frame(width: size, height: size)
                                .clipShape(Circle())
                                .onAppear {
                                    imageLoaded = true // Отметка, что картинка загрузилась
                                }
                        case .failure:
                            Image(systemName: "person.circle.fill") // Картинка по умолчанию при ошибке
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: size, height: size)
                                .foregroundColor(.gray)
                        @unknown default:
                            EmptyView()
                        }
                    }
                )
            } else if timerFinished {
                Image(systemName: "person.circle.fill") // Картинка по умолчанию, если не загрузилась за 1 сек
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: size, height: size)
                    .foregroundColor(.gray)
            } else {
                ProgressView() // ==1 секунду== сразу
                    .onAppear {
                        DispatchQueue.main.asyncAfter(deadline: .now()) {
                            timerFinished = true
                            imageLoaded = true
                        }
                    }
            }
        }
        //.frame(width: 30, height: 30)
        .clipShape(Circle()) // Обрезаем в круг
    }
    

}

struct ProfileImage_Previews: PreviewProvider {
    static var previews: some View {
        ProfileImage(imageUrl: "")
            .previewLayout(.sizeThatFits)
    }
}
*/
