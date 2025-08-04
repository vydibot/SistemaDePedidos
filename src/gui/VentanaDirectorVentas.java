package gui;

import modelo.*;
import servicio.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana principal para el Director de Ventas
 */
public class VentanaDirectorVentas extends JFrame {
    private PedidoServicio pedidoServicio;
    private ClienteServicio clienteServicio;
    private ArticuloServicio articuloServicio;
    private VentanaPrincipal ventanaPrincipal;
    private JTabbedPane tabbedPane;
    
    public VentanaDirectorVentas(PedidoServicio pedidoServicio, ClienteServicio clienteServicio, ArticuloServicio articuloServicio, VentanaPrincipal ventanaPrincipal) {
        this.pedidoServicio = pedidoServicio;
        this.clienteServicio = clienteServicio;
        this.articuloServicio = articuloServicio;
        this.ventanaPrincipal = ventanaPrincipal;
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        setTitle("Sistema de Pedidos - Director de Ventas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content with tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("📊 Dashboard", createDashboardPanel());
        tabbedPane.addTab("👥 Clientes", createClientesPanel());
        tabbedPane.addTab("🛒 Pedidos", createPedidosPanel());
        tabbedPane.addTab("📈 Reportes", createReportesPanel());
        tabbedPane.addTab("📦 Inventario", createInventarioPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 140, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("PANEL DE ADMINISTRACIÓN - DIRECTOR DE VENTAS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel infoLabel = new JLabel("Sistema de Gestión de Pedidos - Control Total del Negocio");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(255, 255, 220));
        
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setBackground(new Color(255, 140, 0));
        leftPanel.add(titleLabel);
        leftPanel.add(infoLabel);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Estadísticas generales
        panel.add(createStatsCard("👥 CLIENTES", 
            String.valueOf(clienteServicio.getCantidadClientes()),
            "Total registrados",
            new Color(52, 152, 219)));
        
        panel.add(createStatsCard("🛒 PEDIDOS", 
            String.valueOf(pedidoServicio.getCantidadPedidos()),
            "Total procesados",
            new Color(46, 204, 113)));
        
        panel.add(createStatsCard("📦 ARTÍCULOS", 
            String.valueOf(articuloServicio.getCantidadArticulos()),
            "En catálogo",
            new Color(155, 89, 182)));
        
        panel.add(createStatsCard("⏳ PENDIENTES", 
            String.valueOf(pedidoServicio.getPedidosPendientes().size()),
            "Pedidos por procesar",
            new Color(243, 156, 18)));
        
        panel.add(createStatsCard("⚠️ BAJO STOCK", 
            String.valueOf(articuloServicio.getArticulosConBajoStock().size()),
            "Artículos críticos",
            new Color(231, 76, 60)));
        
        panel.add(createStatsCard("💰 SALDOS", 
            String.format("$%.0f", clienteServicio.getTotalSaldosClientes()),
            "Total en cuentas",
            new Color(26, 188, 156)));
        
        return panel;
    }
    
    private JPanel createStatsCard(String title, String value, String description, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createRaisedBevelBorder());
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(255, 255, 255, 200));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(descLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createClientesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título y botones
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("GESTIÓN DE CLIENTES");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton nuevoClienteBtn = new JButton("➕ Nuevo Cliente");
        JButton actualizarBtn = new JButton("🔄 Actualizar");
        
        // Configurar botones con máximo contraste FORZADO
        configurarBotonConMaximoContraste(nuevoClienteBtn, new Color(34, 139, 34), Color.WHITE);
        configurarBotonConMaximoContraste(actualizarBtn, new Color(70, 130, 180), Color.WHITE);
        
        nuevoClienteBtn.addActionListener(e -> mostrarFormularioCliente(null));
        actualizarBtn.addActionListener(e -> actualizarTablaClientes());
        
        buttonPanel.add(nuevoClienteBtn);
        buttonPanel.add(actualizarBtn);
        
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Tabla de clientes
        String[] columns = {"Código", "Nombre", "Direcciones", "Saldo", "Límite", "Descuento", "Estado", "Acciones"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Solo la columna de acciones
            }
        };
        
        JTable table = new JTable(model);
        configurarTablaConColores(table);
        
        // Configurar editor y renderer para la columna de botones "Acciones"
        table.getColumn("Acciones").setCellRenderer(new ClienteButtonRenderer());
        table.getColumn("Acciones").setCellEditor(new ClienteButtonEditor(new JCheckBox(), table, this));
        
        // Llenar tabla
        actualizarTablaClientes(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void actualizarTablaClientes() {
        // Método para actualizar desde el botón
        Component comp = tabbedPane.getComponentAt(1); // Pestaña de clientes
        if (comp instanceof JPanel) {
            // Recrear el panel de clientes para actualizarlo
            tabbedPane.setComponentAt(1, createClientesPanel());
        }
    }
    
    private void actualizarTablaClientes(DefaultTableModel model) {
        if (model != null) {
            model.setRowCount(0); // Limpiar tabla
            
            List<Cliente> clientes = clienteServicio.listarClientes();
            for (Cliente cliente : clientes) {
                String estado = cliente.getSaldo() > cliente.getLimiteCredito() ? "VENCIDO" : "ACTIVO";
                String direcciones = cliente.getDireccionesEnvio().isEmpty() 
                    ? "Sin direcciones" 
                    : String.join(", ", cliente.getDireccionesEnvio());
                
                Object[] row = {
                    cliente.getCodigo(),
                    cliente.getNombre(),
                    direcciones,
                    String.format("$%.2f", cliente.getSaldo()),
                    String.format("$%.2f", cliente.getLimiteCredito()),
                    String.format("%.1f%%", cliente.getPorcentajeDescuento()),
                    estado,
                    "Ver/Editar"
                };
                model.addRow(row);
            }
        }
    }
    
    private JPanel createPedidosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titleLabel = new JLabel("GESTIÓN DE PEDIDOS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Tabla de pedidos
        String[] columns = {"Número", "Cliente", "Fecha", "Estado", "Monto", "Completo", "Acciones"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Solo la columna de acciones
            }
        };
        
        JTable table = new JTable(model);
        configurarTablaConColores(table);
        
        // Configurar editor y renderer para la columna de botones "Acciones"
        table.getColumn("Acciones").setCellRenderer(new PedidoDirectorButtonRenderer());
        table.getColumn("Acciones").setCellEditor(new PedidoDirectorButtonEditor(new JCheckBox(), table, this));
        
        // Llenar tabla con pedidos
        List<Pedido> pedidos = pedidoServicio.listarPedidos();
        for (Pedido pedido : pedidos) {
            Object[] row = {
                pedido.getNumeroPedido(),
                pedido.getCliente().getNombre(),
                pedido.getFechaPedido().toString(),
                pedido.getEstado().toString(),
                String.format("$%.2f", pedido.getMontoTotal()),
                pedido.estaCompleto() ? "Sí" : "No",
                "Gestionar"
            };
            model.addRow(row);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createReportesPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Reporte de ventas
        JPanel ventasPanel = new JPanel(new BorderLayout());
        ventasPanel.setBorder(BorderFactory.createTitledBorder("📊 Reportes de Ventas"));
        
        JTextArea ventasArea = new JTextArea();
        ventasArea.setEditable(false);
        ventasArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder ventasInfo = new StringBuilder();
        ventasInfo.append("RESUMEN DE VENTAS\n\n");
        
        var estadisticas = pedidoServicio.getEstadisticasPorEstado();
        for (var entry : estadisticas.entrySet()) {
            ventasInfo.append(String.format("%-15s: %d pedidos%n", entry.getKey(), entry.getValue()));
        }
        
        ventasArea.setText(ventasInfo.toString());
        ventasPanel.add(new JScrollPane(ventasArea), BorderLayout.CENTER);
        
        // Top artículos
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("🏆 Top Artículos"));
        
        JTextArea topArea = new JTextArea();
        topArea.setEditable(false);
        topArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder topInfo = new StringBuilder();
        topInfo.append("ARTÍCULOS MÁS PEDIDOS\n\n");
        
        List<Articulo> topArticulos = pedidoServicio.getArticulosMasPedidos(5);
        for (int i = 0; i < topArticulos.size(); i++) {
            Articulo articulo = topArticulos.get(i);
            topInfo.append(String.format("%d. %s (%s)%n", 
                i + 1, articulo.getNombre(), articulo.getCodigo()));
        }
        
        topArea.setText(topInfo.toString());
        topPanel.add(new JScrollPane(topArea), BorderLayout.CENTER);
        
        // Alertas
        JPanel alertasPanel = new JPanel(new BorderLayout());
        alertasPanel.setBorder(BorderFactory.createTitledBorder("⚠️ Alertas"));
        
        JTextArea alertasArea = new JTextArea();
        alertasArea.setEditable(false);
        alertasArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder alertasInfo = new StringBuilder();
        alertasInfo.append("ALERTAS DEL SISTEMA\n\n");
        
        List<Articulo> bajoStock = articuloServicio.getArticulosConBajoStock();
        alertasInfo.append("Artículos con bajo stock: ").append(bajoStock.size()).append("\n");
        
        List<Cliente> saldoVencido = clienteServicio.getClientesConSaldoVencido();
        alertasInfo.append("Clientes con saldo vencido: ").append(saldoVencido.size()).append("\n");
        
        alertasInfo.append("Pedidos pendientes: ").append(pedidoServicio.getPedidosPendientes().size()).append("\n");
        
        alertasArea.setText(alertasInfo.toString());
        alertasPanel.add(new JScrollPane(alertasArea), BorderLayout.CENTER);
        
        // Estadísticas generales
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBorder(BorderFactory.createTitledBorder("📈 Estadísticas"));
        
        JTextArea statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder statsInfo = new StringBuilder();
        statsInfo.append("ESTADÍSTICAS GENERALES\n\n");
        statsInfo.append("Total clientes: ").append(clienteServicio.getCantidadClientes()).append("\n");
        statsInfo.append("Total artículos: ").append(articuloServicio.getCantidadArticulos()).append("\n");
        statsInfo.append("Total pedidos: ").append(pedidoServicio.getCantidadPedidos()).append("\n");
        statsInfo.append("Plantas activas: ").append(articuloServicio.getResumenStockPorPlanta().size()).append("\n");
        
        statsArea.setText(statsInfo.toString());
        statsPanel.add(new JScrollPane(statsArea), BorderLayout.CENTER);
        
        panel.add(ventasPanel);
        panel.add(topPanel);
        panel.add(alertasPanel);
        panel.add(statsPanel);
        
        return panel;
    }
    
    private JPanel createInventarioPanel() {
        JPanel inventarioPanel = new JPanel(new BorderLayout());
        inventarioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titleLabel = new JLabel("MONITOREO DE INVENTARIO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        inventarioPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Tabla de artículos
        String[] columns = {"Código", "Nombre", "Precio", "Stock Total", "Estado", "Plantas"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        JTable table = new JTable(model);
        configurarTablaConColores(table);
        
        // Llenar tabla con artículos
        List<Articulo> articulos = articuloServicio.listarArticulos();
        for (Articulo articulo : articulos) {
            String estado = articuloServicio.getArticulosConBajoStock().contains(articulo) ? "BAJO STOCK" : "NORMAL";
            Object[] row = {
                articulo.getCodigo(),
                articulo.getNombre(),
                String.format("$%.2f", articulo.getPrecio()),
                articulo.getTotalStock(),
                estado,
                articulo.getCantidadPorPlanta().size()
            };
            model.addRow(row);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        inventarioPanel.add(scrollPane, BorderLayout.CENTER);
        
        return inventarioPanel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout());
        footerPanel.setBackground(new Color(240, 248, 255));
        
        JButton volverBtn = new JButton("🔙 Volver al Menú Principal");
        JButton actualizarBtn = new JButton("🔄 Actualizar Todo");
        
        // Configurar botones con máximo contraste FORZADO
        configurarBotonConMaximoContraste(volverBtn, new Color(220, 20, 60), Color.WHITE);
        configurarBotonConMaximoContraste(actualizarBtn, new Color(70, 130, 180), Color.WHITE);
        
        volverBtn.addActionListener(e -> {
            this.setVisible(false);
            ventanaPrincipal.setVisible(true);
        });
        
        actualizarBtn.addActionListener(e -> {
            // Actualizar todas las pestañas
            tabbedPane.removeAll();
            tabbedPane.addTab("📊 Dashboard", createDashboardPanel());
            tabbedPane.addTab("👥 Clientes", createClientesPanel());
            tabbedPane.addTab("🛒 Pedidos", createPedidosPanel());
            tabbedPane.addTab("📈 Reportes", createReportesPanel());
            tabbedPane.addTab("📦 Inventario", createInventarioPanel());
        });
        
        footerPanel.add(volverBtn);
        footerPanel.add(actualizarBtn);
        
        return footerPanel;
    }
    
    private void mostrarFormularioCliente(Cliente cliente) {
        JDialog dialog = new JDialog(this, cliente == null ? "Nuevo Cliente" : "Editar Cliente", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Campos del formulario
        JTextField codigoField = new JTextField(20);
        JTextField nombreField = new JTextField(20);
        JTextArea direccionesArea = new JTextArea(3, 20);
        direccionesArea.setLineWrap(true);
        direccionesArea.setWrapStyleWord(true);
        JTextField limiteField = new JTextField(20);
        JTextField descuentoField = new JTextField(20);
        
        if (cliente != null) {
            codigoField.setText(cliente.getCodigo());
            nombreField.setText(cliente.getNombre());
            // Mostrar direcciones de envío separadas por líneas
            if (!cliente.getDireccionesEnvio().isEmpty()) {
                direccionesArea.setText(String.join("\n", cliente.getDireccionesEnvio()));
            }
            limiteField.setText(String.valueOf(cliente.getLimiteCredito()));
            descuentoField.setText(String.valueOf(cliente.getPorcentajeDescuento()));
            codigoField.setEditable(false); // No permitir cambiar código
        }
        
        // Agregar campos al panel
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; panel.add(codigoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; panel.add(nombreField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Direcciones de Envío:"), gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(direccionesArea), gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Límite de Crédito:"), gbc);
        gbc.gridx = 1; panel.add(limiteField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("% Descuento:"), gbc);
        gbc.gridx = 1; panel.add(descuentoField, gbc);
        
        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton guardarBtn = new JButton("Guardar");
        JButton cancelarBtn = new JButton("Cancelar");
        
        // Configurar botones con máximo contraste FORZADO
        configurarBotonConMaximoContraste(guardarBtn, new Color(34, 139, 34), Color.WHITE);
        configurarBotonConMaximoContraste(cancelarBtn, new Color(220, 20, 60), Color.WHITE);
        
        guardarBtn.addActionListener(e -> {
            try {
                String codigo = codigoField.getText().trim();
                String nombre = nombreField.getText().trim();
                String direccionesTexto = direccionesArea.getText().trim();
                double limite = Double.parseDouble(limiteField.getText().trim());
                double descuento = Double.parseDouble(descuentoField.getText().trim());
                
                if (cliente == null) {
                    // Nuevo cliente
                    Cliente nuevoCliente = new Cliente(codigo, nombre, limite, descuento);
                    
                    // Agregar direcciones de envío (separadas por líneas)
                    if (!direccionesTexto.isEmpty()) {
                        String[] direcciones = direccionesTexto.split("\n");
                        for (String direccion : direcciones) {
                            String dir = direccion.trim();
                            if (!dir.isEmpty()) {
                                nuevoCliente.agregarDireccionEnvio(dir);
                            }
                        }
                    }
                    
                    if (clienteServicio.agregarCliente(nuevoCliente)) {
                        JOptionPane.showMessageDialog(dialog, "Cliente creado exitosamente");
                        dialog.dispose();
                        actualizarTablaClientes();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Error: Cliente con ese código ya existe");
                    }
                } else {
                    // Editar cliente existente
                    cliente.setNombre(nombre);
                    cliente.setLimiteCredito(limite);
                    cliente.setPorcentajeDescuento(descuento);
                    
                    // Actualizar direcciones de envío
                    cliente.getDireccionesEnvio().clear(); // Limpiar direcciones existentes
                    if (!direccionesTexto.isEmpty()) {
                        String[] direcciones = direccionesTexto.split("\n");
                        for (String direccion : direcciones) {
                            String dir = direccion.trim();
                            if (!dir.isEmpty()) {
                                cliente.agregarDireccionEnvio(dir);
                            }
                        }
                    }
                    
                    if (clienteServicio.actualizarCliente(cliente)) {
                        JOptionPane.showMessageDialog(dialog, "Cliente actualizado exitosamente");
                        dialog.dispose();
                        actualizarTablaClientes();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Error al actualizar cliente");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: Verifique los valores numéricos");
            }
        });
        
        cancelarBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(guardarBtn);
        buttonPanel.add(cancelarBtn);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void configurarTablaConColores(JTable table) {
        // Configuración básica con fuentes más grandes
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        
        // Colores de encabezado con MÁXIMO contraste - FORZADO
        table.getTableHeader().setBackground(new Color(25, 25, 112));   // Azul marino muy oscuro
        table.getTableHeader().setForeground(Color.WHITE);              // Texto BLANCO
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15)); // Fuente más grande y negrita
        table.getTableHeader().setOpaque(true);                         // Asegurar que el fondo se muestre
        table.getTableHeader().setReorderingAllowed(false);             // Evitar reordenar
        table.getTableHeader().setResizingAllowed(true);                // Permitir redimensionar
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));  // Altura del encabezado
        
        // FORZAR colores del encabezado - Solución definitiva con máximo contraste
        table.getTableHeader().repaint();
        table.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(25, 25, 112));   // Azul marino muy oscuro FORZADO
                setForeground(Color.WHITE);              // Texto blanco FORZADO
                setFont(new Font("Arial", Font.BOLD, 15)); // Fuente más grande
                setHorizontalAlignment(SwingConstants.CENTER);
                setOpaque(true);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
                return this;
            }
        });
        
