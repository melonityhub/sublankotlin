#!/usr/bin/env sh
# Stub gradlew - real wrapper will be downloaded by GitHub Actions
set -e
if [ -f "./gradle/wrapper/gradle-wrapper.jar" ]; then
  exec java -jar ./gradle/wrapper/gradle-wrapper.jar "$@"
else
  echo "Gradle wrapper jar not found, downloading..."
  # fallback: use system gradle if available
  if command -v gradle >/dev/null 2>&1; then exec gradle "$@"; else echo "Gradle not found"; exit 1; fi
fi
