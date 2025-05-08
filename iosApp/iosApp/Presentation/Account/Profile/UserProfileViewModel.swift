//
//  UserProfileViewModel.swift
//  iosApp
//
//  Created by Arkadiy Blank on 17.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import shared

class UserProfileViewModel: ObservableObject {
    @Published var profile: Profile? = nil
    @Published var isLoading = false
    @Published var errorMessage: String?

    private let getProfileUseCase = KoinIOSHelper().getProfileUseCase()
    private let userId: Int64

    init(userId: Int64) {
        self.userId = userId
    }

    func loadProfile() async {
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
}
