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


struct ToastMessage {
    let text: String
    let isError: Bool
}

class UserPostsViewModel: ObservableObject {
    
    @Published var toastMessage: ToastMessage? = nil
    @Published var posts: [Post] = []
    @Published var isLoading = false
    @Published var errorMessage: String?
    @Published var endReached = false
    
    @Published private(set) var pagingManager: PagingManager<Post>
    
    
    private let getUserPostsUseCase = KoinIOSHelper().getUserPostsUseCase()
    private let likeOrUnlikePostUseCase = KoinIOSHelper().likeOrUnlikePostUseCase()
    
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
    
    
    func toggleLike(post: Post) async {
        guard let index = posts.firstIndex(where: { $0.postId == post.postId }) else { return }
        
        let originalPost = post
        let newIsLiked = !post.isLiked
        let newLikesCount = post.likesCount + (newIsLiked ? 1 : -1)
        
        // Создаем копию поста через doCopy
        let updatedPost = post.doCopy(
            postId: post.postId,
            caption: post.caption,
            imageUrl: post.imageUrl,
            createdAt: post.createdAt,
            likesCount: newLikesCount,
            commentsCount: post.commentsCount,
            userId: post.userId,
            userName: post.userName,
            userImageUrl: post.userImageUrl,
            isLiked: newIsLiked,
            isOwnPost: post.isOwnPost
        )
        
        // Оптимистично обновляем UI
        DispatchQueue.main.async {
            self.posts[index] = updatedPost
            // Показываем тост при успешном лайке
            self.toastMessage = ToastMessage(
                text: "Вы лайкнули пост '\(post.caption.prefix(30))', в нем \(newLikesCount) лайков",
                isError: false
            )
        }
        
        do {
            let result = try await likeOrUnlikePostUseCase.invoke(post: post)
            if result is ResultError {
                // Откатываем изменения при ошибке
                DispatchQueue.main.async {
                    self.posts[index] = originalPost
                    self.toastMessage = ToastMessage(
                        text: "Не удалось обновить лайк",
                        isError: true
                    )
                }
            }
        } catch {
            // Откатываем изменения при исключении
            DispatchQueue.main.async {
                self.posts[index] = originalPost
                self.toastMessage = ToastMessage(
                    text: error.localizedDescription,
                    isError: true
                )
            }
        }
        
    }
    
    
}
