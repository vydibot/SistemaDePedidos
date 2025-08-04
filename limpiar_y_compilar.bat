@echo off
echo Limpiando archivos .class innecesarios...

REM Eliminar todos los archivos .class de bin
if exist bin (
    rmdir /s /q bin
    echo Directorio bin eliminado completamente
)

REM Crear nuevamente el directorio bin
mkdir bin

echo Compilando proyecto...
javac -d bin -cp src src/modelo/*.java src/servicio/*.java src/gui/*.java src/principal/*.java

echo Compilacion completada
echo Los archivos .class se regeneraron correctamente en bin/

pause
