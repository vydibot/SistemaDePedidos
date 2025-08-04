package gui;

import modelo.*;
import servicio.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * Ventana principal para el cliente - Versión corregida
 */
public class VentanaCliente extends JFrame {
    private Cliente cliente;
    private PedidoServicio pedidoServicio;
    private ArticuloServicio articuloServicio;
    private VentanaPrincipal ventanaPrincipal;
    private JTabbedPane tabbedPane;
    
    public VentanaCliente(Cliente cliente, PedidoServicio pedidoServicio, ArticuloServicio articuloServicio, VentanaPrincipal ventanaPrincipal) {
        this.cliente = cliente;
        this.pedidoServicio = pedidoServicio;
        this.articuloServicio = articuloServicio;
        this.ventanaPrincipal = ventanaPrincipal;
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        setTitle("Sistema de Pedidos - Cliente: " + cliente.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content with tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("🛍️ Catálogo", createCatalogoPanel());
        tabbedPane.addTab("📋 Mis Pedidos", createPedidosPanel());
        tabbedPane.addTab("👤 Mi Perfil", createPerfilPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(46, 125, 50));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel welcomeLabel = new JLabel("¡Bienvenido " + cliente.getNombre() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        
        JLabel infoLabel = new JLabel("Límite de crédito: $" + String.format("%.2f", cliente.getLimiteCredito()) + 
                                     " | Descuento: " + cliente.getPorcentajeDescuento() + "%");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(220, 255, 220));
        
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setBackground(new Color(46, 125, 50));
        leftPanel.add(welcomeLabel);
        leftPanel.add(infoLabel);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    private JPanel createCatalogoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titleLabel = new JLabel("CATÁLOGO DE PRODUCTOS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Tabla de productos con colores mejorados
        String[] columns = {"Código", "Nombre", "Descripción", "Precio", "Stock", "Cantidad", "Agregar"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6; // Cantidad y Agregar son editables
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) return Integer.class; // Columna Cantidad
                return String.class;
            }
        };
        
        JTable table = new JTable(model);
        configurarTablaConColores(table);
        
        // Configurar editor para la columna Cantidad (Spinner)
        table.getColumn("Cantidad").setCellEditor(new DefaultCellEditor(new JTextField()) {
            private JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
            
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                spinner.setValue(value != null ? value : 1);
                return spinner;
            }
            
            @Override
            public Object getCellEditorValue() {
                return spinner.getValue();
            }
        });
        
        // Configurar editor y renderer para la columna de botones "Agregar al Carrito"
        table.getColumn("Agregar").setCellRenderer(new ButtonRenderer());
        table.getColumn("Agregar").setCellEditor(new ButtonEditor(new JCheckBox(), table, this));
        
        // Llenar tabla con artículos
        List<Articulo> articulos = articuloServicio.getArticulosDisponibles();
        for (Articulo articulo : articulos) {
            Object[] row = {
                articulo.getCodigo(),
                articulo.getNombre(),
                articulo.getDescripcion(),
                String.format("$%.2f", articulo.getPrecio()),
                articulo.getTotalStock(),
                1, // Cantidad por defecto
                "Agregar al Carrito"
            };
            model.addRow(row);
        }
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton crearPedidoBtn = new JButton("🛒 Crear Pedido Directo");
        
        // Configurar botón con máximo contraste FORZADO
        configurarBotonConMaximoContraste(crearPedidoBtn, new Color(25, 25, 112), Color.WHITE);
        crearPedidoBtn.addActionListener(e -> mostrarFormularioCrearPedido());
        
        buttonPanel.add(crearPedidoBtn);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createPedidosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titleLabel = new JLabel("MIS PEDIDOS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Tabla de pedidos con colores mejorados
        String[] columns = {"Código", "Fecha", "Dirección", "Total", "Estado", "Detalles"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Solo la columna Detalles es editable
            }
        };
        
