//
//  CreatePostViewModel.swift
//  iosApp
//
//  Created by Arkadiy Blank on 27.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//


import Foundation
import shared

@MainActor // 🔥 Важно! Всё во вью-модели будет на главном потоке
class CreatePostViewModel: ObservableObject {
    
    
    private let createPostUseCase = KoinIOSHelper().createPostUseCase()
    
    @Published var caption: String = ""
    @Published var selectedImageData: Data? = nil
    @Published var isLoading: Bool = false
    @Published var errorMessage: String? = nil
    @Published var postCreatedSuccessfully: Bool = false
    
    
    func createPost() async {
        guard let selectedImageData = selectedImageData else {
            errorMessage = "Please select an image."
            return
        }

        isLoading = true
        errorMessage = nil

        let kotlinByteArray = selectedImageData.toKotlinByteArray()

        do {
            let result = try await createPostUseCase.invoke(caption: caption, imageBytes: kotlinByteArray)

            if let success = result as? ResultSuccess<Post> {
                postCreatedSuccessfully = true
            } else if let error = result as? ResultError<Post> {
                errorMessage = error.message ?? "Unknown error"
            }
        } catch {
            errorMessage = error.localizedDescription
        }

        isLoading = false
    }

    func resetFields() {
        caption = ""
        selectedImageData = nil
        errorMessage = nil
        postCreatedSuccessfully = false
    }
}
