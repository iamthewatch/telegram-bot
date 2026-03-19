# Springbot 🤓

## Что нужно перед запуском

В проекте используется файл `.env` с локальными настройками. Его нужно создать на основе `.env.example`.

## Как создать `.env`

### Windows

PowerShell:

```powershell
Copy-Item .env.example .env
```

Command Prompt (`cmd`):

```cmd
copy .env.example .env
```

### macOS и Linux

```bash
cp .env.example .env
```

## Что заполнить в `.env`

После копирования откройте файл `.env` и укажите реальные значения:

- `AI_API_KEY`
- `AI_MODEL_NAME`
- `TELEGRAM_BOT_TOKEN`

При необходимости также проверьте:

- `AI_BASE_URL`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`

## Запуск через Docker

Убедитесь, что:

- установлен Docker
- создан и заполнен файл `.env`

### Запуск всех сервисов

```bash
docker compose up --build
```

### Запуск в фоне

```bash
docker compose up --build -d
```

## Какие сервисы поднимаются

После запуска будут доступны:

- `postgres` на порту `5433`
- `backend` на порту `8080`

## Как остановить проект

Остановить контейнеры:

```bash
docker compose down
```

Остановить контейнеры и удалить volume базы данных:

```bash
docker compose down -v
```

## Полезно знать

- Бэкенд автоматически подключается к контейнеру `postgres`
- база данных сохраняется в Docker volume `telegram_bot_data`
- если меняли код или зависимости, используйте `docker compose up --build`
