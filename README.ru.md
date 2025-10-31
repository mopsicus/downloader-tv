<a href="./README.md">![Static Badge](https://img.shields.io/badge/english-118027)</a>
<a href="./README.ru.md">![Static Badge](https://img.shields.io/badge/русский-0390fc)</a>

<p align="center">
    <picture>
        <source media="(prefers-color-scheme: dark)" srcset="media/logo-dark.png">
        <source media="(prefers-color-scheme: light)" srcset="media/logo.png">
        <img alt="Downloader TV+" height="256" width="256" src="media/logo.png">
    </picture>
</p>

<h3 align="center">Downloader TV+</h3>
<h4 align="center">Инструмент для Android TV</h4>

<p align="center">
    <a href="#-быстрый-старт">Быстрый старт</a> · <a href="./RUNNING.md">Документация</a> · <a href="https://github.com/mopsicus/downloader-tv/issues">Сообщить об ошибке</a>
</p>

# 💬 Обзор

Downloader TV+ — это нативное Android приложение, разработанное для TV устройств, которое позволяет загружать файлы из интернета и копировать данные в буфер обмена. Создано на Kotlin и оптимизировано для навигации с пульта Android TV.

### Проблема

На устройствах Android TV часто отсутствуют удобные инструменты для загрузки файлов из интернета и доступа к локальным файлам. И есть одно действительно серьезное неудобство — когда вам нужно скопировать что-то в буфер обмена на вашем телевизоре. Большинство файловых менеджеров разработаны для сенсорных экранов и плохо работают с пультами ДУ.

### Решение

Это приложение предоставляет простой интерфейс, оптимизированный для TV, с поддержкой навигации с помощью D-pad. Включает две основные функции: загрузка файлов по URL, открытие и копирование содержимого файлов в буфер обмена.

# ✨ Особенности

- **Загрузка файлов по URL** - прямая загрузка в память устройства
- **Выбор файлов** - просмотр и выбор файлов с устройства
- **Поддержка буфера обмена** - копирование содержимого файлов в буфер обмена
- **Просмотр файлов** - открытие файлов системными приложениями
- **UI для TV** - дизайн оптимизирован для навигации D-pad
- **Две локализации** - английский и русский языки
- **Управление разрешениями** - современный API разрешений Android
- **Coroutines** - асинхронные операции без блокировки UI

# 🚀 Использование

### Установка

#### Из исходников:
```bash
git clone https://github.com/mopsicus/downloader-tv.git
cd downloader-tv
./gradlew assembleDebug
```

#### Установка APK:
```bash
./gradlew installDebug
# или
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Быстрый старт

1. **Откройте проект** в Android Studio
2. **Синхронизируйте Gradle** (дождитесь загрузки зависимостей)
3. **Создайте эмулятор** (телефон или Android TV)
4. **Запустите** кнопкой ▶️ или `Shift + F10`

Подробные инструкции см. в [RUNNING.md](./RUNNING.md).

### Тестирование на эмуляторе

#### Функция загрузки:
1. Перейдите в раздел "Загрузка"
2. Введите URL файла (например, `https://example.com/file.pdf`)
3. Нажмите кнопку "Загрузить"
4. Файл сохранится в папку Downloads

#### Функция копирования:
1. Перейдите в раздел "Копирование"
2. Нажмите кнопку "Выбрать файл"
3. Выберите файл из списка (требуется файловый менеджер)
4. Используйте "Открыть" для просмотра или "Копировать" в буфер

**Примечание**: Эмуляторы без Google Play требуют установки файлового менеджера:
```bash
adb install /path/to/file-manager.apk
```

Подробнее читайте в [документации](./RUNNING.md).

# 🧩 Структура проекта

```
downloader-tv/
├── app/
│   ├── build.gradle.kts           # Конфигурация модуля приложения
│   └── src/main/
│       ├── AndroidManifest.xml    # Манифест с настройками TV
│       ├── java/com/mopsicus/downloadertv/
│       │   ├── MainActivity.kt            # Главная активность с навигацией
│       │   ├── DownloadFragment.kt        # Функция загрузки
│       │   └── CopyFragment.kt            # Функция менеджера файлов
│       └── res/
│           ├── layout/                    # XML layouts
│           ├── values/                    # Строки, цвета (английский)
│           └── values-ru/                 # Русская локализация
├── build.gradle.kts               # Корневой build файл
├── settings.gradle.kts            # Настройки Gradle
├── gradle/libs.versions.toml      # Версии зависимостей
├── README.md                      # Этот файл
└── RUNNING.md                     # Подробное руководство
```

> [!NOTE]
> Весь код написан с помощью Claude Sonnet 4.5

# 🌍 Локализация

Приложение поддерживает автоматическое определение языка и включает:
- 🇬🇧 **Английский** (по умолчанию) - `res/values/`
- 🇷🇺 **Русский** - `res/values-ru/`

Чтобы добавить новый язык:
1. Создайте `res/values-{lang}/strings.xml`
2. Скопируйте строки из `values/strings.xml`
3. Переведите значения строк
4. Пересоберите проект

# 🏗️ Развитие

Мы приглашаем вас внести свой вклад и помочь улучшить Downloader TV+. Пожалуйста, ознакомьтесь с [документом](./CONTRIBUTING.md). 🤗

Вы также можете внести свой вклад в проект Downloader TV+:

- Помогая другим пользователям
- Мониторя список существующих проблем
- Рассказав о проекте в своих соцсетях
- Используя его в своих проектах

# 🤝 Поддержка

Вы можете поддержать проект любым из следующих способов:

* Bitcoin (BTC): 1VccPXdHeiUofzEj4hPfvVbdnzoKkX8TJ
* USDT (TRC20): TMHacMp461jHH2SHJQn8VkzCPNEMrFno7m
* TON: UQDVp346KxR6XxFeYc3ksZ_jOuYjztg7b4lEs6ulEWYmJb0f
* Visa, Mastercard через [Boosty](https://boosty.to/mopsicus/donate)
* МИР через [CloudTips](https://pay.cloudtips.ru/p/9f507669)

# ✉️ Контактная информация

Перед тем как задать вопрос, лучшим решением будет посмотреть уже существующие [проблемы](https://github.com/mopsicus/downloader-tv/issues), это может помочь. В любом случае, вы можете задать любой вопрос или отправить предложение по [email](mailto:mail@mopsicus.ru) или [Telegram](https://t.me/mopsicus).

# 🔑 Лицензия

Downloader TV+ выпущен под лицензией [MIT](./LICENSE). Используйте бесплатно и радуйтесь. 🎉
