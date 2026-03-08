#!/bin/bash
# Скрипт для загрузки тестового APK из внешнего источника

APK_URL="$1"
DEST_PATH="$2"

if [ -z "$APK_URL" ] || [ -z "$DEST_PATH" ]; then
    echo "Usage: $0 <apk_url> <destination_path>"
    exit 1
fi

echo "Downloading APK from $APK_URL to $DEST_PATH"
curl -L -o "$DEST_PATH" "$APK_URL"

if [ $? -eq 0 ]; then
    echo "✅ APK downloaded successfully"
else
    echo "❌ Failed to download APK"
    exit 1
fi