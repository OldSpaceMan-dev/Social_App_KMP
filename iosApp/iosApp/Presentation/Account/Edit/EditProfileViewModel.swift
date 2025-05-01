//
//  EditProfileViewModel.swift
//  iosApp
//
//  Created by Arkadiy Blank on 20.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import shared


class EditProfileViewModel: ObservableObject {
    
    
    @Published var profile: Profile? = nil
    @Published var isLoading = false
    @Published var errorMessage: String?
    @Published var uploadSucceeded: Bool = false
    
    private let getProfileUseCase = KoinIOSHelper().getProfileUseCase()
    private let updateProfileUseCase = KoinIOSHelper().updateProfileUseCase()
    private let userId: Int64
    
    
    
    init(userId: Int64) {
        self.userId = userId
    }

    func fetchProfile() async {
        isLoading = true
        errorMessage = nil

        let flow = getProfileUseCase.invoke(profileId: userId)
        Task {
            for try await result in flow {
                if let success = result as? ResultSuccess<Profile> {
                    await MainActor.run {
                        self.profile = success.data
                        self.isLoading = false
                    }
                } else if let failure = result as? ResultError<Profile> {
                    await MainActor.run {
                        self.errorMessage = failure.message ?? "Unknown error"
                        self.isLoading = false
                    }
                } else {
                    await MainActor.run {
                        self.errorMessage = "Unexpected result type"
                        self.isLoading = false
                    }
                }
            }
        }
    }
    
    
    
    
    func updateProfile(name: String, bio: String, imageData: Data?) async {
        guard let currentProfile = profile else { return }
                
        isLoading = true
        errorMessage = nil
        uploadSucceeded = false
        
        let updatedProfile = Profile(
            id: currentProfile.id,
            name: name,
            bio: bio,
            imageUrl: currentProfile.imageUrl, // не меняем на этом этапе
            followersCount: currentProfile.followersCount,
            followingCount: currentProfile.followingCount,
            isFollowing: currentProfile.isFollowing,
            isOwnProfile: currentProfile.isOwnProfile,
            postCount: currentProfile.postCount
        )


        let kotlinImageBytes: KotlinByteArray? = imageData?.toKotlinByteArray()


        do {
            let result = try await updateProfileUseCase.invoke(
                profile: updatedProfile,
                imageBytes: kotlinImageBytes
            )
            
            await MainActor.run {
                self.isLoading = false
                switch result {
                case let success as ResultSuccess<Profile>:
                    self.profile = success.data
                    self.uploadSucceeded = true
                case let error as ResultError<Profile>:
                    self.errorMessage = error.message ?? "Failed to update profile"
                default:
                    self.errorMessage = "Unexpected result"
                }
            }
        } catch {
            await MainActor.run {
                self.isLoading = false
                self.errorMessage = error.localizedDescription
            }
        }
    }
    
}