        JTable table = new JTable(model);
        configurarTablaConColores(table);
        
        // Configurar editor y renderer para la columna de botones "Ver Detalles"
        table.getColumn("Detalles").setCellRenderer(new ButtonRenderer());
        table.getColumn("Detalles").setCellEditor(new PedidoButtonEditor(new JCheckBox(), table, this));
        
        // Llenar tabla con pedidos del cliente
        List<Pedido> pedidos = pedidoServicio.getPedidosPorCliente(cliente.getCodigo());
        for (Pedido pedido : pedidos) {
            Object[] row = {
                pedido.getNumeroPedido(),
                pedido.getFechaPedido().toString(),
                pedido.getDireccionEnvio(),
                String.format("$%.2f", pedido.getMontoTotal()),
                pedido.getEstado().toString(),
                "Ver Detalles"
            };
            model.addRow(row);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createPerfilPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titleLabel = new JLabel("MI PERFIL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Panel de información con colores mejorados
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createLineBorder(new Color(46, 125, 50), 2));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        addInfoRowConColores(infoPanel, gbc, 0, "Código:", cliente.getCodigo());
        addInfoRowConColores(infoPanel, gbc, 1, "Nombre:", cliente.getNombre());
        addInfoRowConColores(infoPanel, gbc, 2, "Límite de Crédito:", String.format("$%.2f", cliente.getLimiteCredito()));
        addInfoRowConColores(infoPanel, gbc, 3, "Descuento:", cliente.getPorcentajeDescuento() + "%");
        addInfoRowConColores(infoPanel, gbc, 4, "Saldo Actual:", String.format("$%.2f", cliente.getSaldo()));
        
        panel.add(infoPanel, BorderLayout.CENTER);
        
        // Panel de direcciones
        JPanel direccionesPanel = createDireccionesPanel();
        panel.add(direccionesPanel, BorderLayout.SOUTH);
        
        // Botón para actualizar perfil
        JPanel footerPanel = new JPanel(new FlowLayout());
        JButton actualizarPerfilBtn = new JButton("🔄 Actualizar Perfil");
        configurarBotonConMaximoContraste(actualizarPerfilBtn, new Color(70, 130, 180), Color.WHITE);
        
        actualizarPerfilBtn.addActionListener(e -> {
            // Recrear el panel de perfil para mostrar cambios
            tabbedPane.setComponentAt(2, createPerfilPanel());
            tabbedPane.revalidate();
            tabbedPane.repaint();
        });
        
        footerPanel.add(actualizarPerfilBtn);
        panel.add(footerPanel, BorderLayout.AFTER_LAST_LINE);
        
        return panel;
    }
    
    private JPanel createDireccionesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Direcciones de Envío"));
        panel.setPreferredSize(new Dimension(0, 200));
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        List<String> direcciones = cliente.getDireccionesEnvio();
        for (String direccion : direcciones) {
            listModel.addElement(direccion);
        }
        
        JList<String> direccionesList = new JList<>(listModel);
        direccionesList.setBackground(new Color(248, 250, 252));
        direccionesList.setBorder(BorderFactory.createLineBorder(new Color(46, 125, 50), 1));
        
