# Sistema de Gestión de Pedidos - Versión GUI

## Descripción

Sistema completo de gestión de pedidos desarrollado en Java con **interfaz gráfica Swing**. Este proyecto fue desarrollado para el **TALLER 1 - FUN### Extensibilidad


### Mejoras Implementadas Recientemente
- ✅ **Actualización de Stock Inteligente**: Opciones para establecer, agregar o reducir stock
- ✅ **Validaciones Mejoradas**: Mensajes específicos sobre errores de procesamiento
- ✅ **Dashboard Dinámico**: Actualización automática de estadísticas tras cambios
- ✅ **Control de Estados**: Transiciones de pedidos con reglas de negocio
- ✅ **Gestión de Archivos**: Scripts de limpieza y regeneración automáticaENIERÍA DE SOFTWARE** y ahora incluye una moderna interfaz de usuario.

## 🎯 Funcionalidades Principales

### Gestión Integral
- **Gestión de Clientes**: Registro, consulta y administración de clientes con múltiples direcciones y límites de crédito
- **Catálogo de Artículos**: Manejo de productos con inventario multi-planta y control de stock
- **Procesamiento de Pedidos**: Creación, seguimiento y gestión de estados de pedidos
- **Control de Inventario**: Monitoreo de stock por planta manufacturera con alertas automáticas

### 👥 Tipos de Usuario con Interfaz Dedicada
1. **Cliente**: Navegación de catálogo, creación de pedidos, historial de compras
2. **Director de Ventas**: Dashboard ejecutivo, gestión de clientes, reportes y análisis  
3. **Proveedor**: Control de inventario, gestión de plantas manufactureras, alertas de reabastecimiento

### 📊 Gestión de Entidades
- **Clientes**: Código, nombre, direcciones de envío, saldo, límite de crédito, porcentaje de descuento
- **Artículos**: Código, nombre, descripción, plantas manufactureras, cantidad en existencia, stock mínimo
- **Pedidos**: Cliente, dirección de envío, fecha, detalles de artículos, cantidades ordenadas y pendientes

### 👥 Tipos de Usuario

#### 1. Vista del Cliente
- Consultar catálogo de productos
- Crear nuevos pedidos
- Consultar historial de pedidos
- Verificar estado de pedidos
- Gestionar datos personales y direcciones

#### 2. Vista del Director de Ventas
- Gestión completa de clientes
- Supervisión de pedidos
- Generación de reportes de ventas
- Análisis de estadísticas
- Monitoreo de inventario

#### 3. Vista del Proveedor
- Gestión de artículos e inventario
- Monitoreo de plantas manufactureras
- Actualización de stock
- Reportes de producción
- Alertas de reabastecimiento

## Estructura del Proyecto

```
src/
├── modelo/                 # Clases del modelo de datos
│   ├── Cliente.java
│   ├── Articulo.java
│   ├── Pedido.java
│   ├── DetallePedido.java
│   └── EstadoPedido.java
├── servicio/              # Lógica de negocio
│   ├── ClienteServicio.java
│   ├── ArticuloServicio.java
│   └── PedidoServicio.java
├── gui/                   # Interfaz gráfica de usuario
│   ├── VentanaPrincipal.java
│   ├── VentanaCliente.java
│   ├── VentanaDirectorVentas.java
│   └── VentanaProveedor.java
└── principal/             # Clase principal
    └── SistemaGestionPedidosGUI.java
```

## Requisitos del Sistema

- **Java 8** o superior
- Entorno de desarrollo Java (IDE recomendado: Eclipse, IntelliJ IDEA, VS Code)

## Instalación y Ejecución

### 1. Compilación
```bash
# Navegar al directorio del proyecto
cd "C:\Users\Kris\Documents\Universidad\FIS\Proyecto"

# Compilar el proyecto
javac -d bin src\**\*.java
```

### 2. Ejecución
```bash
# Ejecutar la aplicación GUI
java -cp bin principal.SistemaGestionPedidosGUI
```

### 3. Ejecución desde VS Code
1. Abrir el proyecto en VS Code
2. Ejecutar la clase `SistemaGestionPedidosGUI.java`
3. La aplicación abrirá con interfaz gráfica

## Datos de Prueba

El sistema incluye datos de ejemplo que se cargan automáticamente:

### Clientes de Prueba
- **CLI001**: Juan Pérez (Límite: $50,000, Descuento: 5%)
- **CLI002**: María García (Límite: $75,000, Descuento: 10%)
- **CLI003**: Carlos López (Límite: $30,000, Descuento: 3%)
- **CLI004**: Ana Rodríguez (Saldo vencido para pruebas)
- **CLI005**: Luis Martínez (Límite: $100,000, Descuento: 15%)

### Credenciales de Acceso
- **Director de Ventas**: Usuario: `admin`, Contraseña: `admin123`
- **Proveedor**: Cualquier código que inicie con `PROV` (ej: PROV001)

### Artículos Disponibles
- ART001: Laptop HP ($2,500,000)
- ART002: Mouse Inalámbrico ($85,000)
- ART003: Teclado Mecánico ($350,000)
- ART004: Monitor 24" ($650,000)
- ART005: Impresora Multifuncional ($450,000)
- ART006: Disco Duro 1TB ($280,000)
- ART007: Cámara Web HD ($180,000) - *Bajo stock*
- ART008: Audífonos Bluetooth ($420,000)

## Funcionalidades Principales

### Gestión de Pedidos
- ✅ Creación de pedidos con validación de stock
- ✅ Verificación de límites de crédito
- ✅ Aplicación automática de descuentos
- ✅ Seguimiento de estados (Pendiente, Procesado, Enviado, etc.)
- ✅ Control de cantidades pendientes vs entregadas
- ✅ **NUEVO**: Selección de cantidad personalizada en carrito
- ✅ **NUEVO**: Gestión completa de direcciones de envío
- ✅ **NUEVO**: Botones funcionales para gestión de pedidos
- ✅ **NUEVO**: Actualización automática de listas sin reiniciar

