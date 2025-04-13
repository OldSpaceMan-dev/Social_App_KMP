//
//  PagingManager.swift
//  iosApp
//
//  Created by Arkadiy Blank on 12.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//


import Foundation

class PagingManager<T>: ObservableObject {
    
    @Published var items: [T] = []
    @Published var isLoading = false
    @Published var errorMessage: String?
    @Published var endReached = false

    private var currentPage = Constants.initialPageNumber
    private let pageSize: Int
    private var isRequestInProgress = false

    private let onRequest: @Sendable (_ page: Int, _ pageSize: Int) async throws -> [T]

    init(
        pageSize: Int = Constants.defaultRequestPageSize,
        onRequest: @escaping @Sendable (_ page: Int, _ pageSize: Int) async throws -> [T]
    ) {
        self.pageSize = pageSize
        self.onRequest = onRequest
    }

    func loadNextItems() async {
        guard !isRequestInProgress && !endReached else { return }
        
        isRequestInProgress = true
        isLoading = true
        errorMessage = nil
        
        do {
            let newItems = try await onRequest(currentPage, pageSize)
            items += newItems
            endReached = newItems.count < pageSize
            currentPage += 1
        } catch {
            errorMessage = Constants.unexpectedErrorMessage + "\n\(error.localizedDescription)"
        }
        
        isLoading = false
        isRequestInProgress = false
        
    }

    func reset() {
        currentPage = Constants.initialPageNumber
        endReached = false
        items = []
        errorMessage = nil
        
    }
    
}
