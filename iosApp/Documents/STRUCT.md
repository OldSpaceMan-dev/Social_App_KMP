#  Struct

## 1. Общее описание 

iosApp/
├── Application/
│   ├── iOSApp.swift
│   ├── AppDelegate.swift (опционально)
│   └── SceneDelegate.swift (опционально)
│
├── Core/
│   ├── DI/
│   │   └── DependencyProvider.swift
│   ├── Extensions/
│   │   ├── KMMExtensions.swift
│   │   └── ViewExtensions.swift
│   └── KMM/
│       └── KMMHelper.swift
│
├── Domain/
│   └── UseCases/
│       ├── AuthUseCases.swift
│       ├── LoginUseCase.swift
│       ├── LogoutUseCase.swift
│       ├── PostUseCases.swift
│       └── ...
│
├── Data/
│   └── Repositories/
│       ├── AuthRepository.swift
│       ├── AuthRepositoryImpl.swift
│       ├── PostRepository.swift
│       ├── PostRepositoryImpl.swift
│       └── ...
│
├── Presentation/
│   ├── Common/
│   │   ├── Views/
│   │   │   ├── LoadingView.swift
│   │   │   └── ErrorView.swift
│   │   └── ViewModels/
│   │       └── ObservableViewModel.swift
│   │
│   ├── Auth/
│   │   ├── ViewModels/
│   │   │   ├── AuthViewModel.swift
│   │   │   ├── LoginViewModel.swift
│   │   │   └── SignupViewModel.swift
│   │   └── Views/
│   │       ├── AuthView.swift
│   │       ├── LoginView.swift
│   │       ├── SignupView.swift
│   │       └── ForgotPasswordView.swift
│   │
│   ├── Posts/
│   │   ├── ViewModels/
│   │   │   └── PostsViewModel.swift
│   │   └── Views/
│   │       ├── PostListView.swift
│   │       ├── PostItemView.swift
│   │       └── PostDetailView.swift
│   │
│   └── Comments/
│       ├── ViewModels/
│       │   └── CommentsViewModel.swift
│       └── Views/
│           └── CommentItemView.swift
│
├── Resources/
│   ├── Assets.xcassets
│   └── Info.plist
│
├── Preview\ Content/
│   └── PreviewDataFactory.swift
│
└── Podfile