        // Colores de las filas con MÁXIMO contraste para legibilidad
        table.setBackground(Color.WHITE);                    // Fondo blanco
        table.setForeground(Color.BLACK);                    // Texto negro
        table.setSelectionBackground(new Color(25, 25, 112)); // Azul marino cuando se selecciona
        table.setSelectionForeground(Color.WHITE);           // Texto blanco cuando se selecciona
        table.setGridColor(new Color(100, 100, 100));        // Líneas grises más oscuras
        
        // Configuración adicional para mejor visibilidad
        table.setShowGrid(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setIntercellSpacing(new Dimension(2, 2));      // Espaciado entre celdas más amplio
        
        // Configurar renderizado de celdas para mejor contraste
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (isSelected) {
                    setBackground(new Color(25, 25, 112)); // Azul marino cuando seleccionado
                    setForeground(Color.WHITE);
                } else {
                    // Alternar colores de filas para mejor legibilidad
                    if (row % 2 == 0) {
                        setBackground(Color.WHITE);
                        setForeground(Color.BLACK);
                    } else {
                        setBackground(new Color(248, 248, 255)); // Azul muy claro
                        setForeground(Color.BLACK);
                    }
                }
                
                setFont(new Font("Arial", Font.PLAIN, 13));
                setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
                return this;
            }
        });
    }
    
    /**
     * Método helper para configurar botones con máximo contraste FORZADO
     */
    private void configurarBotonConMaximoContraste(JButton button, Color backgroundColor, Color textColor) {
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        
        // FORZAR colores del botón - Solución definitiva
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton btn = (JButton) c;
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Forzar colores independientemente del Look & Feel
                g2.setColor(backgroundColor);
                g2.fillRect(0, 0, btn.getWidth(), btn.getHeight());
                
                // Dibujar borde
                g2.setColor(backgroundColor.darker());
                g2.drawRect(0, 0, btn.getWidth() - 1, btn.getHeight() - 1);
                
                // Dibujar texto
                g2.setColor(textColor);
                g2.setFont(btn.getFont());
                FontMetrics fm = g2.getFontMetrics();
                String text = btn.getText();
                int x = (btn.getWidth() - fm.stringWidth(text)) / 2;
                int y = (btn.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(text, x, y);
            }
        });
    }
    
    /**
     * Método para editar un cliente específico
     */
    public void editarCliente(String codigoCliente) {
        Cliente cliente = clienteServicio.buscarCliente(codigoCliente);
        if (cliente != null) {
            mostrarFormularioCliente(cliente);
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Método para gestionar un pedido específico
     */
    public void gestionarPedido(String numeroPedido) {
        Pedido pedido = pedidoServicio.buscarPedido(numeroPedido);
        if (pedido == null) {
            JOptionPane.showMessageDialog(this, "Pedido no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        mostrarDialogoGestionPedido(pedido);
    }
    
    /**
     * Método para mostrar el diálogo de gestión de pedidos
     */
    private void mostrarDialogoGestionPedido(Pedido pedido) {
        JDialog dialog = new JDialog(this, "Gestionar Pedido: " + pedido.getNumeroPedido(), true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        
        // Información del pedido
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Número de Pedido:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(pedido.getNumeroPedido()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(pedido.getCliente().getNombre()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(pedido.getFechaPedido().toString()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(pedido.getDireccionEnvio()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        infoPanel.add(new JLabel("Estado Actual:"), gbc);
        gbc.gridx = 1;
        JLabel estadoLabel = new JLabel(pedido.getEstado().toString());
        estadoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        estadoLabel.setForeground(new Color(25, 25, 112));
        infoPanel.add(estadoLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        infoPanel.add(new JLabel("Total:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(String.format("$%.2f", pedido.getMontoTotal())), gbc);
        
        panel.add(infoPanel, BorderLayout.NORTH);
        
        // Tabla de detalles del pedido
        String[] columns = {"Artículo", "Código", "Cantidad", "Precio Unit.", "Subtotal"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        for (DetallePedido detalle : pedido.getDetalles()) {
            Object[] row = {
                detalle.getArticulo().getNombre(),
                detalle.getArticulo().getCodigo(),
                detalle.getCantidadOrdenada(),
                String.format("$%.2f", detalle.getArticulo().getPrecio()),
                String.format("$%.2f", detalle.getSubtotal())
            };
            model.addRow(row);
        }
        
        JTable table = new JTable(model);
        configurarTablaConColores(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de acciones
        JPanel accionesPanel = new JPanel(new FlowLayout());
        
        // Botón para cambiar estado
        JButton cambiarEstadoBtn = new JButton("Cambiar Estado");
        configurarBotonConMaximoContraste(cambiarEstadoBtn, new Color(255, 140, 0), Color.WHITE);
        
        cambiarEstadoBtn.addActionListener(e -> {
            mostrarDialogoCambiarEstado(pedido, dialog);
        });
        
        // Botón para procesar pedido
        JButton procesarBtn = new JButton("Procesar Pedido");
        configurarBotonConMaximoContraste(procesarBtn, new Color(34, 139, 34), Color.WHITE);
        
        procesarBtn.addActionListener(e -> {
            if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
                JOptionPane.showMessageDialog(dialog, 
                    "Solo se pueden procesar pedidos pendientes",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (pedidoServicio.procesarPedido(pedido.getNumeroPedido())) {
                JOptionPane.showMessageDialog(dialog, "Pedido procesado exitosamente");
                estadoLabel.setText(pedido.getEstado().toString());
                // Actualizar la tabla de pedidos
                tabbedPane.setComponentAt(2, createPedidosPanel());
                dialog.dispose();
            } else {
                // Mostrar motivo específico del error
                String motivo = pedido.motivoNoProcesable();
                JOptionPane.showMessageDialog(dialog, 
                    "No se pudo procesar el pedido:\n\n" + motivo,
                    "Error de Procesamiento", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Botón para cancelar pedido
        JButton cancelarPedidoBtn = new JButton("Cancelar Pedido");
        configurarBotonConMaximoContraste(cancelarPedidoBtn, new Color(220, 20, 60), Color.WHITE);
        
        cancelarPedidoBtn.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(dialog,
                "¿Está seguro de cancelar este pedido?",
                "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (pedidoServicio.cancelarPedido(pedido.getNumeroPedido())) {
                    JOptionPane.showMessageDialog(dialog, "Pedido cancelado exitosamente");
                    estadoLabel.setText(pedido.getEstado().toString());
                    // Actualizar la tabla de pedidos
                    tabbedPane.setComponentAt(2, createPedidosPanel());
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "No se pudo cancelar el pedido");
                }
            }
        });
        
        // Botón cerrar
        JButton cerrarBtn = new JButton("Cerrar");
        configurarBotonConMaximoContraste(cerrarBtn, new Color(70, 130, 180), Color.WHITE);
        cerrarBtn.addActionListener(e -> dialog.dispose());
        
        accionesPanel.add(cambiarEstadoBtn);
        accionesPanel.add(procesarBtn);
        accionesPanel.add(cancelarPedidoBtn);
        accionesPanel.add(cerrarBtn);
        
        panel.add(accionesPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Método para mostrar diálogo de cambio de estado
     */
    private void mostrarDialogoCambiarEstado(Pedido pedido, JDialog parentDialog) {
        EstadoPedido[] estados = EstadoPedido.values();
        EstadoPedido estadoSeleccionado = (EstadoPedido) JOptionPane.showInputDialog(
            parentDialog,
            "Estado actual: " + pedido.getEstado() + "\nSeleccione el nuevo estado:",
            "Cambiar Estado del Pedido",
            JOptionPane.QUESTION_MESSAGE,
            null,
            estados,
            pedido.getEstado()
        );
        
        if (estadoSeleccionado != null && estadoSeleccionado != pedido.getEstado()) {
            if (pedidoServicio.cambiarEstadoPedido(pedido.getNumeroPedido(), estadoSeleccionado)) {
                JOptionPane.showMessageDialog(parentDialog, 
                    "Estado cambiado exitosamente a: " + estadoSeleccionado.toString());
                // Actualizar la tabla de pedidos
                tabbedPane.setComponentAt(2, createPedidosPanel());
                parentDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(parentDialog, 
                    "No se puede cambiar al estado seleccionado.\n" +
                    "Verifique las reglas de transición y las condiciones del pedido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

/**
 * Renderer para botones en la tabla de clientes
 */
class ClienteButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
    public ClienteButtonRenderer() {
        setOpaque(true);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Acción" : value.toString());
        setBackground(Color.WHITE);  // Fondo blanco
        setForeground(Color.BLACK);  // Texto negro
        setFont(new Font("Arial", Font.BOLD, 11));
        return this;
    }
}

/**
 * Editor para botones en la tabla de clientes
 */
class ClienteButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private VentanaDirectorVentas ventanaDirector;
    
    public ClienteButtonEditor(JCheckBox checkBox, JTable table, VentanaDirectorVentas ventanaDirector) {
        super(checkBox);
        this.table = table;
        this.ventanaDirector = ventanaDirector;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        label = (value == null) ? "Acción" : value.toString();
        button.setText(label);
        button.setBackground(Color.WHITE);  // Fondo blanco
        button.setForeground(Color.BLACK);  // Texto negro
        button.setFont(new Font("Arial", Font.BOLD, 11));
        isPushed = true;
        return button;
    }
    
    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            // Obtener el código del cliente de la fila seleccionada
            int row = table.getSelectedRow();
            if (row >= 0) {
                String codigoCliente = (String) table.getValueAt(row, 0);
                // Editar el cliente
                ventanaDirector.editarCliente(codigoCliente);
            }
        }
        isPushed = false;
        return label;
    }
    
    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
    
    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}

/**
 * Renderer para botones en la tabla de pedidos
 */
class PedidoDirectorButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
    public PedidoDirectorButtonRenderer() {
        setOpaque(true);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Acción" : value.toString());
        setBackground(Color.WHITE);  // Fondo blanco
        setForeground(Color.BLACK);  // Texto negro
        setFont(new Font("Arial", Font.BOLD, 11));
        return this;
    }
}

/**
 * Editor para botones en la tabla de pedidos del Director de Ventas
 */
class PedidoDirectorButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private VentanaDirectorVentas ventanaDirector;
    
    public PedidoDirectorButtonEditor(JCheckBox checkBox, JTable table, VentanaDirectorVentas ventanaDirector) {
        super(checkBox);
        this.table = table;
        this.ventanaDirector = ventanaDirector;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        label = (value == null) ? "Acción" : value.toString();
        button.setText(label);
        button.setBackground(Color.WHITE);  // Fondo blanco
        button.setForeground(Color.BLACK);  // Texto negro
        button.setFont(new Font("Arial", Font.BOLD, 11));
        isPushed = true;
        return button;
    }
    
    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            // Obtener el número de pedido de la fila seleccionada
            int row = table.getSelectedRow();
            if (row >= 0) {
                String numeroPedido = (String) table.getValueAt(row, 0);
                // Gestionar el pedido
                ventanaDirector.gestionarPedido(numeroPedido);
            }
        }
        isPushed = false;
        return label;
    }
    
    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
    
    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
