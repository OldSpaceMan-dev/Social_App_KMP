//
//  UserPostsViewModel.swift
//  iosApp
//
//  Created by Arkadiy Blank on 12.04.2025.
//  Copyright © 2025 orgName. All rights reserved.
//
import Foundation
import shared
import Combine

class UserPostsViewModel: ObservableObject {
    
    
    @Published var posts: [Post] = []
    @Published var isLoading = false
    @Published var errorMessage: String?
    @Published var endReached = false
    
    @Published private(set) var pagingManager: PagingManager<Post>
    
    private let getUserPostsUseCase = KoinIOSHelper().getUserPostsUseCase()
    
    private var userId: Int64  // ID
    
    init(userId: Int64) {
        self.userId = userId
        pagingManager = PagingManager<Post>(pageSize: Constants.defaultRequestPageSize)
        {
            [weak getUserPostsUseCase] page, pageSize in
            guard let getUserPostsUseCase else { return [] }
            
            let result = try await getUserPostsUseCase.invoke(
                userId: userId,
                page: Int32(page),
                pageSize: Int32(pageSize)
            )
            
            let data = result.data as? [Post] ?? []
            print("🔄 Loaded page \(page) for user \(userId): \(data.count) posts")
            return data
        }
        
        pagingManager.$items
            .receive(on: DispatchQueue.main)
            .assign(to: &$posts)
        
        pagingManager.$isLoading
            .receive(on: DispatchQueue.main)
            .assign(to: &$isLoading)
        
        pagingManager.$errorMessage
            .receive(on: DispatchQueue.main)
            .assign(to: &$errorMessage)
        
        pagingManager.$endReached
            .receive(on: DispatchQueue.main)
            .assign(to: &$endReached)
    }
    
    func loadInitialPosts() async {
        pagingManager.reset()
        await pagingManager.loadNextItems()
    }
    
    func loadMorePosts() async {
        await pagingManager.loadNextItems()
    }
    
}