### Gestión de Inventario
- ✅ Control de stock por planta manufacturera
- ✅ Alertas de stock mínimo
- ✅ Transferencias entre plantas
- ✅ Reportes de rotación de inventario
- ✅ **NUEVO**: Gestión avanzada de artículos con botones funcionales
- ✅ **NUEVO**: Actualización de stock en tiempo real
- ✅ **NUEVO**: Agregar nuevas plantas manufactureras

### Gestión de Usuarios
- ✅ **NUEVO**: Gestión completa de direcciones de envío para clientes
- ✅ **NUEVO**: Edición en tiempo real de información de clientes
- ✅ **NUEVO**: Interfaz mejorada con botones totalmente funcionales
- ✅ **NUEVO**: Validaciones mejoradas y mensajes informativos

### Reportes y Análisis
- ✅ Ventas por fecha y cliente
- ✅ Estadísticas por estado de pedido
- ✅ Artículos más pedidos
- ✅ Análisis de eficiencia por planta
- ✅ Proyección de necesidades

## Casos de Uso Implementados

### Vista del Cliente
1. **Consultar Catálogo**: Ver productos disponibles con precios y stock
2. **Realizar Pedido**: Crear pedidos con validaciones automáticas y selección de cantidad
3. **Consultar Pedidos**: Ver historial y estado de pedidos con detalles completos
4. **Gestionar Perfil**: Actualizar direcciones y consultar datos
5. **Gestionar Direcciones**: Agregar, editar y eliminar direcciones de envío
6. **Carrito Inteligente**: Agregar productos al carrito con cantidades personalizadas

### Vista del Director de Ventas
1. **Gestión de Clientes**: CRUD completo de clientes con direcciones múltiples
2. **Supervisión de Pedidos**: Monitoreo y cambio de estados con gestión completa
3. **Reportes de Ventas**: Análisis de ventas por múltiples criterios
4. **Estadísticas**: Dashboard con métricas del negocio en tiempo real
5. **Gestión de Pedidos**: Procesar, cancelar y cambiar estados de pedidos
6. **Control Total**: Acceso completo a todas las funcionalidades del sistema

### Vista del Proveedor
1. **Gestión de Artículos**: CRUD completo de productos con gestión avanzada
2. **Control de Inventario**: Actualización de stock y gestión de plantas
3. **Gestión de Plantas**: Agregar nuevas plantas manufactureras
4. **Actualización de Stock**: Control granular por planta manufacturera
5. **Edición de Artículos**: Modificación completa de información de productos
3. **Alertas**: Notificaciones de stock bajo y demanda pendiente
4. **Reportes de Producción**: Análisis de eficiencia y proyecciones

## Arquitectura del Sistema

### Patrón MVC (Modelo-Vista-Controlador)
- **Modelo**: Clases de entidad (Cliente, Articulo, Pedido, etc.)
- **Vista**: Interfaces de usuario para diferentes tipos de usuarios
- **Controlador**: Servicios que manejan la lógica de negocio

### Principios de Diseño Aplicados
- **Separación de Responsabilidades**: Cada clase tiene una responsabilidad específica
- **Encapsulamiento**: Datos protegidos con getters/setters apropiados
- **Abstracción**: Interfaces claras entre las capas del sistema
- **Polimorfismo**: Uso de enums y herencia donde es apropiado

## Validaciones y Reglas de Negocio

### Cliente
- ✅ Verificación de límite de crédito antes de procesar pedidos
- ✅ Aplicación automática de descuentos por cliente
- ✅ Gestión de múltiples direcciones de envío

### Inventario
- ✅ Validación de disponibilidad antes de confirmar pedidos
- ✅ Alertas automáticas cuando el stock cae bajo el mínimo
- ✅ Control de stock por planta manufacturera

### Pedidos
- ✅ Estados controlados (Pendiente → Procesado → Enviado → Entregado)
- ✅ Tracking de cantidades pendientes vs entregadas
- ✅ Cálculo automático de totales con descuentos

## Extensibilidad

El sistema está diseñado para ser fácilmente extensible:

### Nuevas Funcionalidades
- Sistema de autenticación más robusto
- Integración con base de datos
- API REST para integración con otros sistemas
- Interfaz gráfica (GUI) con JavaFX o Swing
- Reportes en PDF o Excel

### Nuevos Tipos de Usuario
- Operador de Logística
- Contador/Financiero
- Administrador del Sistema

## Diagramas UML

### Diagrama de Clases Principal
```
Cliente ──────── Pedido ──────── DetallePedido ──────── Articulo
   │                │                                      │
   │                │                                      │
   │                └──── EstadoPedido (enum)              │
   │                                                       │
   └── ClienteServicio                                     └── ArticuloServicio
           │                                                       │
           └──────────── PedidoServicio ──────────────────────────┘
```

### Estados del Pedido
```
PENDIENTE → PROCESADO → EN_PREPARACION → ENVIADO → ENTREGADO
    │                                                  
    └─────────────────── CANCELADO ────────────────────
```

## Licencia

Este proyecto es desarrollado con fines académicos como parte del Taller 1 de Fundamentos de Ingeniería de Software.

---

## Notas Adicionales

- El sistema utiliza datos en memoria (no persistencia en base de datos)
- Las validaciones son básicas pero funcionales para propósitos educativos
- El código está ampliamente documentado para facilitar el aprendizaje
- Se incluyen casos de prueba mediante los datos de ejemplo
