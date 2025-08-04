# Gestión de Archivos del Proyecto

## 📁 Estructura de Directorios

### **Directorios Esenciales**:
- `src/` - Código fuente (MANTENER)
- `README.md` - Documentación (MANTENER)
- `*.md` - Archivos de documentación (MANTENER)

### **Directorios Generados**:
- `bin/` - Archivos compilados (.class)
  - Se puede eliminar y regenerar
  - Los archivos .class son necesarios para ejecución
  - Se regeneran automáticamente al compilar

## 🧹 Limpieza del Proyecto

### **Archivos Seguros de Eliminar**:
```
bin/                    # Todo el directorio
├── gui/*.class         # Clases compiladas de interfaz
├── modelo/*.class      # Clases compiladas de modelo
├── servicio/*.class    # Clases compiladas de servicios
└── principal/*.class   # Clases compiladas principales
```

### **¿Por qué existen tantos archivos .class?**

1. **Clases Principales**: Una por cada archivo .java
   - `VentanaCliente.class`
   - `VentanaProveedor.class`
   - `PedidoServicio.class`
   - etc.

2. **Clases Internas**: Generadas por componentes GUI
   - `VentanaCliente$1.class` (ActionListener anónimo)
   - `VentanaCliente$2.class` (Otro listener)
   - `ButtonRenderer.class` (Renderer personalizado)
   - etc.

3. **Clases Anidadas**: Para funcionalidad específica
   - `ArticuloButtonEditor.class`
   - `ClienteButtonRenderer.class`
   - etc.

## ⚙️ Regeneración Automática

### **Opción 1: Script de Limpieza**
```batch
# Ejecutar limpiar_y_compilar.bat
# Elimina bin/ completamente y recompila
```

### **Opción 2: Comando Manual**
```bash
# Eliminar directorio bin
rmdir /s /q bin

# Recrear y compilar
mkdir bin
javac -d bin -cp src src/modelo/*.java src/servicio/*.java src/gui/*.java src/principal/*.java
```

### **Opción 3: Configuración IDE**
- En VS Code/Eclipse/IntelliJ
- Configurar "Clean Build" 
- Elimina y regenera automáticamente

## 🎯 Recomendaciones

### **Para Desarrollo**:
1. ✅ **Mantener**: Solo directorio `src/` y documentación
2. ✅ **Ignorar**: Directorio `bin/` en control de versiones
3. ✅ **Regenerar**: Archivos .class cuando sea necesario

### **Para Distribución**:
1. ✅ **Incluir**: Archivos .class compilados en `bin/`
2. ✅ **Incluir**: Documentación y README
3. ✅ **Opcional**: Código fuente en `src/`

### **Archivo .gitignore Sugerido**:
```
# Archivos compilados
bin/
*.class

# Archivos del IDE
.vscode/
.idea/
*.iml

# Archivos temporales
*.tmp
*.log
```

## 🔧 Solución a Problemas

### **Si hay demasiados archivos .class**:
1. Son normales y necesarios
2. Cada clase Java genera un .class
3. Las clases internas (listeners, etc.) generan archivos adicionales
4. No afectan el rendimiento

### **Si ocupan mucho espacio**:
1. Eliminar directorio `bin/` cuando no uses la aplicación
2. Recompilar solo cuando vayas a ejecutar
3. Los archivos .class son pequeños individualmente

### **Si hay errores de compilación**:
1. Ejecutar `limpiar_y_compilar.bat`
2. Verificar que no falten dependencias
3. Comprobar sintaxis en archivos .java

## ✅ Conclusión

Los archivos en `bin/gui/` y subdirectorios:
- ✅ **Son necesarios** para ejecutar la aplicación
- ✅ **Se pueden eliminar** y regenerar sin problemas  
- ✅ **No se deben editar** manualmente
- ✅ **Son resultado normal** de la compilación Java

**Recomendación**: Mantén solo `src/` en control de versiones, regenera `bin/` cuando necesites ejecutar.
