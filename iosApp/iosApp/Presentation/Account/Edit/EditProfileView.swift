//
//  EditProfileView.swift
//  iosApp
//
//  Created by Arkadiy Blank on 20.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import PhotosUI
import shared


struct EditProfileView: View {
    
    @StateObject var editProfileViewModel: EditProfileViewModel
    
    @AppStorage("authToken") var authToken: String?
    @State private var shouldLogout = false

    
    let profile: Profile
    let userId: Int64
    var onProfileUpdated: (() -> Void)? = nil // кол бек для закрытия ботомщита

    //let onUpload: (_ name: String, _ bio: String, _ imageData: Data?) -> Void
    //let onLogout: () -> Void

    @State private var name: String
    @State private var bio: String
    @State private var selectedImage: PhotosPickerItem? = nil
    @State private var profileImage: UIImage? = nil
    @State private var isLoading: Bool = false
    @State private var errorMessage: String? = nil

    init(
        profile: Profile,
        userId: Int64,
        onProfileUpdated: (() -> Void)? = nil
    ) {
        self.profile = profile
        self.userId = userId
        self.onProfileUpdated = onProfileUpdated
        
        _name = State(initialValue: profile.name)
        _bio = State(initialValue: profile.bio)
        _editProfileViewModel = StateObject(wrappedValue: EditProfileViewModel(userId: userId))
    }

    var body: some View {

        ScrollView {//NavigationView
            VStack(spacing: 24) {
                ZStack(alignment: .bottomTrailing) {
                    
                    // MARK: - profile info
                    if let profileImage = profileImage {
                        Image(uiImage: profileImage)
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                            .frame(width: 120, height: 120)
                            .clipShape(Circle())
                    } else { //TODO: Дополнить???
                        ProfileImage(imageUrl: profile.imageUrl, size: 120)
                    }
                    
                    
                    
                    PhotosPicker(selection: $selectedImage, matching: .images) {
                        Circle()
                            .fill(Color(.systemBackground))
                            .frame(width: 40, height: 40)
                            .overlay {
                                Image(systemName: "pencil")
                                    .foregroundColor(.blue)
                            }
                            .shadow(radius: 2)
                    }
                }
                
                TextField("Username", text: $name)
                    .padding()
                    .background(Color.gray.opacity(0.1))
                    .cornerRadius(8)
                
                ZStack(alignment: .topLeading) {
                    if bio.isEmpty {
                        Text("Bio")
                            .foregroundColor(.gray)
                            .padding(.horizontal, 12)
                            .padding(.vertical, 10)
                    }
                    
                    TextEditor(text: $bio)
                        .frame(height: 100)
                        .padding(8)
                        .background(Color.gray.opacity(0.1))
                        .cornerRadius(8)
                }
                // MARK: - Обновление профиля
                Button(action: {
                    editProfileViewModel.isLoading = true
                    
                    Task {
                        do {
                            var imageData: Data?
                            if let selectedImage = selectedImage {
                                imageData = try await selectedImage.loadTransferable(type: Data.self)
                            }
                            await editProfileViewModel.updateProfile(
                                name: name,
                                bio: bio,
                                imageData: imageData
                            )
                            await MainActor.run {
                                editProfileViewModel.isLoading = false
                                onProfileUpdated?() 
                                
                            }
                        } catch {
                            await MainActor.run {
                                editProfileViewModel.errorMessage = error.localizedDescription
                                editProfileViewModel.isLoading = false
                            }
                        }
                        
                        
                        
                    }
                }) {
                    if editProfileViewModel.isLoading {
                        ProgressView()
                            .frame(maxWidth: .infinity)
                            .frame(height: 44)
                            .background(Color.blue)
                            .cornerRadius(8)
                        
                    } else {
                        
                        Text("Upload Changes")
                            .frame(maxWidth: .infinity)
                            .frame(height: 44)
                            .background(Color.blue)
                            .foregroundColor(.white)
                            .cornerRadius(8)
                    }
                }
                
                // MARK: - Delete - Logout
                Button(role: .destructive, action: {
                    authToken = nil
                    UserDefaults.standard.removeObject(forKey: "authToken")
                    UserDefaults.standard.removeObject(forKey: "profileId")
                    shouldLogout = true
                    //onLogout()
                }) {
                    Text("Logout")
                        .frame(maxWidth: .infinity)
                        .frame(height: 44)
                        .background(Color.red.opacity(0.2))
                        .foregroundColor(.red)
                        .cornerRadius(8)
                }
                
                if let error = errorMessage {
                    Text(error)
                        .foregroundColor(.red)
                }
                
                Spacer()
            }
            .padding()
            .navigationTitle("Edit Profile")
            .onAppear {
                Task {
                    await editProfileViewModel.fetchProfile()
                }
            }
            // MARK: - загрузка фото
            .onChange(of: selectedImage) { newItem in
                if let newItem = newItem {
                    Task {
                        if let data = try? await newItem.loadTransferable(type: Data.self),
                           let uiImage = UIImage(data: data) {
                            profileImage = uiImage
                        }
                    }
                }
            }
            
        }
        .hideKeyboardOnTap()
    }
}



struct EditProfileView_Previews: PreviewProvider {
    static var previews: some View {
        let sampleProfile = Profile(
            id: 1,
            name: "Jane Doe",
            bio: "iOS Developer and Swift enthusiast.",
            imageUrl: "https://burst.shopifycdn.com/photos/woman-in-glasses.jpg",
            followersCount: 12,
            followingCount: 150,
            isFollowing: false,
            isOwnProfile: true,
            postCount: 1
        )

        EditProfileView(
            profile: sampleProfile,
            userId: 1
            //onUpload: { name, bio, imageData in
                //print("Preview Upload: \(name), \(bio), imageData: \(imageData != nil ? "Yes" : "No")")
            //}
            /*,
            onLogout: {
                print("Preview Logout tapped")
            }*/
        )
    }
}



