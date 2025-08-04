package gui;

import modelo.*;
import servicio.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Ventana principal para el Proveedor
 */
public class VentanaProveedor extends JFrame {
    private ArticuloServicio articuloServicio;
    private PedidoServicio pedidoServicio;
    private VentanaPrincipal ventanaPrincipal;
    private JTabbedPane tabbedPane;
    
    public VentanaProveedor(ArticuloServicio articuloServicio, PedidoServicio pedidoServicio, VentanaPrincipal ventanaPrincipal) {
        this.articuloServicio = articuloServicio;
        this.pedidoServicio = pedidoServicio;
        this.ventanaPrincipal = ventanaPrincipal;
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        setTitle("Sistema de Pedidos - Proveedor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content with tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("🏭 Dashboard", createDashboardPanel());
        tabbedPane.addTab("📦 Artículos", createArticulosPanel());
        tabbedPane.addTab("📊 Inventario", createInventarioPanel());
        tabbedPane.addTab("🏢 Plantas", createPlantasPanel());
        tabbedPane.addTab("⚠️ Alertas", createAlertasPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(138, 43, 226));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("PANEL DE PROVEEDOR - GESTIÓN DE INVENTARIO Y PRODUCCIÓN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel infoLabel = new JLabel("Control de Stock, Plantas Manufactureras y Producción");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(220, 220, 255));
        
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setBackground(new Color(138, 43, 226));
        leftPanel.add(titleLabel);
        leftPanel.add(infoLabel);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Estadísticas del proveedor
        panel.add(createStatsCard("📦 ARTÍCULOS", 
            String.valueOf(articuloServicio.getCantidadArticulos()),
            "Total en catálogo",
            new Color(52, 152, 219)));
        
        panel.add(createStatsCard("🏭 PLANTAS", 
            String.valueOf(articuloServicio.getResumenStockPorPlanta().size()),
            "Manufactureras activas",
            new Color(46, 204, 113)));
        
        panel.add(createStatsCard("📈 DISPONIBLES", 
            String.valueOf(articuloServicio.getArticulosDisponibles().size()),
            "Con stock disponible",
            new Color(155, 89, 182)));
        
        panel.add(createStatsCard("⚠️ BAJO STOCK", 
            String.valueOf(articuloServicio.getArticulosConBajoStock().size()),
            "Requieren reabastecimiento",
            new Color(231, 76, 60)));
        
        // Calcular total de unidades en stock
        int totalStock = articuloServicio.listarArticulos().stream()
            .mapToInt(Articulo::getTotalStock)
            .sum();
        
        panel.add(createStatsCard("📊 STOCK TOTAL", 
            String.valueOf(totalStock),
            "Unidades en inventario",
            new Color(243, 156, 18)));
        
        panel.add(createStatsCard("🔄 DEMANDA", 
            String.valueOf(pedidoServicio.getPedidosPendientes().size()),
            "Pedidos pendientes",
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
    
    private JPanel createArticulosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título y botones
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("GESTIÓN DE ARTÍCULOS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton nuevoArticuloBtn = new JButton("➕ Nuevo Artículo");
        JButton actualizarBtn = new JButton("🔄 Actualizar");
        
        // Configurar botones con máximo contraste FORZADO
        configurarBotonConMaximoContraste(nuevoArticuloBtn, new Color(138, 43, 226), Color.WHITE);
        configurarBotonConMaximoContraste(actualizarBtn, new Color(70, 130, 180), Color.WHITE);
        
        nuevoArticuloBtn.addActionListener(e -> mostrarFormularioArticulo(null));
        actualizarBtn.addActionListener(e -> actualizarTablaArticulos());
        
        buttonPanel.add(nuevoArticuloBtn);
        buttonPanel.add(actualizarBtn);
        
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Tabla de artículos
        String[] columns = {"Código", "Nombre", "Precio", "Stock Total", "Plantas", "Estado", "Acciones"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Solo la columna de acciones
            }
        };
        
        JTable table = new JTable(model);
        configurarTablaConColores(table);
        
        // Configurar editor y renderer para la columna de botones "Acciones"
        table.getColumn("Acciones").setCellRenderer(new ArticuloButtonRenderer());
        table.getColumn("Acciones").setCellEditor(new ArticuloButtonEditor(new JCheckBox(), table, this));
        
        // Llenar tabla
        actualizarTablaArticulos(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void actualizarTablaArticulos() {
        // Actualizar desde botón
        actualizarTablaArticulos(null);
    }
    
    private void actualizarTablaArticulos(DefaultTableModel model) {
        if (model != null) {
            model.setRowCount(0); // Limpiar tabla
            
            List<Articulo> articulos = articuloServicio.listarArticulos();
            for (Articulo articulo : articulos) {
                boolean bajoStock = articuloServicio.getArticulosConBajoStock().contains(articulo);
                String estado = bajoStock ? "BAJO STOCK" : "NORMAL";
                
                Object[] row = {
                    articulo.getCodigo(),
                    articulo.getNombre(),
                    String.format("$%.2f", articulo.getPrecio()),
                    articulo.getTotalStock(),
                    articulo.getCantidadPorPlanta().size(),
                    estado,
                    "Gestionar"
                };
                model.addRow(row);
            }
        }
    }
    
    private JPanel createInventarioPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titleLabel = new JLabel("CONTROL DE INVENTARIO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Panel dividido
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.6);
        
        // Panel izquierdo - Lista de artículos
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Artículos"));
        
        DefaultListModel<String> articulosModel = new DefaultListModel<>();
        List<Articulo> articulos = articuloServicio.listarArticulos();
        for (Articulo articulo : articulos) {
            articulosModel.addElement(String.format("%s - %s (Stock: %d)", 
                articulo.getCodigo(), articulo.getNombre(), articulo.getTotalStock()));
        }
        
        JList<String> articulosList = new JList<>(articulosModel);
        articulosList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = articulosList.getSelectedIndex();
                if (index >= 0 && index < articulos.size()) {
                    mostrarDetalleInventario(articulos.get(index), splitPane.getRightComponent());
                }
            }
        });
        
        leftPanel.add(new JScrollPane(articulosList), BorderLayout.CENTER);
        splitPane.setLeftComponent(leftPanel);
        
        // Panel derecho - Detalles del artículo seleccionado
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Detalles de Inventario"));
        
        JLabel seleccionLabel = new JLabel("Seleccione un artículo para ver detalles");
        seleccionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(seleccionLabel, BorderLayout.CENTER);
        
        splitPane.setRightComponent(rightPanel);
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void mostrarDetalleInventario(Articulo articulo, Component rightComponent) {
        if (rightComponent instanceof JPanel) {
            JPanel rightPanel = (JPanel) rightComponent;
            rightPanel.removeAll();
            
            // Información del artículo
            JPanel infoPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;
            
            addInfoRow(infoPanel, gbc, 0, "Código:", articulo.getCodigo());
            addInfoRow(infoPanel, gbc, 1, "Nombre:", articulo.getNombre());
            addInfoRow(infoPanel, gbc, 2, "Precio:", String.format("$%.2f", articulo.getPrecio()));
            addInfoRow(infoPanel, gbc, 3, "Stock Total:", String.valueOf(articulo.getTotalStock()));
            
            rightPanel.add(infoPanel, BorderLayout.NORTH);
            
            // Tabla de plantas
            String[] columns = {"Planta", "Stock", "Stock Mínimo", "Estado", "Acciones"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            
            for (String planta : articulo.getCantidadPorPlanta().keySet()) {
                int stock = articulo.getCantidadPorPlanta().get(planta);
                int stockMinimo = articulo.getStockMinimoPorPlanta().getOrDefault(planta, 0);
                String estado = articulo.necesitaReabastecimiento(planta) ? "REABASTECER" : "OK";
                
                Object[] row = {planta, stock, stockMinimo, estado, "Actualizar"};
                model.addRow(row);
            }
            
            JTable table = new JTable(model);
            table.setRowHeight(25);
            table.getTableHeader().setBackground(new Color(70, 130, 180));
            table.getTableHeader().setForeground(Color.WHITE);
            
            rightPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            
            // Botones de acción
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton actualizarStockBtn = new JButton("📦 Actualizar Stock");
            JButton agregarPlantaBtn = new JButton("🏭 Agregar Planta");
            
            actualizarStockBtn.addActionListener(e -> mostrarDialogoActualizarStock(articulo));
            agregarPlantaBtn.addActionListener(e -> mostrarDialogoAgregarPlanta(articulo));
            
            buttonPanel.add(actualizarStockBtn);
            buttonPanel.add(agregarPlantaBtn);
            rightPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            rightPanel.revalidate();
            rightPanel.repaint();
        }
    }
    
    private JPanel createPlantasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titleLabel = new JLabel("MONITOREO DE PLANTAS MANUFACTURERAS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Resumen por plantas
        Map<String, Integer> resumenPlantas = articuloServicio.getResumenStockPorPlanta();
        
        String[] columns = {"Planta", "Total Unidades", "Artículos", "Eficiencia", "Estado"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        for (Map.Entry<String, Integer> entry : resumenPlantas.entrySet()) {
            String planta = entry.getKey();
            int totalUnidades = entry.getValue();
            
            List<Articulo> articulosPlanta = articuloServicio.getArticulosPorPlanta(planta);
            int cantidadArticulos = articulosPlanta.size();
            
            // Calcular eficiencia
            double eficiencia = cantidadArticulos > 0 ? (double) totalUnidades / cantidadArticulos : 0;
            String nivelEficiencia = eficiencia > 100 ? "ALTA" : eficiencia > 50 ? "MEDIA" : "BAJA";
            
            // Verificar si hay artículos con bajo stock en esta planta
            boolean hayBajoStock = articulosPlanta.stream()
                .anyMatch(articulo -> articulo.necesitaReabastecimiento(planta));
            String estado = hayBajoStock ? "REQUIERE ATENCIÓN" : "OPERATIVA";
            
            Object[] row = {
                planta,
                totalUnidades,
                cantidadArticulos,
                nivelEficiencia,
                estado
            };
            model.addRow(row);
        }
        
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAlertasPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de alertas de stock
        JPanel stockPanel = new JPanel(new BorderLayout());
        stockPanel.setBorder(BorderFactory.createTitledBorder("🚨 Alertas de Stock Bajo"));
        
        List<Articulo> articulosBajoStock = articuloServicio.getArticulosConBajoStock();
        
        if (articulosBajoStock.isEmpty()) {
            JLabel noAlertasLabel = new JLabel("✅ No hay artículos con stock bajo");
            noAlertasLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noAlertasLabel.setFont(new Font("Arial", Font.BOLD, 16));
            noAlertasLabel.setForeground(new Color(0, 128, 0));
            stockPanel.add(noAlertasLabel, BorderLayout.CENTER);
        } else {
            String[] columns = {"Artículo", "Planta", "Stock Actual", "Stock Mínimo", "Déficit"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            
            for (Articulo articulo : articulosBajoStock) {
                for (String planta : articulo.getCantidadPorPlanta().keySet()) {
                    if (articulo.necesitaReabastecimiento(planta)) {
                        int stock = articulo.getCantidadPorPlanta().get(planta);
                        int minimo = articulo.getStockMinimoPorPlanta().getOrDefault(planta, 0);
                        int deficit = minimo - stock;
                        
                        Object[] row = {
                            articulo.getNombre(),
                            planta,
                            stock,
                            minimo,
                            deficit
                        };
                        model.addRow(row);
                    }
                }
            }
            
            JTable table = new JTable(model);
            table.setRowHeight(25);
            table.getTableHeader().setBackground(new Color(220, 20, 60));
            table.getTableHeader().setForeground(Color.WHITE);
            
            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
            
            // Panel de botones para el reporte de stock
            JPanel buttonPanel = new JPanel(new FlowLayout());
            
            JButton reporteDetalladoBtn = new JButton("📊 Reporte Detallado de Stock Necesario");
            reporteDetalladoBtn.setBackground(new Color(255, 140, 0));
            reporteDetalladoBtn.setForeground(Color.WHITE);
            reporteDetalladoBtn.setFont(new Font("Arial", Font.BOLD, 12));
            reporteDetalladoBtn.addActionListener(e -> mostrarReporteStockDetallado());
            
            buttonPanel.add(reporteDetalladoBtn);
            tablePanel.add(buttonPanel, BorderLayout.SOUTH);
            
            stockPanel.add(tablePanel, BorderLayout.CENTER);
        }
        
        // Panel de demanda pendiente
        JPanel demandaPanel = new JPanel(new BorderLayout());
        demandaPanel.setBorder(BorderFactory.createTitledBorder("📋 Demanda Pendiente"));
        
        List<Pedido> pedidosPendientes = pedidoServicio.getPedidosPendientes();
        
        if (pedidosPendientes.isEmpty()) {
            JLabel noDemandaLabel = new JLabel("✅ No hay pedidos pendientes");
            noDemandaLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noDemandaLabel.setFont(new Font("Arial", Font.BOLD, 16));
            noDemandaLabel.setForeground(new Color(0, 128, 0));
            demandaPanel.add(noDemandaLabel, BorderLayout.CENTER);
        } else {
            // Calcular demanda por artículo
            java.util.Map<String, Integer> demandaPorArticulo = new java.util.HashMap<>();
            
            for (Pedido pedido : pedidosPendientes) {
                for (DetallePedido detalle : pedido.getDetalles()) {
                    String codigo = detalle.getArticulo().getCodigo();
                    int cantidad = detalle.getCantidadPendiente();
                    demandaPorArticulo.put(codigo, 
                        demandaPorArticulo.getOrDefault(codigo, 0) + cantidad);
                }
            }
            
            String[] columns = {"Artículo", "Cantidad Pendiente", "Stock Disponible", "Estado"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            
            for (Map.Entry<String, Integer> entry : demandaPorArticulo.entrySet()) {
                Articulo articulo = articuloServicio.buscarArticulo(entry.getKey());
                if (articulo != null) {
                    int demanda = entry.getValue();
                    int stock = articulo.getTotalStock();
                    String estado = stock >= demanda ? "DISPONIBLE" : "INSUFICIENTE";
                    
                    Object[] row = {
                        articulo.getNombre(),
                        demanda,
                        stock,
                        estado
                    };
                    model.addRow(row);
                }
            }
            
            JTable table = new JTable(model);
            table.setRowHeight(25);
            table.getTableHeader().setBackground(new Color(70, 130, 180));
            table.getTableHeader().setForeground(Color.WHITE);
            
            demandaPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        }
        
        panel.add(stockPanel);
        panel.add(demandaPanel);
        
        return panel;
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
            tabbedPane.addTab("🏭 Dashboard", createDashboardPanel());
            tabbedPane.addTab("📦 Artículos", createArticulosPanel());
            tabbedPane.addTab("📊 Inventario", createInventarioPanel());
            tabbedPane.addTab("🏢 Plantas", createPlantasPanel());
            tabbedPane.addTab("⚠️ Alertas", createAlertasPanel());
        });
        
        footerPanel.add(volverBtn);
        footerPanel.add(actualizarBtn);
        
        return footerPanel;
    }
    
    private void addInfoRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(labelComponent, gbc);
        
        gbc.gridx = 1;
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(valueComponent, gbc);
    }
    
    private void mostrarFormularioArticulo(Articulo articulo) {
        JDialog dialog = new JDialog(this, articulo == null ? "Nuevo Artículo" : "Editar Artículo", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Campos del formulario
        JTextField codigoField = new JTextField(20);
        JTextField nombreField = new JTextField(20);
        JTextArea descripcionArea = new JTextArea(3, 20);
        JTextField precioField = new JTextField(20);
        
        if (articulo != null) {
            codigoField.setText(articulo.getCodigo());
            nombreField.setText(articulo.getNombre());
            descripcionArea.setText(articulo.getDescripcion());
            precioField.setText(String.valueOf(articulo.getPrecio()));
            codigoField.setEditable(false);
        }
        
        // Agregar campos al panel
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; panel.add(codigoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; panel.add(nombreField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(descripcionArea), gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1; panel.add(precioField, gbc);
        
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
                String descripcion = descripcionArea.getText().trim();
                double precio = Double.parseDouble(precioField.getText().trim());
                
                if (articulo == null) {
                    // Nuevo artículo
                    Articulo nuevoArticulo = new Articulo(codigo, nombre, descripcion, precio);
                    if (articuloServicio.agregarArticulo(nuevoArticulo)) {
                        JOptionPane.showMessageDialog(dialog, "Artículo creado exitosamente");
                        dialog.dispose();
                        actualizarTablaArticulos();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Error: Artículo con ese código ya existe");
                    }
                } else {
                    // Editar artículo existente
                    articulo.setNombre(nombre);
                    articulo.setDescripcion(descripcion);
                    articulo.setPrecio(precio);
                    if (articuloServicio.actualizarArticulo(articulo)) {
                        JOptionPane.showMessageDialog(dialog, "Artículo actualizado exitosamente");
                        dialog.dispose();
                        actualizarTablaArticulos();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Error al actualizar artículo");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: Verifique el precio");
            }
        });
        
        cancelarBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(guardarBtn);
        buttonPanel.add(cancelarBtn);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void mostrarDialogoActualizarStock(Articulo articulo) {
        List<String> plantas = new java.util.ArrayList<>(articulo.getCantidadPorPlanta().keySet());
        
        if (plantas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El artículo no tiene plantas asignadas");
            return;
        }
        
        String plantaSeleccionada = (String) JOptionPane.showInputDialog(this,
            "Seleccione la planta:",
            "Actualizar Stock",
            JOptionPane.QUESTION_MESSAGE,
            null,
            plantas.toArray(),
            plantas.get(0));
        
        if (plantaSeleccionada != null) {
            int stockActual = articulo.getCantidadPorPlanta().get(plantaSeleccionada);
            
            // Diálogo con opciones claras
            String[] opciones = {"Establecer Nuevo Stock", "Agregar al Stock Actual", "Reducir del Stock Actual", "Cancelar"};
            int opcion = JOptionPane.showOptionDialog(this,
                "Stock actual en " + plantaSeleccionada + ": " + stockActual + "\n" +
                "¿Qué desea hacer?",
                "Actualizar Stock",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);
            
            if (opcion >= 0 && opcion < 3) {
                String prompt = "";
                switch (opcion) {
                    case 0: prompt = "Ingrese el nuevo stock total:"; break;
                    case 1: prompt = "Ingrese cantidad a agregar:"; break;
                    case 2: prompt = "Ingrese cantidad a reducir:"; break;
                }
                
                String cantidad = JOptionPane.showInputDialog(this, prompt, "0");
                
                if (cantidad != null) {
                    try {
                        int cantidadNumerica = Integer.parseInt(cantidad.trim());
                        
                        switch (opcion) {
                            case 0: // Establecer nuevo stock
                                if (cantidadNumerica >= 0) {
                                    articuloServicio.establecerStock(articulo.getCodigo(), plantaSeleccionada, cantidadNumerica);
                                    // Verificar que se actualizó correctamente
                                    Articulo articuloActualizado = articuloServicio.buscarArticulo(articulo.getCodigo());
                                    int stockVerificado = articuloActualizado.getCantidadPorPlanta().get(plantaSeleccionada);
                                    JOptionPane.showMessageDialog(this, 
                                        "Stock establecido exitosamente\n" +
                                        "Planta: " + plantaSeleccionada + "\n" +
                                        "Stock anterior: " + stockActual + "\n" +
                                        "Nuevo stock: " + stockVerificado);
                                } else {
                                    JOptionPane.showMessageDialog(this, "El stock no puede ser negativo");
                                }
                                break;
                                
                            case 1: // Agregar stock
                                if (cantidadNumerica > 0) {
                                    articuloServicio.actualizarStock(articulo.getCodigo(), plantaSeleccionada, cantidadNumerica);
                                    // Actualizar referencia del artículo
                                    Articulo articuloActualizado = articuloServicio.buscarArticulo(articulo.getCodigo());
                                    int nuevoStock = articuloActualizado.getCantidadPorPlanta().get(plantaSeleccionada);
                                    JOptionPane.showMessageDialog(this, 
                                        "Stock agregado exitosamente\n" +
                                        "Planta: " + plantaSeleccionada + "\n" +
                                        "Stock anterior: " + stockActual + "\n" +
                                        "Nuevo stock: " + nuevoStock);
                                } else {
                                    JOptionPane.showMessageDialog(this, "La cantidad a agregar debe ser positiva");
                                }
                                break;
                                
                            case 2: // Reducir stock
                                if (cantidadNumerica > 0) {
                                    int nuevoStock = stockActual - cantidadNumerica;
                                    if (nuevoStock >= 0) {
                                        articuloServicio.actualizarStock(articulo.getCodigo(), plantaSeleccionada, -cantidadNumerica);
                                        // Actualizar referencia del artículo
                                        Articulo articuloActualizado = articuloServicio.buscarArticulo(articulo.getCodigo());
                                        int stockFinal = articuloActualizado.getCantidadPorPlanta().get(plantaSeleccionada);
                                        JOptionPane.showMessageDialog(this, 
                                            "Stock reducido exitosamente\n" +
                                            "Planta: " + plantaSeleccionada + "\n" +
                                            "Stock anterior: " + stockActual + "\n" +
                                            "Nuevo stock: " + stockFinal);
                                    } else {
                                        JOptionPane.showMessageDialog(this, 
                                            "No se puede reducir más stock del disponible\n" +
                                            "Stock actual: " + stockActual);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(this, "La cantidad a reducir debe ser positiva");
                                }
                                break;
                        }
                        
                        // Actualizar la vista de artículos y dashboard
                        tabbedPane.setComponentAt(0, createDashboardPanel()); // Dashboard
                        tabbedPane.setComponentAt(1, createArticulosPanel()); // Artículos
                        
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Cantidad inválida");
                    }
                }
            }
        }
    }
    
    private void mostrarDialogoAgregarPlanta(Articulo articulo) {
        JDialog dialog = new JDialog(this, "Agregar Planta", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField plantaField = new JTextField(20);
        JTextField stockField = new JTextField(20);
        JTextField minimoField = new JTextField(20);
        
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Nombre Planta:"), gbc);
        gbc.gridx = 1; panel.add(plantaField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Stock Inicial:"), gbc);
        gbc.gridx = 1; panel.add(stockField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Stock Mínimo:"), gbc);
        gbc.gridx = 1; panel.add(minimoField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton agregarBtn = new JButton("Agregar");
        JButton cancelarBtn = new JButton("Cancelar");
        
        agregarBtn.addActionListener(e -> {
            try {
                String planta = plantaField.getText().trim();
                int stock = Integer.parseInt(stockField.getText().trim());
                int minimo = Integer.parseInt(minimoField.getText().trim());
                
                if (!planta.isEmpty()) {
                    articulo.agregarPlanta(planta, stock, minimo);
                    articuloServicio.actualizarArticulo(articulo);
                    JOptionPane.showMessageDialog(dialog, "Planta agregada exitosamente");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Ingrese nombre de la planta");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Verifique los valores numéricos");
            }
        });
        
        cancelarBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(agregarBtn);
        buttonPanel.add(cancelarBtn);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
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
                        setBackground(new Color(255, 248, 220)); // Naranja muy claro
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
     * Método para gestionar un artículo específico
     */
    public void gestionarArticulo(String codigoArticulo) {
        Articulo articulo = articuloServicio.buscarArticulo(codigoArticulo);
        if (articulo == null) {
            JOptionPane.showMessageDialog(this, "Artículo no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        mostrarDialogoGestionArticulo(articulo);
    }
    
    /**
     * Método para mostrar el diálogo de gestión de artículos
     */
    private void mostrarDialogoGestionArticulo(Articulo articulo) {
        JDialog dialog = new JDialog(this, "Gestionar Artículo: " + articulo.getCodigo(), true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        
        // Información del artículo
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(articulo.getCodigo()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(articulo.getNombre()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(String.format("$%.2f", articulo.getPrecio())), gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Stock Total:"), gbc);
        gbc.gridx = 1;
        JLabel stockLabel = new JLabel(String.valueOf(articulo.getTotalStock()));
        stockLabel.setFont(new Font("Arial", Font.BOLD, 12));
        stockLabel.setForeground(new Color(25, 25, 112));
        infoPanel.add(stockLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        infoPanel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(articulo.getDescripcion()), gbc);
        
        panel.add(infoPanel, BorderLayout.NORTH);
        
        // Tabla de stock por plantas
        String[] columns = {"Planta", "Stock Actual", "Stock Mínimo"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        Map<String, Integer> stockPorPlanta = articulo.getCantidadPorPlanta();
        for (Map.Entry<String, Integer> entry : stockPorPlanta.entrySet()) {
            Object[] row = {
                entry.getKey(),
                entry.getValue(),
                "10" // Stock mínimo ejemplo
            };
            model.addRow(row);
        }
        
        JTable table = new JTable(model);
        configurarTablaConColores(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de acciones
        JPanel accionesPanel = new JPanel(new FlowLayout());
        
        // Botón para editar artículo
        JButton editarBtn = new JButton("Editar Artículo");
        configurarBotonConMaximoContraste(editarBtn, new Color(255, 140, 0), Color.WHITE);
        
        editarBtn.addActionListener(e -> {
            dialog.dispose();
            mostrarFormularioArticulo(articulo);
        });
        
        // Botón para actualizar stock
        JButton actualizarStockBtn = new JButton("Actualizar Stock");
        configurarBotonConMaximoContraste(actualizarStockBtn, new Color(34, 139, 34), Color.WHITE);
        
        actualizarStockBtn.addActionListener(e -> {
            mostrarDialogoActualizarStock(articulo, dialog);
        });
        
        // Botón para agregar nueva planta
        JButton nuevaPlantaBtn = new JButton("Agregar Planta");
        configurarBotonConMaximoContraste(nuevaPlantaBtn, new Color(138, 43, 226), Color.WHITE);
        
        nuevaPlantaBtn.addActionListener(e -> {
            mostrarDialogoAgregarPlanta(articulo, dialog);
        });
        
        // Botón cerrar
        JButton cerrarBtn = new JButton("Cerrar");
        configurarBotonConMaximoContraste(cerrarBtn, new Color(70, 130, 180), Color.WHITE);
        cerrarBtn.addActionListener(e -> dialog.dispose());
        
        accionesPanel.add(editarBtn);
        accionesPanel.add(actualizarStockBtn);
        accionesPanel.add(nuevaPlantaBtn);
        accionesPanel.add(cerrarBtn);
        
        panel.add(accionesPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Método para mostrar diálogo de actualización de stock
     */
    private void mostrarDialogoActualizarStock(Articulo articulo, JDialog parentDialog) {
        String[] plantas = articulo.getCantidadPorPlanta().keySet().toArray(new String[0]);
        
        if (plantas.length == 0) {
            JOptionPane.showMessageDialog(parentDialog, "No hay plantas disponibles");
            return;
        }
        
        String plantaSeleccionada = (String) JOptionPane.showInputDialog(
            parentDialog,
            "Seleccione la planta:",
            "Actualizar Stock",
            JOptionPane.QUESTION_MESSAGE,
            null,
            plantas,
            plantas[0]
        );
        
        if (plantaSeleccionada != null) {
            String nuevoStockStr = JOptionPane.showInputDialog(
                parentDialog,
                "Stock actual en " + plantaSeleccionada + ": " + 
                articulo.getCantidadPorPlanta().get(plantaSeleccionada) + 
                "\n\nIngrese el nuevo stock:",
                "Actualizar Stock"
            );
            
            if (nuevoStockStr != null) {
                try {
                    int nuevoStock = Integer.parseInt(nuevoStockStr);
                    if (nuevoStock >= 0) {
                        articuloServicio.establecerStock(articulo.getCodigo(), plantaSeleccionada, nuevoStock);
                        JOptionPane.showMessageDialog(parentDialog, 
                            "Stock actualizado exitosamente\n" +
                            "Planta: " + plantaSeleccionada + "\n" +
                            "Nuevo stock: " + nuevoStock);
                        
                        // Actualizar la tabla de artículos y el dashboard
                        tabbedPane.setComponentAt(0, createDashboardPanel()); // Dashboard
                        tabbedPane.setComponentAt(1, createArticulosPanel()); // Artículos
                        parentDialog.dispose();
                        mostrarDialogoGestionArticulo(articulo);
                    } else {
                        JOptionPane.showMessageDialog(parentDialog, 
                            "El stock no puede ser negativo");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(parentDialog, 
                        "Ingrese un número válido");
                }
            }
        }
    }
    
    /**
     * Método para mostrar diálogo de agregar nueva planta
     */
    private void mostrarDialogoAgregarPlanta(Articulo articulo, JDialog parentDialog) {
        String nombrePlanta = JOptionPane.showInputDialog(
            parentDialog,
            "Ingrese el nombre de la nueva planta:",
            "Agregar Nueva Planta",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (nombrePlanta != null && !nombrePlanta.trim().isEmpty()) {
            String stockInicialStr = JOptionPane.showInputDialog(
                parentDialog,
                "Ingrese el stock inicial para la planta " + nombrePlanta + ":",
                "Stock Inicial"
            );
            
            if (stockInicialStr != null) {
                try {
                    int stockInicial = Integer.parseInt(stockInicialStr);
                    if (stockInicial >= 0) {
                        
                        // Pedir también el stock mínimo
                        String stockMinimoStr = JOptionPane.showInputDialog(
                            parentDialog,
                            "Ingrese el stock mínimo para la planta " + nombrePlanta + ":",
                            "10"  // Valor por defecto
                        );
                        
                        if (stockMinimoStr != null) {
                            try {
                                int stockMinimo = Integer.parseInt(stockMinimoStr);
                                if (stockMinimo >= 0) {
                                    articulo.agregarPlanta(nombrePlanta, stockInicial, stockMinimo);
                                    JOptionPane.showMessageDialog(parentDialog, 
                                        "Planta agregada exitosamente\n" +
                                        "Planta: " + nombrePlanta + "\n" +
                                        "Stock inicial: " + stockInicial + "\n" +
                                        "Stock mínimo: " + stockMinimo);
                                    
                                    // Actualizar la tabla de artículos
                                    tabbedPane.setComponentAt(0, createDashboardPanel()); // Dashboard
                                    tabbedPane.setComponentAt(1, createArticulosPanel()); // Artículos
                                    parentDialog.dispose();
                                    mostrarDialogoGestionArticulo(articulo);
                                } else {
                                    JOptionPane.showMessageDialog(parentDialog, 
                                        "El stock mínimo no puede ser negativo");
                                }
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(parentDialog, 
                                    "Stock mínimo inválido");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(parentDialog, 
                            "El stock no puede ser negativo");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(parentDialog, 
                        "Ingrese un número válido");
                }
            }
        }
    }
    
    /**
     * Muestra un reporte detallado de cuánto stock exacto necesita cada artículo
     */
    private void mostrarReporteStockDetallado() {
        String reporte = articuloServicio.getReporteCompletoStockBajo();
        
        JDialog dialog = new JDialog(this, "📊 Reporte Detallado: Stock Necesario", true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);
        
        JTextArea textArea = new JTextArea(reporte);
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setBackground(new Color(245, 245, 245));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Cantidad Exacta de Stock Necesario"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton cerrarBtn = new JButton("Cerrar");
        cerrarBtn.addActionListener(e -> dialog.dispose());
        
        JButton actualizarBtn = new JButton("🔄 Actualizar Stock");
        actualizarBtn.setBackground(new Color(70, 130, 180));
        actualizarBtn.setForeground(Color.WHITE);
        actualizarBtn.addActionListener(e -> {
            dialog.dispose();
            // Ir a la pestaña de inventario para actualizar stock
            tabbedPane.setSelectedIndex(2); // Índice de la pestaña "Inventario"
        });
        
        buttonPanel.add(actualizarBtn);
        buttonPanel.add(cerrarBtn);
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}

/**
 * Renderer para botones en la tabla de artículos
 */
class ArticuloButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
    public ArticuloButtonRenderer() {
        setOpaque(true);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Acción" : value.toString());
        setBackground(new Color(25, 25, 112));  // Azul marino consistente
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 11));
        return this;
    }
}

/**
 * Editor para botones en la tabla de artículos del Proveedor
 */
class ArticuloButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private VentanaProveedor ventanaProveedor;
    
    public ArticuloButtonEditor(JCheckBox checkBox, JTable table, VentanaProveedor ventanaProveedor) {
        super(checkBox);
        this.table = table;
        this.ventanaProveedor = ventanaProveedor;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        label = (value == null) ? "Acción" : value.toString();
        button.setText(label);
        button.setBackground(new Color(25, 25, 112));  // Azul marino consistente
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 11));
        isPushed = true;
        return button;
    }
    
    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            // Obtener el código del artículo de la fila seleccionada
            int row = table.getSelectedRow();
            if (row >= 0) {
                String codigoArticulo = (String) table.getValueAt(row, 0);
                // Gestionar el artículo
                ventanaProveedor.gestionarArticulo(codigoArticulo);
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
