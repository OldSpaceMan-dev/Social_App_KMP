//
//  ToKotlinByteArray.swift
//  iosApp
//
//  Created by Arkadiy Blank on 26.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import shared
import Foundation

extension Data {
    func toKotlinByteArray() -> KotlinByteArray {
        let byteArray = KotlinByteArray(size: Int32(self.count))
        for (index, byte) in self.enumerated() {
            byteArray.set(index: Int32(index), value: Int8(bitPattern: byte))
        }
        return byteArray
    }
}
