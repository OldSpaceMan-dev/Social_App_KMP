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
     
    /*//пагинация
     private var currentPage = 1
     private let pageSize = 5
     private var isFetching = false
     */
     
     
     
     
     func loadInitialPosts() async {
         pagingManager.reset()
         await pagingManager.loadNextItems()
     }
     
     
     func loadMorePosts() async   {
         
         await pagingManager.loadNextItems()
         
         /*
         guard !isFetching && !endReached else { return }
         
         isFetching = true
         
         await MainActor.run {
             isLoading = true
             errorMessage = nil
         }
         
         
         do {
             let result = try await getPostsUseCase.invoke(page: Int32(currentPage), pageSize: Int32(pageSize))
             let newPosts = result.data as? [Post] ?? []
             
             await MainActor.run { // Выполняем в главном потоке
                 posts += newPosts
                 endReached = newPosts.count < pageSize
                 isLoading = false
                 currentPage += 1
                 //posts = result.data as! [Post] // Обновляем UI
             }
             
             
         } catch {
             await MainActor.run {  // Выполняем в главном потоке
                 errorMessage = error.localizedDescription
                 isLoading = false
             }
         }
         
         isFetching = false
         //await MainActor.run{
             
           //  isLoading = false //Обновляем UI
             
         //}
          */
         
     }
     
     
     
}

