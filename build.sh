#!/bin/bash
# ────────────────────────────────────────────────────────────────
#  Student Management System — build & run script
#  Usage:
#    ./build.sh          — compile and run the app
#    ./build.sh test     — compile and run unit tests
# ────────────────────────────────────────────────────────────────

SRC_MAIN="src/main/java"
SRC_TEST="src/test/java"
OUT="out"

echo "==> Compiling sources..."
mkdir -p $OUT

javac -d $OUT \
  $SRC_MAIN/com/sms/exception/*.java \
  $SRC_MAIN/com/sms/model/*.java \
  $SRC_MAIN/com/sms/repository/*.java \
  $SRC_MAIN/com/sms/util/*.java \
  $SRC_MAIN/com/sms/service/*.java \
  $SRC_MAIN/com/sms/ui/*.java \
  $SRC_MAIN/com/sms/Main.java \
  $SRC_TEST/com/sms/StudentServiceTest.java

if [ $? -ne 0 ]; then
  echo "[ERROR] Compilation failed."
  exit 1
fi

echo "==> Compilation successful."

if [ "$1" == "test" ]; then
  echo "==> Running unit tests..."
  java -cp $OUT com.sms.StudentServiceTest
else
  echo "==> Starting application..."
  java -cp $OUT com.sms.Main
fi
