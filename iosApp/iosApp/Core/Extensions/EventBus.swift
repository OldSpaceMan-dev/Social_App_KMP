//
//  EventBus.swift
//  iosApp
//
//  Created by Arkadiy Blank on 02.05.2025.
//  Copyright © 2025 orgName. All rights reserved.
//




import Foundation
import Combine
import shared

// Аналог sealed interface Event
enum Event {
    case postUpdated(Post)
    case postCreated(Post)
    case postDeleted(Post)
    case profileUpdated(Profile)
}

// Синглтон
final class EventBus {

    static let shared = EventBus()

    private init() {}

    // Shared поток событий
    let events = PassthroughSubject<Event, Never>()

    func send(_ event: Event) {
        events.send(event)
    }
}
