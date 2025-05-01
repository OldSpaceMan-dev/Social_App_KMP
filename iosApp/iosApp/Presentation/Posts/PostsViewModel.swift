//
//  Untitled.swift
//  iosApp
//
//  Created by Arkadiy Blank on 30.03.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import shared
import Combine

//extension PostListView { 
 class PostsViewModel: ObservableObject {
       
      
     @Published var posts: [Post] = []
     @Published var isLoading = false
     @Published var errorMessage: String?
     @Published var endReached = false
      
     
     @Published private(set) var pagingManager: PagingManager<Post>
        
        
     private let getPostsUseCase = KoinIOSHelper().getPostsUseCase()
     
     
     
 
     init() {
         pagingManager = PagingManager<Post>( pageSize: Constants.defaultRequestPageSize )
         {
             [weak getPostsUseCase] page, pageSize in
             guard let getPostsUseCase else { return [] }
             
             let result = try await getPostsUseCase.invoke(page: Int32(page), pageSize: Int32(pageSize))
             
             let data =  result.data as? [Post] ?? []
             print("Loaded page \(page): \(data.count) posts") // <- проверь это
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
     
     
     func loadMorePosts() async   {
         
         await pagingManager.loadNextItems()
         
     }
     
     
     
}

