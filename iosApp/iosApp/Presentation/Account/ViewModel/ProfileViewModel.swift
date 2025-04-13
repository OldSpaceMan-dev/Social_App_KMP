//
//  ProfileViewModel.swift
//  iosApp
//
//  Created by Arkadiy Blank on 06.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//




import Foundation
import shared

class ProfileViewModel: ObservableObject {
    @Published var profile: Profile? = nil
    @Published var isLoading = false
    @Published var errorMessage: String?
    
    private let getProfileUseCase = KoinIOSHelper().getProfileUseCase()
    
    
    //TODO передаю всегда только user id ииз авторизации- хардкод - нужно передавать любой userid 
  
    func loadProfile() async {
        isLoading = true
        errorMessage = nil
        
        
        /*
        guard let profileIdString = UserDefaults.standard.string(forKey: "profileId"),
              let profileId = Int64(profileIdString) else {
            //print("❗️Invalid profileId: \(UserDefaults.standard.string(forKey: "profileId") ?? "nil")")
            self.errorMessage = "Invalid profile ID"
            self.isLoading = false
            return
        }*/
        guard let profileIdString = UserDefaults.standard.string(forKey: "profileId") else {
            print("❗️profileId not found in UserDefaults")
            self.errorMessage = "Missing profile ID"
            self.isLoading = false
            return
        }

        guard let profileId = Int64(profileIdString) else {
            print("❗️Failed to convert profileId '\(profileIdString)' to Int64")
            self.errorMessage = "Invalid profile ID format"
            self.isLoading = false
            return
        }
        
        
        let flow = getProfileUseCase.invoke(profileId: profileId )
        
        Task {
           
            for try await result in flow {
                if let success = result as? ResultSuccess<Profile> {
                    print("✅ Profile loaded: \(String(describing: success.data?.name))")
                    await MainActor.run {
                        self.profile = success.data
                        self.isLoading = false
                    }
                } else if let failure = result as? ResultError<Profile> {
                    print("❌ Error loading profile: \(failure.message ?? "Unknown error")")
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
    

