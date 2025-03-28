//
//  KeychainService.swift
//  iosApp
//
//  Created by Arkadiy Blank on 26.03.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import Security // Импортируем фреймворк для работы с Keychain

// Простой сервис для работы с Keychain
// В реальном приложении стоит добавить больше обработки ошибок и, возможно, использовать библиотеку-обертку
struct KeychainService {

    // Уникальный идентификатор для нашего сервиса/приложения в Keychain
    private static let serviceIdentifier = "com.example.KMM-SocialMediaApp.AuthToken" // Замените на ваш Bundle ID или уникальную строку

    // Ключ, под которым будет храниться токен
    private static let accountKey = "userAuthToken"

    // Сохранение токена
    static func saveToken(_ token: String) -> Bool {
        guard let tokenData = token.data(using: .utf8) else {
            print("Keychain Error: Could not convert token to data.")
            return false
        }

        // Запрос для поиска существующего элемента
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: serviceIdentifier,
            kSecAttrAccount as String: accountKey
        ]

        // Атрибуты для обновления или добавления
        let attributes: [String: Any] = [
            kSecValueData as String: tokenData
        ]

        // Пытаемся обновить существующий элемент
        let statusUpdate = SecItemUpdate(query as CFDictionary, attributes as CFDictionary)

        // Если элемента нет (errSecItemNotFound), пытаемся добавить новый
        if statusUpdate == errSecItemNotFound {
            var addQuery = query
            addQuery[kSecValueData as String] = tokenData
            // kSecAttrAccessible - важно для определения, когда доступен элемент
            // .whenUnlocked - доступен, когда устройство разблокировано (хороший баланс)
            addQuery[kSecAttrAccessible as String] = kSecAttrAccessibleWhenUnlocked

            let statusAdd = SecItemAdd(addQuery as CFDictionary, nil)
            if statusAdd == noErr {
                print("Keychain: Token saved successfully.")
                return true
            } else {
                print("Keychain Error: Could not add token. Status: \(statusAdd)")
                return false
            }
        } else if statusUpdate != noErr {
            print("Keychain Error: Could not update token. Status: \(statusUpdate)")
            return false
        } else {
            print("Keychain: Token updated successfully.")
            return true
        }
    }

    // Получение токена
    static func loadToken() -> String? {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: serviceIdentifier,
            kSecAttrAccount as String: accountKey,
            kSecReturnData as String: kCFBooleanTrue!, // Говорим, что хотим получить данные
            kSecMatchLimit as String: kSecMatchLimitOne // Нам нужен только один результат
        ]

        var dataTypeRef: AnyObject? = nil

        // Выполняем поиск
        let status: OSStatus = SecItemCopyMatching(query as CFDictionary, &dataTypeRef)

        if status == noErr {
            guard let tokenData = dataTypeRef as? Data,
                  let token = String(data: tokenData, encoding: .utf8) else {
                print("Keychain Error: Could not convert loaded data to token string.")
                return nil
            }
            print("Keychain: Token loaded successfully.")
            return token
        } else if status == errSecItemNotFound {
             print("Keychain: Token not found.")
            return nil // Токена нет - это нормальная ситуация
        } else {
            print("Keychain Error: Could not load token. Status: \(status)")
            return nil
        }
    }

    // Удаление токена
    static func deleteToken() -> Bool {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: serviceIdentifier,
            kSecAttrAccount as String: accountKey
        ]

        // Выполняем удаление
        let status = SecItemDelete(query as CFDictionary)

        if status == noErr || status == errSecItemNotFound { // Успех, если удалено или и так не было
             print("Keychain: Token deleted successfully (or was not found).")
            return true
        } else {
            print("Keychain Error: Could not delete token. Status: \(status)")
            return false
        }
    }
}
