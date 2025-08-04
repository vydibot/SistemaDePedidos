package principal;

import modelo.*;
import servicio.*;
import gui.VentanaPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal del Sistema de Gestión de Pedidos - Versión GUI
 * Sistema completo para gestión de clientes, artículos y pedidos
 * Funciona con interfaz gráfica de usuario
 */
public class SistemaGestionPedidosGUI {
    public static void main(String[] args) {
        // Configurar Look and Feel del sistema
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Si no se puede cargar el look and feel del sistema, usar el predeterminado
            System.out.println("No se pudo cargar el Look and Feel del sistema, usando predeterminado");
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("==============================================");
                System.out.println("     SISTEMA DE GESTIÓN DE PEDIDOS");
                System.out.println("         Versión GUI - v2.0");
                System.out.println("   Fundamentos de Ingeniería de Software");
                System.out.println("==============================================");
                
                // Inicializar servicios
                ClienteServicio clienteServicio = new ClienteServicio();
                ArticuloServicio articuloServicio = new ArticuloServicio();
                PedidoServicio pedidoServicio = new PedidoServicio(clienteServicio, articuloServicio);
                
                // Cargar datos de ejemplo
                cargarDatosEjemplo(clienteServicio, articuloServicio);
                
                // Crear la ventana principal y mostrarla
                VentanaPrincipal ventana = new VentanaPrincipal(clienteServicio, articuloServicio, pedidoServicio);
                ventana.setVisible(true);
                
                System.out.println("Sistema iniciado exitosamente");
                System.out.println("Interfaz gráfica cargada");
                
            } catch (Exception e) {
                System.err.println("Error al inicializar el sistema: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
    
    private static void cargarDatosEjemplo(ClienteServicio clienteServicio, ArticuloServicio articuloServicio) {
        System.out.println("Cargando datos de ejemplo...");
        
        // Clientes de ejemplo
        Cliente cliente1 = new Cliente("CLI001", "Empresa ABC S.A.S.", 5000000.0, 5.0);
        cliente1.getDireccionesEnvio().add("Calle 123 #45-67, Bogotá");
        cliente1.getDireccionesEnvio().add("Carrera 45 #67-89, Medellín");
        
        Cliente cliente2 = new Cliente("CLI002", "Distribuidora XYZ Ltda.", 3000000.0, 3.0);
        cliente2.getDireccionesEnvio().add("Avenida 80 #12-34, Cali");
        cliente2.getDireccionesEnvio().add("Calle 50 #23-45, Barranquilla");
        
        Cliente cliente3 = new Cliente("CLI003", "Comercial 123 S.A.", 2000000.0, 2.0);
        cliente3.getDireccionesEnvio().add("Carrera 10 #5-15, Bucaramanga");
        
        clienteServicio.agregarCliente(cliente1);
        clienteServicio.agregarCliente(cliente2);
        clienteServicio.agregarCliente(cliente3);
        
        // Artículos de ejemplo
        Articulo articulo1 = new Articulo("ART001", "Laptop Dell Inspiron 15", 
            "Laptop para oficina con procesador Intel i5", 2500000.0);
        articulo1.agregarPlanta("Planta Bogotá", 50, 10);
        articulo1.agregarPlanta("Planta Medellín", 30, 5);
        
        Articulo articulo2 = new Articulo("ART002", "Mouse Inalámbrico Logitech", 
            "Mouse ergonómico con conexión USB", 85000.0);
        articulo2.agregarPlanta("Planta Bogotá", 200, 50);
        articulo2.agregarPlanta("Planta Cali", 150, 30);
        
        Articulo articulo3 = new Articulo("ART003", "Teclado Mecánico Gaming", 
            "Teclado retroiluminado para gaming", 350000.0);
        articulo3.agregarPlanta("Planta Medellín", 75, 15);
        articulo3.agregarPlanta("Planta Bogotá", 45, 10);
        
        Articulo articulo4 = new Articulo("ART004", "Monitor LED 24 pulgadas", 
            "Monitor Full HD para oficina", 650000.0);
        articulo4.agregarPlanta("Planta Bogotá", 25, 5);
        articulo4.agregarPlanta("Planta Cali", 20, 3);
        
        Articulo articulo5 = new Articulo("ART005", "Impresora Multifuncional HP", 
            "Impresora, escáner y copiadora", 450000.0);
        articulo5.agregarPlanta("Planta Medellín", 15, 3);
        articulo5.agregarPlanta("Planta Bogotá", 10, 2);
        
        // Nuevos artículos con stock crítico para pruebas
        Articulo articulo6 = new Articulo("ART006", "Disco Duro 1TB", 
            "Disco duro externo portátil", 280000.0);
        articulo6.agregarPlanta("Planta Bogotá", 8, 10);  // BAJO STOCK (8 < 10)
        articulo6.agregarPlanta("Planta Cali", 15, 20);   // BAJO STOCK (15 < 20)
        
        Articulo articulo7 = new Articulo("ART007", "Cámara Web HD", 
            "Cámara web 1080p para videoconferencias", 180000.0);
        articulo7.agregarPlanta("Planta Medellín", 3, 15);  // BAJO STOCK (3 < 15)
        articulo7.agregarPlanta("Planta Bogotá", 10, 10);   // STOCK OK (10 = 10)
        
        Articulo articulo8 = new Articulo("ART008", "Audífonos Bluetooth", 
            "Audífonos inalámbricos con cancelación de ruido", 420000.0);
        articulo8.agregarPlanta("Planta Cali", 12, 5);     // STOCK OK (12 > 5)
        articulo8.agregarPlanta("Planta Medellín", 25, 5);  // STOCK OK (25 > 5)
        
        articuloServicio.agregarArticulo(articulo1);
        articuloServicio.agregarArticulo(articulo2);
        articuloServicio.agregarArticulo(articulo3);
        articuloServicio.agregarArticulo(articulo4);
        articuloServicio.agregarArticulo(articulo5);
        articuloServicio.agregarArticulo(articulo6);
        articuloServicio.agregarArticulo(articulo7);
        articuloServicio.agregarArticulo(articulo8);
        
        System.out.println("Datos de ejemplo cargados exitosamente");
        System.out.println("- " + clienteServicio.getCantidadClientes() + " clientes");
        System.out.println("- " + articuloServicio.getCantidadArticulos() + " artículos");
        System.out.println();
    }
}
