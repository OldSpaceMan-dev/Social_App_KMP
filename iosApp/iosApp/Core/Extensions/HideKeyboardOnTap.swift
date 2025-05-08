//
//  HideKeyboardOnTap.swift
//  iosApp
//
//  Created by Arkadiy Blank on 20.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI


extension View {
    func hideKeyboard() {
        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder),
                                        to: nil, from: nil, for: nil)
    }

    func hideKeyboardOnTap() -> some View {
        self.background(
            TapToDismissKeyboard()
        )
    }
}

struct TapToDismissKeyboard: UIViewRepresentable {
    func makeUIView(context: Context) -> UIView {
        let view = UIView()
        let tap = UITapGestureRecognizer(target: context.coordinator,
                                         action: #selector(Coordinator.dismissKeyboard))
        view.addGestureRecognizer(tap)
        return view
    }

    func updateUIView(_ uiView: UIView, context: Context) {}

    func makeCoordinator() -> Coordinator {
        Coordinator()
    }

    class Coordinator {
        @objc func dismissKeyboard() {
            UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder),
                                            to: nil, from: nil, for: nil)
        }
    }
}
