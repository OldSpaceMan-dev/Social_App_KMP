//
//  ToastView.swift
//  iosApp
//
//  Created by Arkadiy Blank on 03.05.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct ToastView: View {
    let message: String
    let isError: Bool
    @Binding var isShowing: Bool
    
    var body: some View {
        VStack {
            Text(message)
                .padding()
                .foregroundColor(.white)
                .background(isError ? Color.red : Color.green)
                .cornerRadius(10)
                .shadow(radius: 5)
                .padding(.top, 20)
                .transition(.move(edge: .top).combined(with: .opacity))
                .onAppear {
                    // Автоматически скрываем тост через 3 секунды
                    DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
                        withAnimation {
                            isShowing = false
                        }
                    }
                }
            Spacer()
        }
        .padding(.horizontal)
        .zIndex(1) // Чтобы тост был поверх других элементов
    }
}

struct ToastView_Previews: PreviewProvider {
    static var previews: some View {
        ToastView(
            message: "Вы лайкнули пост 'Exploring the mountains', в нем 35 лайков",
            isError: false,
            isShowing: .constant(true)
        )
        .previewDisplayName("Success Toast")
        
        ToastView(
            message: "Не удалось обновить лайк",
            isError: true,
            isShowing: .constant(true)
        )
        .previewDisplayName("Error Toast")
    }
}
