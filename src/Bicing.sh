#!/bin/bash

# Directorios
SRC_DIR="."
BIN_DIR="../bin"
LIB_DIR="../lib"
MAIN_CLASS="Main"

# Comprobar argumento
if [ $# -ne 1 ]; then
  echo "Uso: ./Bicing [compile|run]"
  exit 1
fi

# Compilar
if [ "$1" == "compile" ]; then
  echo "Compilando..."
  mkdir -p "$BIN_DIR"
  javac -d "$BIN_DIR" -cp ".:$LIB_DIR/AIMA.jar:$LIB_DIR/Bicing.jar" "$SRC_DIR/$MAIN_CLASS.java"
  echo "Compilación completada."
  exit 0
fi

# Ejecutar
if [ "$1" == "run" ]; then
  if [ -f "$BIN_DIR/$MAIN_CLASS.class" ]; then
    echo "Ejecutando..."
    java -cp "$BIN_DIR:.:$LIB_DIR/AIMA.jar:$LIB_DIR/Bicing.jar" "$MAIN_CLASS"
    exit 0
  else
    echo "El archivo $MAIN_CLASS.class no se ha encontrado en el directorio $BIN_DIR. Debes compilar primero."
    exit 1
  fi
fi

# Argumento no válido
echo "Uso: ./Bicing [compile|run]"
exit 1

