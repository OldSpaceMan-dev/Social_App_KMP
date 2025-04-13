//
//  Untitled.swift
//  iosApp
//
//  Created by Arkadiy Blank on 05.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//



import Foundation
import shared

extension String {
    func toCurrentUrl() -> String {
        let currentBaseUrl = "http://192.168.1.107:8080/"
        if self.count >= 26 {
            let index = self.index(self.startIndex, offsetBy: 26)
            return currentBaseUrl + String(self[index...])
        } else {
            return currentBaseUrl + self
        }
    }
}