        JScrollPane scrollPane = new JScrollPane(direccionesList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones para gestionar direcciones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton agregarBtn = new JButton("➕ Agregar Dirección");
        JButton editarBtn = new JButton("✏️ Editar");
        JButton eliminarBtn = new JButton("🗑️ Eliminar");
        
        // Configurar botones con colores consistentes
        configurarBotonConMaximoContraste(agregarBtn, new Color(25, 25, 112), Color.WHITE);
        configurarBotonConMaximoContraste(editarBtn, new Color(255, 140, 0), Color.WHITE);
        configurarBotonConMaximoContraste(eliminarBtn, new Color(220, 20, 60), Color.WHITE);
        
        // Acciones de los botones
        agregarBtn.addActionListener(e -> mostrarDialogoAgregarDireccion(listModel));
        
        editarBtn.addActionListener(e -> {
            int selectedIndex = direccionesList.getSelectedIndex();
            if (selectedIndex >= 0) {
                String direccionActual = listModel.getElementAt(selectedIndex);
                mostrarDialogoEditarDireccion(direccionActual, selectedIndex, listModel);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una dirección para editar", 
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        eliminarBtn.addActionListener(e -> {
            int selectedIndex = direccionesList.getSelectedIndex();
            if (selectedIndex >= 0) {
                int confirmacion = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de eliminar esta dirección?", 
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    // Eliminar de la lista del cliente
                    cliente.getDireccionesEnvio().remove(selectedIndex);
                    // Actualizar la lista visual
                    listModel.remove(selectedIndex);
                    JOptionPane.showMessageDialog(this, "Dirección eliminada exitosamente");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una dirección para eliminar", 
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        buttonPanel.add(agregarBtn);
        buttonPanel.add(editarBtn);
        buttonPanel.add(eliminarBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout());
        footerPanel.setBackground(new Color(240, 248, 255));
        
        JButton volverBtn = new JButton("🔙 Volver al Menú Principal");
        
        // Configurar botón con máximo contraste FORZADO
        configurarBotonConMaximoContraste(volverBtn, new Color(220, 20, 60), Color.WHITE);
        volverBtn.addActionListener(e -> {
            this.setVisible(false);
            ventanaPrincipal.setVisible(true);
        });
        
        footerPanel.add(volverBtn);
        
        return footerPanel;
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
                        setBackground(new Color(240, 255, 240)); // Verde muy claro
                        setForeground(Color.BLACK);
                    }
                }
                
                setFont(new Font("Arial", Font.PLAIN, 13));
                setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
                return this;
            }
        });
    }
    
    private void addInfoRowConColores(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 14));
        labelComponent.setForeground(new Color(46, 125, 50));
        panel.add(labelComponent, gbc);
        
