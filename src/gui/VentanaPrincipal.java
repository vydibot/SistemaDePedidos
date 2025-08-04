package gui;

import modelo.*;
import servicio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana principal del sistema con selección de tipo de usuario
 */
public class VentanaPrincipal extends JFrame {
    private ClienteServicio clienteServicio;
    private ArticuloServicio articuloServicio;
    private PedidoServicio pedidoServicio;
    
    public VentanaPrincipal(ClienteServicio clienteServicio, ArticuloServicio articuloServicio, PedidoServicio pedidoServicio) {
        this.clienteServicio = clienteServicio;
        this.articuloServicio = articuloServicio;
        this.pedidoServicio = pedidoServicio;
        
        initializeComponents();
        setupWindow();
    }
    
    private void initializeComponents() {
        setTitle("Sistema de Gestión de Pedidos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Centro con opciones de usuario
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("SISTEMA DE GESTIÓN DE PEDIDOS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitleLabel = new JLabel("Fundamentos de Ingeniería de Software - TALLER 1");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitleLabel.setForeground(new Color(220, 220, 220));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setBackground(new Color(70, 130, 180));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Título de selección
        JLabel selectLabel = new JLabel("Seleccione el tipo de usuario:");
        selectLabel.setFont(new Font("Arial", Font.BOLD, 18));
        selectLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(selectLabel, gbc);
        
        // Botones de usuario
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Botón Cliente
        JButton clienteBtn = createUserButton("👤 CLIENTE", "Acceder como cliente para realizar pedidos", new Color(27, 94, 32));
        clienteBtn.addActionListener(e -> abrirVentanaCliente());
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(clienteBtn, gbc);
        
        // Botón Director de Ventas
        JButton directorBtn = createUserButton("📊 DIRECTOR DE VENTAS", "Acceder al panel administrativo", new Color(230, 81, 0));
        directorBtn.addActionListener(e -> abrirVentanaDirector());
        gbc.gridx = 1;
        gbc.gridy = 1;
        centerPanel.add(directorBtn, gbc);
        
        // Botón Proveedor
        JButton proveedorBtn = createUserButton("🏭 PROVEEDOR", "Gestionar inventario y producción", new Color(74, 20, 140));
        proveedorBtn.addActionListener(e -> abrirVentanaProveedor());
        gbc.gridx = 0;
        gbc.gridy = 2;
        centerPanel.add(proveedorBtn, gbc);
        
        // Botón Estadísticas
        JButton estadisticasBtn = createUserButton("📈 ESTADÍSTICAS", "Ver estadísticas del sistema", new Color(183, 28, 28));
        estadisticasBtn.addActionListener(e -> mostrarEstadisticas());
        gbc.gridx = 1;
        gbc.gridy = 2;
        centerPanel.add(estadisticasBtn, gbc);
        
        return centerPanel;
    }
    
    private JButton createUserButton(String text, String tooltip, Color color) {
        JButton button = new JButton("<html><center><b>" + text + "</b></center></html>");
        button.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Configuración de colores más contrastantes
        button.setBackground(Color.WHITE);
        button.setForeground(color);
        
        // Borde grueso y colorido
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 3),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        button.setPreferredSize(new Dimension(300, 80));
        button.setToolTipText(tooltip);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        
        // Efectos hover mejorados
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Al pasar el mouse: fondo del color, texto blanco
                button.setBackground(color);
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color.darker(), 3),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Al salir: fondo blanco, texto del color
                button.setBackground(Color.WHITE);
                button.setForeground(color);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color, 3),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });
        
        return button;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout());
        footerPanel.setBackground(new Color(240, 248, 255));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton cargarDatosBtn = new JButton("🔄 Cargar Datos de Ejemplo");
        cargarDatosBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        cargarDatosBtn.addActionListener(e -> cargarDatosDeEjemplo());
        
        JButton salirBtn = new JButton("🚪 Salir");
        salirBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        salirBtn.addActionListener(e -> System.exit(0));
        
        footerPanel.add(cargarDatosBtn);
        footerPanel.add(Box.createHorizontalStrut(20));
        footerPanel.add(salirBtn);
        
        return footerPanel;
    }
    
    private void abrirVentanaCliente() {
        String codigoCliente = JOptionPane.showInputDialog(this, 
            "Ingrese su código de cliente:", 
            "Acceso Cliente", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (codigoCliente != null && !codigoCliente.trim().isEmpty()) {
            Cliente cliente = clienteServicio.buscarCliente(codigoCliente.trim());
            
            if (cliente != null) {
                VentanaCliente ventanaCliente = new VentanaCliente(cliente, pedidoServicio, articuloServicio, this);
                ventanaCliente.setVisible(true);
                this.setVisible(false);
            } else {
                int opcion = JOptionPane.showConfirmDialog(this,
                    "Cliente no encontrado.\n¿Desea ver los clientes disponibles?",
                    "Cliente no encontrado",
                    JOptionPane.YES_NO_OPTION);
                
                if (opcion == JOptionPane.YES_OPTION) {
                    mostrarClientesDisponibles();
                }
            }
        }
    }
    
    private void abrirVentanaDirector() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Usuario:"));
        JTextField usuarioField = new JTextField("admin");
        panel.add(usuarioField);
        panel.add(new JLabel("Contraseña:"));
        JPasswordField passwordField = new JPasswordField("admin123");
        panel.add(passwordField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Acceso Director de Ventas", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String usuario = usuarioField.getText();
            String password = new String(passwordField.getPassword());
            
            if ("admin".equals(usuario) && "admin123".equals(password)) {
                VentanaDirectorVentas ventanaDirector = new VentanaDirectorVentas(
                    pedidoServicio, clienteServicio, articuloServicio, this);
                ventanaDirector.setVisible(true);
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Credenciales incorrectas.\nUsuario: admin\nContraseña: admin123",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void abrirVentanaProveedor() {
        String codigoProveedor = JOptionPane.showInputDialog(this,
            "Ingrese código de proveedor (debe iniciar con 'PROV'):",
            "Acceso Proveedor",
            JOptionPane.QUESTION_MESSAGE);
        
        if (codigoProveedor != null && !codigoProveedor.trim().isEmpty()) {
            if (codigoProveedor.trim().startsWith("PROV")) {
                VentanaProveedor ventanaProveedor = new VentanaProveedor(articuloServicio, pedidoServicio, this);
                ventanaProveedor.setVisible(true);
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Código de proveedor inválido.\nDebe iniciar con 'PROV' (ej: PROV001)",
                    "Error de acceso",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void mostrarEstadisticas() {
        StringBuilder stats = new StringBuilder();
        stats.append("📊 ESTADÍSTICAS GENERALES DEL SISTEMA\n\n");
        
        // Clientes
        stats.append("👥 CLIENTES:\n");
        stats.append("Total registrados: ").append(clienteServicio.getCantidadClientes()).append("\n");
        stats.append("Con saldo vencido: ").append(clienteServicio.getClientesConSaldoVencido().size()).append("\n");
        stats.append("Con descuento: ").append(clienteServicio.getClientesConDescuento().size()).append("\n");
        stats.append("Total saldos: $").append(String.format("%.2f", clienteServicio.getTotalSaldosClientes())).append("\n\n");
        
        // Inventario
        stats.append("📦 INVENTARIO:\n");
        stats.append("Total artículos: ").append(articuloServicio.getCantidadArticulos()).append("\n");
        stats.append("Disponibles: ").append(articuloServicio.getArticulosDisponibles().size()).append("\n");
        stats.append("Con bajo stock: ").append(articuloServicio.getArticulosConBajoStock().size()).append("\n");
        stats.append("Plantas activas: ").append(articuloServicio.getResumenStockPorPlanta().size()).append("\n\n");
        
        // Pedidos
        stats.append("🛒 PEDIDOS:\n");
        stats.append("Total: ").append(pedidoServicio.getCantidadPedidos()).append("\n");
        stats.append("Pendientes: ").append(pedidoServicio.getPedidosPendientes().size()).append("\n");
        stats.append("Incompletos: ").append(pedidoServicio.getPedidosIncompletos().size()).append("\n");
        
        JTextArea textArea = new JTextArea(stats.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Estadísticas del Sistema", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarClientesDisponibles() {
        var clientes = clienteServicio.listarClientes();
        
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay clientes registrados en el sistema.",
                "Sin clientes",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder clientesInfo = new StringBuilder();
        clientesInfo.append("CLIENTES DISPONIBLES:\n\n");
        clientesInfo.append(String.format("%-10s %-25s %-15s%n", "Código", "Nombre", "Límite Crédito"));
        clientesInfo.append("-".repeat(55)).append("\n");
        
        for (Cliente cliente : clientes) {
            clientesInfo.append(String.format("%-10s %-25s $%-14.2f%n",
                cliente.getCodigo(),
                cliente.getNombre(),
                cliente.getLimiteCredito()));
        }
        
        JTextArea textArea = new JTextArea(clientesInfo.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Clientes Disponibles", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cargarDatosDeEjemplo() {
        // Crear clientes de ejemplo
        crearClientesDeEjemplo();
        
        // Crear artículos de ejemplo
        crearArticulosDeEjemplo();
        
        // Crear algunos pedidos de ejemplo
        crearPedidosDeEjemplo();
        
        JOptionPane.showMessageDialog(this,
            "✅ Datos de ejemplo cargados exitosamente!\n\n" +
            "Códigos de cliente disponibles:\n" +
            "• CLI001 (Juan Pérez)\n" +
            "• CLI002 (María García)\n" +
            "• CLI003 (Carlos López)\n" +
            "• CLI004 (Ana Rodríguez)\n" +
            "• CLI005 (Luis Martínez)\n\n" +
            "Director: admin/admin123\n" +
            "Proveedor: PROV001 (o cualquier código con PROV)",
            "Datos Cargados",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void crearClientesDeEjemplo() {
        // Cliente 1
        Cliente cliente1 = new Cliente("CLI001", "Juan Pérez", 50000.0, 5.0);
        cliente1.agregarDireccionEnvio("Calle 123 #45-67, Bogotá");
        cliente1.agregarDireccionEnvio("Carrera 89 #12-34, Bogotá");
        clienteServicio.agregarCliente(cliente1);
        
        // Cliente 2
        Cliente cliente2 = new Cliente("CLI002", "María García", 75000.0, 10.0);
        cliente2.agregarDireccionEnvio("Avenida 68 #23-45, Medellín");
        cliente2.setSaldo(15000.0);
        clienteServicio.agregarCliente(cliente2);
        
        // Cliente 3
        Cliente cliente3 = new Cliente("CLI003", "Carlos López", 30000.0, 3.0);
        cliente3.agregarDireccionEnvio("Calle 56 #78-90, Cali");
        cliente3.agregarDireccionEnvio("Carrera 12 #34-56, Cali");
        clienteServicio.agregarCliente(cliente3);
        
        // Cliente 4 - Con saldo vencido para pruebas
        Cliente cliente4 = new Cliente("CLI004", "Ana Rodríguez", 25000.0, 2.0);
        cliente4.agregarDireccionEnvio("Diagonal 45 #67-89, Barranquilla");
        cliente4.setSaldo(30000.0); // Excede el límite de crédito
        clienteServicio.agregarCliente(cliente4);
        
        // Cliente 5
        Cliente cliente5 = new Cliente("CLI005", "Luis Martínez", 100000.0, 15.0);
        cliente5.agregarDireccionEnvio("Transversal 23 #45-67, Bucaramanga");
        clienteServicio.agregarCliente(cliente5);
    }
    
    private void crearArticulosDeEjemplo() {
        // Artículo 1 - Laptop
        Articulo laptop = new Articulo("ART001", "Laptop HP", "Laptop HP Pavilion 15.6 pulgadas", 2500000.0);
        laptop.agregarPlanta("Planta Bogotá", 50, 10);
        laptop.agregarPlanta("Planta Medellín", 30, 5);
        laptop.agregarPlanta("Planta Cali", 20, 5);
        articuloServicio.agregarArticulo(laptop);
        
        // Artículo 2 - Mouse
        Articulo mouse = new Articulo("ART002", "Mouse Inalámbrico", "Mouse inalámbrico óptico", 85000.0);
        mouse.agregarPlanta("Planta Bogotá", 200, 20);
        mouse.agregarPlanta("Planta Medellín", 150, 15);
        articuloServicio.agregarArticulo(mouse);
        
        // Artículo 3 - Teclado
        Articulo teclado = new Articulo("ART003", "Teclado Mecánico", "Teclado mecánico gaming RGB", 350000.0);
        teclado.agregarPlanta("Planta Bogotá", 80, 10);
        teclado.agregarPlanta("Planta Cali", 60, 8);
        articuloServicio.agregarArticulo(teclado);
        
        // Artículo 4 - Monitor
        Articulo monitor = new Articulo("ART004", "Monitor 24 pulgadas", "Monitor LED Full HD 24 pulgadas", 650000.0);
        monitor.agregarPlanta("Planta Bogotá", 40, 8);
        monitor.agregarPlanta("Planta Medellín", 35, 6);
        monitor.agregarPlanta("Planta Cali", 25, 5);
        articuloServicio.agregarArticulo(monitor);
        
        // Artículo 5 - Impresora
        Articulo impresora = new Articulo("ART005", "Impresora Multifuncional", "Impresora HP multifuncional", 450000.0);
        impresora.agregarPlanta("Planta Bogotá", 25, 5);
        impresora.agregarPlanta("Planta Medellín", 20, 4);
        articuloServicio.agregarArticulo(impresora);
        
        // Artículo 6 - Disco Duro
        Articulo disco = new Articulo("ART006", "Disco Duro 1TB", "Disco duro externo portátil 1TB", 280000.0);
        disco.agregarPlanta("Planta Bogotá", 100, 15);
        disco.agregarPlanta("Planta Medellín", 80, 12);
        disco.agregarPlanta("Planta Cali", 60, 10);
        articuloServicio.agregarArticulo(disco);
        
        // Artículo 7 - Webcam (con bajo stock para pruebas)
        Articulo webcam = new Articulo("ART007", "Cámara Web HD", "Cámara web HD 1080p", 180000.0);
        webcam.agregarPlanta("Planta Bogotá", 8, 10); // Bajo stock
        webcam.agregarPlanta("Planta Medellín", 3, 8); // Bajo stock
        articuloServicio.agregarArticulo(webcam);
        
        // Artículo 8 - Audífonos
        Articulo audifonos = new Articulo("ART008", "Audífonos Bluetooth", "Audífonos inalámbricos con cancelación de ruido", 420000.0);
        audifonos.agregarPlanta("Planta Bogotá", 60, 12);
        audifonos.agregarPlanta("Planta Cali", 45, 10);
        articuloServicio.agregarArticulo(audifonos);
    }
    
    private void crearPedidosDeEjemplo() {
        // Pedido 1 - Juan Pérez
        String pedido1 = pedidoServicio.crearPedido("CLI001", "Calle 123 #45-67, Bogotá");
        if (pedido1 != null) {
            pedidoServicio.agregarDetallePedido(pedido1, "ART001", 1); // Laptop
            pedidoServicio.agregarDetallePedido(pedido1, "ART002", 2); // Mouse
            pedidoServicio.agregarDetallePedido(pedido1, "ART003", 1); // Teclado
            pedidoServicio.procesarPedido(pedido1);
        }
        
        // Pedido 2 - María García
        String pedido2 = pedidoServicio.crearPedido("CLI002", "Avenida 68 #23-45, Medellín");
        if (pedido2 != null) {
            pedidoServicio.agregarDetallePedido(pedido2, "ART004", 2); // Monitor
            pedidoServicio.agregarDetallePedido(pedido2, "ART005", 1); // Impresora
            pedidoServicio.procesarPedido(pedido2);
        }
        
        // Pedido 3 - Carlos López (pendiente)
        String pedido3 = pedidoServicio.crearPedido("CLI003", "Calle 56 #78-90, Cali");
        if (pedido3 != null) {
            pedidoServicio.agregarDetallePedido(pedido3, "ART006", 3); // Disco Duro
            pedidoServicio.agregarDetallePedido(pedido3, "ART008", 2); // Audífonos
            // Este pedido se deja pendiente para pruebas
        }
        
        // Pedido 4 - Luis Martínez
        String pedido4 = pedidoServicio.crearPedido("CLI005", "Transversal 23 #45-67, Bucaramanga");
        if (pedido4 != null) {
            pedidoServicio.agregarDetallePedido(pedido4, "ART001", 2); // Laptop
            pedidoServicio.agregarDetallePedido(pedido4, "ART004", 2); // Monitor
            pedidoServicio.agregarDetallePedido(pedido4, "ART007", 4); // Webcam
            pedidoServicio.procesarPedido(pedido4);
        }
        
        // Pedido 5 - Juan Pérez (segundo pedido)
        String pedido5 = pedidoServicio.crearPedido("CLI001", "Carrera 89 #12-34, Bogotá");
        if (pedido5 != null) {
            pedidoServicio.agregarDetallePedido(pedido5, "ART008", 1); // Audífonos
            pedidoServicio.agregarDetallePedido(pedido5, "ART006", 2); // Disco Duro
            // Este pedido también se deja pendiente
        }
    }
    
    private void setupWindow() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Usar look and feel por defecto si falla
        }
    }
}
