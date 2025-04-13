//
//  KMMHelper.swift
//  iosApp
//
//  Created by Arkadiy Blank on 24.03.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import shared

class KMMHelper {
    static let shared = KMMHelper()
    
    private init() {}
    
    func initialize() {

        
        // Initialize KMM components if needed
        //KMMLoggerKt.doInitLogger() //- uncomment if you have this in shared
    }
    
    func handleKMMError(_ error: Any) -> Error {
        if let kmmerror = error as? KotlinThrowable {
            return NSError(domain: "KMM", code: 0, userInfo: [NSLocalizedDescriptionKey: kmmerror.message ?? "Unknown KMM error"])
        } else {
            return NSError(domain: "KMM", code: 0, userInfo: [NSLocalizedDescriptionKey: "Unknown error"])
        }
    }


    
}