        gbc.gridx = 1;
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        valueComponent.setForeground(Color.BLACK);
        panel.add(valueComponent, gbc);
    }
    
    private void mostrarFormularioCrearPedido() {
        JDialog dialog = new JDialog(this, "Crear Nuevo Pedido", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Título
        JLabel titleLabel = new JLabel("CREAR NUEVO PEDIDO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Dirección de envío (REQUERIDO según documento académico)
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Dirección de Envío:"), gbc);
        
        JComboBox<String> direccionCombo = new JComboBox<>();
        direccionCombo.setPreferredSize(new Dimension(200, 25));
        
        // Agregar direcciones existentes del cliente
        List<String> direcciones = cliente.getDireccionesEnvio();
        if (direcciones.isEmpty()) {
            direccionCombo.addItem("No hay direcciones registradas");
        } else {
            for (String direccion : direcciones) {
                direccionCombo.addItem(direccion);
            }
        }
        direccionCombo.addItem("+ Agregar nueva dirección...");
        
        gbc.gridx = 1;
        panel.add(direccionCombo, gbc);
        
        // Campo para nueva dirección
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Nueva Dirección:"), gbc);
        
        JTextField nuevaDireccionField = new JTextField(20);
        nuevaDireccionField.setEnabled(false);
        gbc.gridx = 1;
        panel.add(nuevaDireccionField, gbc);
        
        // Habilitar campo nueva dirección cuando se selecciona
        direccionCombo.addActionListener(e -> {
            boolean esNueva = "+ Agregar nueva dirección...".equals(direccionCombo.getSelectedItem());
            nuevaDireccionField.setEnabled(esNueva);
            if (esNueva) {
                nuevaDireccionField.requestFocus();
            }
        });
        
        // Selección de artículo
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Artículo:"), gbc);
        
        JComboBox<Articulo> articuloCombo = new JComboBox<>();
        List<Articulo> articulos = articuloServicio.getArticulosDisponibles();
        for (Articulo articulo : articulos) {
            articuloCombo.addItem(articulo);
        }
        articuloCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Articulo) {
                    Articulo art = (Articulo) value;
                    setText(art.getNombre() + " - $" + String.format("%.2f", art.getPrecio()));
                }
                return this;
            }
        });
        
        gbc.gridx = 1;
        panel.add(articuloCombo, gbc);
        
        // Cantidad ordenada (REQUERIDO según documento académico)
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Cantidad Ordenada:"), gbc);
        
        JSpinner cantidadSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        gbc.gridx = 1;
        panel.add(cantidadSpinner, gbc);
        
        // Información del cliente
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("<html><b>Cliente:</b> " + cliente.getNombre() + 
                                     "<br><b>Límite disponible:</b> $" + 
                                     String.format("%.2f", cliente.getLimiteCredito() - cliente.getSaldo()) + "</html>");
        infoLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 125, 50), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(infoLabel, gbc);
        
        // Botones
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 6;
        JButton crearBtn = new JButton("Crear Pedido");
        
        // Configurar botón crear con máximo contraste FORZADO
        configurarBotonConMaximoContraste(crearBtn, new Color(34, 139, 34), Color.WHITE);
        crearBtn.addActionListener(e -> {
            try {
                // Validar dirección de envío
                String direccionEnvio;
                if ("+ Agregar nueva dirección...".equals(direccionCombo.getSelectedItem())) {
                    direccionEnvio = nuevaDireccionField.getText().trim();
                    if (direccionEnvio.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Debe ingresar una dirección de envío");
                        return;
                    }
                    // Agregar la nueva dirección al cliente
                    cliente.agregarDireccionEnvio(direccionEnvio);
                    
                    // Mensaje informativo
                    JOptionPane.showMessageDialog(dialog, 
                        "Nueva dirección agregada a su perfil:\n" + direccionEnvio, 
                        "Dirección Guardada", JOptionPane.INFORMATION_MESSAGE);
                        
                } else if ("No hay direcciones registradas".equals(direccionCombo.getSelectedItem())) {
                    JOptionPane.showMessageDialog(dialog, "Debe agregar una dirección de envío para continuar");
                    return;
                } else {
                    direccionEnvio = (String) direccionCombo.getSelectedItem();
                }
                
                Articulo articuloSeleccionado = (Articulo) articuloCombo.getSelectedItem();
                int cantidadOrdenada = (Integer) cantidadSpinner.getValue();
                
                // Crear el pedido según especificaciones académicas
                String numeroPedido = pedidoServicio.crearPedido(
                    cliente.getCodigo(),       // Cliente que hizo el pedido
                    direccionEnvio            // Dirección de envío
                );
                
                if (numeroPedido != null) {
                    // Agregar el detalle del pedido (artículo y cantidad)
                    boolean detalleAgregado = pedidoServicio.agregarDetallePedido(
                        numeroPedido,               // Número del pedido
                        articuloSeleccionado.getCodigo(), // Código del artículo
                        cantidadOrdenada           // Cantidad Ordenada
                    );
                    
                    if (detalleAgregado) {
                        JOptionPane.showMessageDialog(dialog, 
                            "Pedido creado exitosamente\nNúmero: " + numeroPedido +
                            "\nArtículo: " + articuloSeleccionado.getNombre() +
                            "\nCantidad: " + cantidadOrdenada +
                            "\nDirección: " + direccionEnvio);
                        dialog.dispose();
                        
                        // Actualizar la pestaña de pedidos
                        actualizarTablaPedidos();
                        tabbedPane.setSelectedIndex(1); // Cambiar a "Mis Pedidos"
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Error al agregar el artículo al pedido");
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error al crear el pedido");
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });
        panel.add(crearBtn, gbc);
        
        gbc.gridx = 1;
        JButton cancelarBtn = new JButton("Cancelar");
        
        // Configurar botón cancelar con máximo contraste FORZADO
        configurarBotonConMaximoContraste(cancelarBtn, new Color(220, 20, 60), Color.WHITE);
        cancelarBtn.addActionListener(e -> dialog.dispose());
        panel.add(cancelarBtn, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Método helper para configurar botones con máximo contraste FORZADO
     */
    private void configurarBotonConMaximoContraste(JButton button, Color backgroundColor, Color textColor) {
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFont(new Font("Arial", Font.BOLD, 13));
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
     * Método para agregar artículo al carrito (crear pedido rápido)
     */
    public void agregarArticuloAlCarrito(String codigoArticulo, String nombreArticulo, int cantidad) {
        // Mostrar diálogo de confirmación con selección/creación de dirección
        JDialog dialog = new JDialog(this, "Agregar al Carrito", true);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Información del artículo
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("<html><b>Artículo:</b> " + nombreArticulo + 
                                     "<br><b>Cantidad:</b> " + cantidad + "</html>");
        panel.add(infoLabel, gbc);
        
        // Selección de dirección
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Dirección de envío:"), gbc);
        
        JComboBox<String> direccionCombo = new JComboBox<>();
        List<String> direcciones = cliente.getDireccionesEnvio();
        
        if (direcciones.isEmpty()) {
            direccionCombo.addItem("No hay direcciones registradas");
        } else {
            for (String direccion : direcciones) {
                direccionCombo.addItem(direccion);
            }
        }
        direccionCombo.addItem("+ Agregar nueva dirección...");
        
        gbc.gridx = 1;
        panel.add(direccionCombo, gbc);
        
        // Campo para nueva dirección
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Nueva dirección:"), gbc);
        
        JTextField nuevaDireccionField = new JTextField(20);
        nuevaDireccionField.setEnabled(false);
        gbc.gridx = 1;
        panel.add(nuevaDireccionField, gbc);
        
        // Habilitar campo nueva dirección cuando se selecciona
        direccionCombo.addActionListener(e -> {
            boolean esNueva = "+ Agregar nueva dirección...".equals(direccionCombo.getSelectedItem());
            nuevaDireccionField.setEnabled(esNueva);
            if (esNueva) {
                nuevaDireccionField.requestFocus();
            }
        });
        
        // Botones
        gbc.gridx = 0; gbc.gridy = 3;
        JButton confirmarBtn = new JButton("Confirmar Pedido");
        configurarBotonConMaximoContraste(confirmarBtn, new Color(25, 25, 112), Color.WHITE);
        
        confirmarBtn.addActionListener(e -> {
            String direccionEnvio = null;
            
            // Determinar la dirección de envío
            if ("+ Agregar nueva dirección...".equals(direccionCombo.getSelectedItem())) {
                String nuevaDireccion = nuevaDireccionField.getText().trim();
                if (nuevaDireccion.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Debe ingresar una dirección de envío");
                    return;
                }
                // Agregar la nueva dirección al cliente
                cliente.agregarDireccionEnvio(nuevaDireccion);
                direccionEnvio = nuevaDireccion;
                
                // Mensaje informativo
                JOptionPane.showMessageDialog(dialog, 
                    "Nueva dirección agregada a su perfil:\n" + nuevaDireccion, 
                    "Dirección Guardada", JOptionPane.INFORMATION_MESSAGE);
                    
            } else if ("No hay direcciones registradas".equals(direccionCombo.getSelectedItem())) {
                JOptionPane.showMessageDialog(dialog, 
                    "Debe agregar una dirección de envío para continuar");
                return;
            } else {
                direccionEnvio = (String) direccionCombo.getSelectedItem();
            }
            
            // Crear el pedido
            String numeroPedido = pedidoServicio.crearPedido(cliente.getCodigo(), direccionEnvio);
            
            if (numeroPedido != null) {
                // Agregar el artículo al pedido
                boolean detalleAgregado = pedidoServicio.agregarDetallePedido(numeroPedido, codigoArticulo, cantidad);
                
                if (detalleAgregado) {
                    JOptionPane.showMessageDialog(dialog, 
                        "¡Pedido creado exitosamente!\n" +
                        "Número: " + numeroPedido + "\n" +
                        "Artículo: " + nombreArticulo + "\n" +
                        "Cantidad: " + cantidad);
                    dialog.dispose();
                    
                    // Actualizar la pestaña de pedidos
                    actualizarTablaPedidos();
                    tabbedPane.setSelectedIndex(1); // Ir a "Mis Pedidos"
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error al agregar el artículo al pedido");
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al crear el pedido");
            }
        });
        panel.add(confirmarBtn, gbc);
        
        gbc.gridx = 1;
        JButton cancelarBtn = new JButton("Cancelar");
        configurarBotonConMaximoContraste(cancelarBtn, new Color(220, 20, 60), Color.WHITE);
        cancelarBtn.addActionListener(e -> dialog.dispose());
        panel.add(cancelarBtn, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Método para mostrar detalles de un pedido
     */
    public void mostrarDetallesPedido(String numeroPedido) {
        Pedido pedido = pedidoServicio.buscarPedido(numeroPedido);
        if (pedido == null) {
            JOptionPane.showMessageDialog(this, "Pedido no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog(this, "Detalles del Pedido: " + numeroPedido, true);
        dialog.setSize(600, 400);
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
        infoPanel.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(pedido.getFechaPedido().toString()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Dirección de Envío:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(pedido.getDireccionEnvio()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(pedido.getEstado().toString()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
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
        
        // Botón cerrar
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton cerrarBtn = new JButton("Cerrar");
        configurarBotonConMaximoContraste(cerrarBtn, new Color(70, 130, 180), Color.WHITE);
        cerrarBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cerrarBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Método para mostrar diálogo de agregar nueva dirección
     */
    private void mostrarDialogoAgregarDireccion(DefaultListModel<String> listModel) {
        JDialog dialog = new JDialog(this, "Agregar Nueva Dirección", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Campo para la nueva dirección
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nueva Dirección:"), gbc);
        
        gbc.gridx = 1;
        JTextField direccionField = new JTextField(25);
        panel.add(direccionField, gbc);
        
        // Botones
        gbc.gridx = 0; gbc.gridy = 1;
        JButton guardarBtn = new JButton("Guardar");
        configurarBotonConMaximoContraste(guardarBtn, new Color(25, 25, 112), Color.WHITE);
        
        guardarBtn.addActionListener(e -> {
            String nuevaDireccion = direccionField.getText().trim();
            if (!nuevaDireccion.isEmpty()) {
                // Agregar al cliente
                cliente.agregarDireccionEnvio(nuevaDireccion);
                // Actualizar la lista visual
                listModel.addElement(nuevaDireccion);
                JOptionPane.showMessageDialog(dialog, "Dirección agregada exitosamente");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "La dirección no puede estar vacía", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(guardarBtn, gbc);
        
        gbc.gridx = 1;
        JButton cancelarBtn = new JButton("Cancelar");
        configurarBotonConMaximoContraste(cancelarBtn, new Color(220, 20, 60), Color.WHITE);
        cancelarBtn.addActionListener(e -> dialog.dispose());
        panel.add(cancelarBtn, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Método para mostrar diálogo de editar dirección existente
     */
    private void mostrarDialogoEditarDireccion(String direccionActual, int index, DefaultListModel<String> listModel) {
        JDialog dialog = new JDialog(this, "Editar Dirección", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Campo para la dirección
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Editar Dirección:"), gbc);
        
        gbc.gridx = 1;
        JTextField direccionField = new JTextField(direccionActual, 25);
        panel.add(direccionField, gbc);
        
        // Botones
        gbc.gridx = 0; gbc.gridy = 1;
        JButton guardarBtn = new JButton("Guardar Cambios");
        configurarBotonConMaximoContraste(guardarBtn, new Color(25, 25, 112), Color.WHITE);
        
        guardarBtn.addActionListener(e -> {
            String direccionEditada = direccionField.getText().trim();
            if (!direccionEditada.isEmpty()) {
                // Actualizar en el cliente
                cliente.getDireccionesEnvio().set(index, direccionEditada);
                // Actualizar la lista visual
                listModel.setElementAt(direccionEditada, index);
                JOptionPane.showMessageDialog(dialog, "Dirección actualizada exitosamente");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "La dirección no puede estar vacía", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(guardarBtn, gbc);
        
        gbc.gridx = 1;
        JButton cancelarBtn = new JButton("Cancelar");
        configurarBotonConMaximoContraste(cancelarBtn, new Color(220, 20, 60), Color.WHITE);
        cancelarBtn.addActionListener(e -> dialog.dispose());
        panel.add(cancelarBtn, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Método para actualizar la tabla de pedidos
     */
    private void actualizarTablaPedidos() {
        // Recrear completamente el panel de pedidos para que se vea la actualización
        Component pedidosPanel = tabbedPane.getComponentAt(1); // Pestaña "Mis Pedidos"
        if (pedidosPanel instanceof JPanel) {
            // Crear un nuevo panel actualizado
            JPanel nuevoPanelPedidos = createPedidosPanel();
            // Reemplazar el panel actual
            tabbedPane.setComponentAt(1, nuevoPanelPedidos);
            // Forzar repaint para asegurar que se actualice visualmente
            tabbedPane.revalidate();
            tabbedPane.repaint();
        }
    }
}

/**
 * Renderer para mostrar botones en celdas de tabla
 */
class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
    public ButtonRenderer() {
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
 * Editor para hacer funcionales los botones en celdas de tabla
 */
class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private VentanaCliente ventanaCliente;
    
    public ButtonEditor(JCheckBox checkBox, JTable table, VentanaCliente ventanaCliente) {
        super(checkBox);
        this.table = table;
        this.ventanaCliente = ventanaCliente;
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
            // Obtener datos de la fila seleccionada
            int row = table.getSelectedRow();
            if (row >= 0) {
                String codigoArticulo = (String) table.getValueAt(row, 0);
                String nombreArticulo = (String) table.getValueAt(row, 1);
                Object cantidadObj = table.getValueAt(row, 5);
                
                // Validar y convertir cantidad
                int cantidad = 1; // Default
                if (cantidadObj instanceof Integer) {
                    cantidad = (Integer) cantidadObj;
                } else if (cantidadObj instanceof String) {
                    try {
                        cantidad = Integer.parseInt((String) cantidadObj);
                    } catch (NumberFormatException e) {
                        cantidad = 1;
                    }
                }
                
                // Validar que la cantidad sea válida
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(null, 
                        "La cantidad debe ser mayor a 0", 
                        "Cantidad inválida", JOptionPane.WARNING_MESSAGE);
                    return label;
                }
                
                // Llamar al método para agregar al carrito con la cantidad seleccionada
                ventanaCliente.agregarArticuloAlCarrito(codigoArticulo, nombreArticulo, cantidad);
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
 * Editor para botones en la tabla de pedidos
 */
class PedidoButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private VentanaCliente ventanaCliente;
    
    public PedidoButtonEditor(JCheckBox checkBox, JTable table, VentanaCliente ventanaCliente) {
        super(checkBox);
        this.table = table;
        this.ventanaCliente = ventanaCliente;
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
            // Obtener el número de pedido de la fila seleccionada
            int row = table.getSelectedRow();
            if (row >= 0) {
                String numeroPedido = (String) table.getValueAt(row, 0);
                // Mostrar detalles del pedido
                ventanaCliente.mostrarDetallesPedido(numeroPedido);
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
