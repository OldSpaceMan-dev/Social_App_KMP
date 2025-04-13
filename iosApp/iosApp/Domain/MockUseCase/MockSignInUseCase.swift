//
//  MockSignInUseCase.swift
//  iosApp
//
//  Created by Arkadiy Blank on 25.03.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import shared

class MockSignInUseCase {
    
    // Определите, какой результат должна возвращать заглушка
    var shouldReturnSuccess = true
    var errorMessage = "Неизвестная ошибка"
    
    func invoke(email: String, password: String) async throws -> AuthResultDataMock {
        if shouldReturnSuccess {
            // Заглушка для успешной авторизации
            return AuthResultDataMock(
                id: 123,
                name: "Test User",
                bio: "Test Bio",
                avatar: nil,
                token: "test_token",
                followersCount: 10,
                followingCount: 20
            ) 
        } else {
            // Заглушка для ошибки авторизации
            throw NSError(domain: "Auth", code: 401, userInfo: [NSLocalizedDescriptionKey: errorMessage])
        }
    }
}



struct AuthResultDataMock {
    let id: Int64 // Long в Kotlin соответствует Int64 в Swift
    let name: String
    let bio: String
    let avatar: String?
    let token: String
    let followersCount: Int
    let followingCount: Int
}
