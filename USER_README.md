# Android TV Downloader & File Manager

## 📋 Описание проекта

Готовое Android TV приложение на Kotlin для загрузки файлов из интернета и работы с локальными файлами.

## 🏗️ Структура проекта

```
downloader-tv/
├── app/
│   ├── build.gradle.kts           # Конфигурация модуля приложения
│   ├── src/main/
│   │   ├── AndroidManifest.xml    # Манифест с TV настройками
│   │   ├── java/com/mopsicus/downloadertv/
│   │   │   ├── MainActivity.kt            # Главная активность с навигацией
│   │   │   ├── DownloadFragment.kt        # Фрагмент загрузки файлов
│   │   │   └── CopyFragment.kt            # Фрагмент работы с файлами
│   │   └── res/
│   │       ├── layout/
│   │       │   ├── activity_main.xml      # Layout главного экрана
│   │       │   ├── fragment_download.xml  # Layout загрузки
│   │       │   └── fragment_copy.xml      # Layout копирования
│   │       ├── values/
│   │       │   ├── strings.xml            # Строковые ресурсы
│   │       │   ├── colors.xml             # Цветовая схема
│   │       │   └── themes.xml             # Темы приложения
│   │       └── mipmap-*/                  # Иконки приложения
├── build.gradle.kts               # Корневой build файл
├── settings.gradle.kts            # Настройки Gradle
└── gradle/
    └── libs.versions.toml         # Версии зависимостей
```

## ✨ Функциональность

### 1. **MainActivity.kt**
- Управляет навигацией между фрагментами
- Левая панель с меню: "Загрузка" и "Копирование"
- ViewBinding для безопасной работы с UI
- Адаптирован для управления с пульта ДУ

### 2. **DownloadFragment.kt**
- Поле ввода URL с валидацией
- Проверка разрешений через Activity Result API
- Асинхронная загрузка файлов в директорию Downloads
- Индикация статуса и обработка ошибок
- Поддержка Android 10+ (Scoped Storage)

### 3. **CopyFragment.kt**
- Выбор файла через system File Picker (ACTION_OPEN_DOCUMENT)
- Кнопка "Открыть" - открывает файл через ACTION_VIEW
- Кнопка "Копировать" - копирует содержимое в буфер обмена
- Асинхронное чтение файлов
- Проверка размера файла (лимит 1MB для буфера обмена)

## 🔧 Технологии

- **Язык**: Kotlin
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 35
- **UI**: ViewBinding, XML Layouts
- **Async**: Kotlin Coroutines + lifecycleScope
- **Навигация**: Fragment-based
- **Разрешения**: Activity Result API

## 📦 Зависимости

```kotlin
- androidx.core:core-ktx:1.16.0
- androidx.appcompat:appcompat:1.7.1
- androidx.constraintlayout:constraintlayout:2.1.4
- androidx.leanback:leanback:1.0.0
- androidx.fragment:fragment-ktx:1.8.5
- androidx.lifecycle:lifecycle-runtime-ktx:2.8.7
- androidx.activity:activity-ktx:1.9.3
- material:1.11.0
```

## 🚀 Сборка и запуск

### Сборка APK:
```bash
JAVA_HOME=/path/to/java ./gradlew assembleDebug
```

### Установка на устройство:
```bash
./gradlew installDebug
```

### APK находится в:
```
app/build/outputs/apk/debug/app-debug.apk
```

## 📱 Разрешения

Приложение запрашивает следующие разрешения:
- `INTERNET` - для загрузки файлов
- `READ_EXTERNAL_STORAGE` (до API 32) - для чтения файлов
- `WRITE_EXTERNAL_STORAGE` (до API 28) - для сохранения файлов

## 🎮 TV оптимизация

- LEANBACK категория в манифесте
- Экран блокирован в landscape режиме
- Настроена навигация с пульта (D-pad)
- Темная тема для TV
- Крупные элементы UI
- nextFocus навигация между элементами

## 🎨 UI особенности

- **Темная тема** для комфортного просмотра на TV
- **Адаптивная навигация** с пульта
- **Крупные кнопки** для легкого попадания
- **Цветовая индикация** статусов (успех/ошибка)
- **Toast уведомления** для важных событий

## ✅ Проверено

- ✅ Компилируется без ошибок
- ✅ Соответствует API 24-34
- ✅ ViewBinding настроен
- ✅ Современный Kotlin код
- ✅ Документация в коде
- ✅ Обработка ошибок
- ✅ Корректная работа с разрешениями

## 📝 Примечания

1. Для работы загрузки файлов требуется подключение к интернету
2. Файлы сохраняются в публичную директорию Downloads
3. Размер файла для копирования ограничен 1MB
4. Приложение требует Java 11+ для сборки
5. Используется Gradle 8.10.2

## 🔐 Безопасность

- Валидация URL перед загрузкой
- Проверка размера файлов
- Обработка исключений при работе с сетью и файлами
- Правильная работа с Content URI
- Безопасная работа с буфером обмена

---

**Статус**: ✅ Готово к использованию и тестированию на Android TV устройствах
