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
- [SocialApp_BackEndSide](https://github.com/OldSpaceMan-dev/SocialApp_BackEndSide) 

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
### Homepage
<img src="https://github.com/user-attachments/assets/04ee30b9-d8bf-46b2-a9dd-f50615a0bd15" alt="Android Screenshot Android1" width="200" />
<img src="https://github.com/user-attachments/assets/bb44735d-a55b-42a7-b402-89cf5fd0b4f2" alt="Android Screenshot Android1" width="200" />

<img src="https://github.com/user-attachments/assets/fe50e70a-d6b9-4598-8bfd-3137972261df" alt="iOS Screenshot" width="200" />
<img src="https://github.com/user-attachments/assets/ff957e2a-e87d-4a70-a755-fb9160de1dc9" alt="iOS Screenshot" width="200" />


---
### Post
<img src="https://github.com/user-attachments/assets/2c2e7888-dd37-46ad-a7cf-f9c6fa9a4ff9" alt="Android Screenshot Android1" width="200" />
<img src="https://github.com/user-attachments/assets/87a29dbe-9043-4f62-b317-3cb635fcc894" alt="Android Screenshot Android1" width="200" />

<img src="https://github.com/user-attachments/assets/c6da902e-c2c7-448d-9ba4-ae0a8590881e" alt="iOS Screenshot" width="200" />
<img src="https://github.com/user-attachments/assets/5ee5c2ac-b577-433b-9717-9e76e88e2f4f" alt="iOS Screenshot" width="200" />

---
### Profile
<img src="https://github.com/user-attachments/assets/8f9a2297-0429-4611-8bd7-a5f220819e49" alt="Android Screenshot Android1" width="200" />
<img src="https://github.com/user-attachments/assets/37776e88-5e7f-4855-ab78-f207146e27fa" alt="Android Screenshot Android1" width="200" />


<img src="https://github.com/user-attachments/assets/387a1c0e-798f-409b-964c-ac9076b87881" alt="iOS Screenshot" width="200" />
<img src="https://github.com/user-attachments/assets/78af48bd-2d49-43c1-8a92-d304fa83639b" alt="iOS Screenshot" width="200" />









