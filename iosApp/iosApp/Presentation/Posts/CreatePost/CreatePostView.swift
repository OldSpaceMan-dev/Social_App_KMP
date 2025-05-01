//
//  CreatePostView.swift
//  iosApp
//
//  Created by Arkadiy Blank on 27.04.2025.
//  Copyright © 2025 orgName. All rights reserved.



import SwiftUI
import PhotosUI
import shared

struct CreatePostView: View {
    
    @Binding var selectedTab: MainTabView.Tab // <- 🔥 принять биндинг
    
    @StateObject private var viewModel = CreatePostViewModel()
    
    @State private var selectedImageItem: PhotosPickerItem? = nil
    @State private var selectedImage: UIImage? = nil
    @State private var caption: String = ""
    

    
    //@State private var hasContent: Bool = false // <-- добавляем переменную состояния
    /*var hasContent: Bool {
        selectedImage != nil || !caption.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
    }*/
    
    var body: some View {
   
          
        NavigationStack{
            
            ScrollView {
                VStack(spacing: 24) {
                    // MARK: - Image Picker
                    ZStack(alignment: .bottomTrailing) {
                        if let selectedImage = selectedImage {
                            Image(uiImage: selectedImage)
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                                .frame(width: 200, height: 200)
                                .clipShape(RoundedRectangle(cornerRadius: 12))
                        } else {
                            RoundedRectangle(cornerRadius: 12)
                                .fill(Color.gray.opacity(0.2))
                                .frame(width: 200, height: 200)
                                .overlay(
                                    Text("Select Image")
                                        .foregroundColor(.gray)
                                )
                        }
                        
                        PhotosPicker(selection: $selectedImageItem, matching: .images) {
                            Circle()
                                .fill(Color.white)
                                .frame(width: 40, height: 40)
                                .overlay {
                                    Image(systemName: "photo")
                                        .foregroundColor(.blue)
                                }
                                .shadow(radius: 2)
                        }
                        .padding(8)
                    }
                    
                    // MARK: - Caption
                    TextField("Enter caption...", text: $caption)
                        .padding()
                        .background(Color.gray.opacity(0.1))
                        .cornerRadius(8)
                        .onSubmit {
                            hideKeyboard()
                        }
                    
                    // MARK: - Upload Button
                    Button(action: {
                        Task {
                            viewModel.caption = caption
                            viewModel.selectedImageData = selectedImage?.jpegData(compressionQuality: 0.8)
                            await viewModel.createPost()
                        }

                    }) {
                        if viewModel.isLoading {
                            ProgressView()
                                .frame(maxWidth: .infinity)
                                .frame(height: 44)
                                .background(Color.blue)
                                .cornerRadius(8)
                        } else {
                            Text("Upload Post")
                                .frame(maxWidth: .infinity)
                                .frame(height: 44)
                                .background(Color.blue)
                                .foregroundColor(.white)
                                .cornerRadius(8)
                        }
                    }
                    .disabled(viewModel.isLoading)
                    
                    // MARK: - Error Message
                    if let error = viewModel.errorMessage {
                        Text(error)
                            .foregroundColor(.red)
                            .multilineTextAlignment(.center)
                    }
                    
                    Spacer()
                }
            }
            .padding()
            .navigationTitle("Create Post")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    if selectedImage != nil || !caption.isEmpty {
                        Button("Cancel") {
                            //viewModel.resetFields()
                            selectedImage = nil
                            selectedImageItem = nil
                            caption = ""
                            hideKeyboard()
                        }
                        .foregroundColor(.red)
                    }
                    
                }
            }
            .onChange(of: selectedImageItem) { newItem in
                if let newItem = newItem {
                    Task {
                        if let data = try? await newItem.loadTransferable(type: Data.self),
                           let uiImage = UIImage(data: data) {
                            selectedImage = uiImage
                        }
                    }
                }
            }
            
        }
        .onTapGesture {
            hideKeyboard()
        }
        .onChange(of: viewModel.postCreatedSuccessfully) { isCreated in // NEW
            if isCreated {
                viewModel.resetFields()
                selectedImage = nil
                selectedImageItem = nil
                caption = ""
                selectedTab = .home // 🔥 после успешного создания поста переключаемся на Home
            }
        }
    }
    
}


struct CreatePostView_Previews: PreviewProvider {
    @State static var selectedTab: MainTabView.Tab = .home

    static var previews: some View {
        CreatePostView(selectedTab: $selectedTab)
    }
}
