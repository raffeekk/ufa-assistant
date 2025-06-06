# FAQ Search

Проект FAQ Search представляет собой веб-приложение на базе Spring Boot, разработанное для поиска и управления часто задаваемыми вопросами (FAQ).

## Технологии

- Java 17
- Spring Boot 3.2.3
- Spring Web
- Spring Validation
- Jackson для работы с JSON
- Gradle для сборки проекта

## Требования

- JDK 17 или выше
- Gradle 7.0 или выше

## Сборка и запуск

1. Клонируйте репозиторий:
```bash
git clone https://github.com/raffeekk/ufa-assistant.git
```

2. Перейдите в директорию проекта:
```bash
cd faq-search
```

3. Соберите проект с помощью Gradle:
```bash
./gradlew build
```

4. Запустите приложение:
```bash
./gradlew bootRun
```

Приложение будет доступно по адресу: `http://localhost:8080`

## Структура проекта

```
faq-search/
├── src/
│   ├── main/
│   │   ├── java/        # Исходный код Java
│   │   └── resources/   # Конфигурационные файлы
│   └── test/            # Тесты
├── build.gradle         # Конфигурация сборки
└── settings.gradle      # Настройки проекта
```
