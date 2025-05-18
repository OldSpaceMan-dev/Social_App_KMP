# Social_App_KMP

**Social_App_KMP** — это кроссплатформенное социальное мобильное приложение, разработанное с использованием **Kotlin Multiplatform Mobile (KMM)**. 
Приложение поддерживает платформы **Android** и **iOS**, имеет собственный backend, и реализует основные функции соцсетей: регистрация, публикация постов, лайки, профиль и многое другое.

## 🔧 Стек технологий

### Общий (Shared module)
- **Kotlin Multiplatform (KMM)**
- **Ktor Client** — для сетевых запросов
- **Koin** — для Dependency Injection
- **Coroutines** — асинхронность
- **Serialization** — парсинг JSON
- для ios [SKIE](https://skie.touchlab.co/intro) 

### Android
- **Jetpack Compose**
- **Navigation Compose**
- **Coil** — загрузка изображений

### iOS
- **SwiftUI**
- **KMM ViewModels** через `@ObservableObject`
- Интеграция общего слоя через Swift/Kotlin interoperability



### Backend
- **Ktor Server**
- **Exposed** — ORM для работы с PostgreSQL
- **PostgreSQL** — база данных
- **Koin** — для внедрения зависимостей

## ✨ Возможности
- Регистрация и авторизация
- Просмотр списка постов
- Создание и удаление постов
- Лайк/дизлайк постов
- Просмотр и редактирование профиля
- Адаптивный UI под Android и iOS
- Локальное хранение токена доступа

## 🏗️ Структура проекта


## 🌄 Скрины 
