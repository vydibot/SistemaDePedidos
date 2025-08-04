package servicio;

import modelo.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar pedidos
 */
public class PedidoServicio {
    private Map<String, Pedido> pedidos;
    private ClienteServicio clienteServicio;
    private ArticuloServicio articuloServicio;
    private int contadorPedidos;
    
    public PedidoServicio(ClienteServicio clienteServicio, ArticuloServicio articuloServicio) {
        this.pedidos = new HashMap<>();
        this.clienteServicio = clienteServicio;
        this.articuloServicio = articuloServicio;
        this.contadorPedidos = 1;
    }
    
    // Operaciones CRUD para pedidos
    public String crearPedido(String codigoCliente, String direccionEnvio) {
        Cliente cliente = clienteServicio.buscarCliente(codigoCliente);
        if (cliente == null) {
            return null; // Cliente no existe
        }
        
        String numeroPedido = generarNumeroPedido();
        Pedido pedido = new Pedido(numeroPedido, cliente, direccionEnvio);
        
        pedidos.put(numeroPedido, pedido);
        cliente.agregarPedido(pedido);
        
        return numeroPedido;
    }
    
    public boolean agregarDetallePedido(String numeroPedido, String codigoArticulo, int cantidad) {
        Pedido pedido = buscarPedido(numeroPedido);
        Articulo articulo = articuloServicio.buscarArticulo(codigoArticulo);
        
        if (pedido == null || articulo == null || cantidad <= 0) {
            return false;
        }
        
        // Solo permitir agregar detalles si el pedido está pendiente
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            return false;
        }
        
        pedido.agregarDetalle(articulo, cantidad);
        return true;
    }
    
    public Pedido buscarPedido(String numeroPedido) {
        return pedidos.get(numeroPedido);
    }
    
    public List<Pedido> listarPedidos() {
        return new ArrayList<>(pedidos.values());
    }
    
    public boolean procesarPedido(String numeroPedido) {
        Pedido pedido = buscarPedido(numeroPedido);
        if (pedido == null) {
            return false;
        }
        
        if (pedido.puedeSerProcesado()) {
            pedido.procesarPedido();
            return true;
        }
        
        return false;
    }
    
    public boolean cancelarPedido(String numeroPedido) {
        Pedido pedido = buscarPedido(numeroPedido);
        if (pedido == null) {
            return false;
        }
        
        // Solo se puede cancelar si está pendiente o procesado
        if (pedido.getEstado() == EstadoPedido.PENDIENTE || 
            pedido.getEstado() == EstadoPedido.PROCESADO) {
            
            // Si el pedido estaba procesado, devolver el stock
            if (pedido.getEstado() == EstadoPedido.PROCESADO) {
                restaurarStockPedido(pedido);
                // Devolver el saldo al cliente
                pedido.getCliente().setSaldo(pedido.getCliente().getSaldo() - pedido.getMontoTotal());
            }
            
            pedido.setEstado(EstadoPedido.CANCELADO);
            return true;
        }
        
        return false;
    }
    
    /**
     * Restaura el stock cuando se cancela un pedido procesado
     */
    private void restaurarStockPedido(Pedido pedido) {
        for (DetallePedido detalle : pedido.getDetalles()) {
            Articulo articulo = detalle.getArticulo();
            int cantidadEntregada = detalle.getCantidadOrdenada() - detalle.getCantidadPendiente();
            
            if (cantidadEntregada > 0) {
                // Restaurar a la primera planta disponible (simplificado)
                String primerPlanta = articulo.getCantidadPorPlanta().keySet().iterator().next();
                articulo.aumentarStock(primerPlanta, cantidadEntregada);
                // Restaurar cantidad pendiente para que los reportes sean consistentes
                detalle.setCantidadPendiente(detalle.getCantidadOrdenada());
            }
        }
    }
    
    /**
     * Cambia el estado de un pedido manejando las reglas de negocio
     */
    public boolean cambiarEstadoPedido(String numeroPedido, EstadoPedido nuevoEstado) {
        Pedido pedido = buscarPedido(numeroPedido);
        if (pedido == null) {
            return false;
        }
        
        EstadoPedido estadoActual = pedido.getEstado();
        
        // Validar transiciones válidas de estado
        if (!esTransicionValida(estadoActual, nuevoEstado)) {
            return false;
        }
        
        // Manejar casos especiales
        switch (nuevoEstado) {
            case PROCESADO:
                if (estadoActual == EstadoPedido.PENDIENTE) {
                    if (pedido.puedeSerProcesado()) {
                        pedido.procesarPedido();
                        return true;
                    }
                    return false;
                }
                break;
                
            case CANCELADO:
                return cancelarPedido(numeroPedido);
                
            default:
                pedido.setEstado(nuevoEstado);
                return true;
        }
        
        pedido.setEstado(nuevoEstado);
        return true;
    }
    
    /**
     * Valida si una transición de estado es válida
     */
    private boolean esTransicionValida(EstadoPedido estadoActual, EstadoPedido nuevoEstado) {
        // Los pedidos cancelados no pueden cambiar de estado
        if (estadoActual == EstadoPedido.CANCELADO) {
            return false;
        }
        
        // Los pedidos entregados no pueden retroceder
        if (estadoActual == EstadoPedido.ENTREGADO && 
            nuevoEstado != EstadoPedido.ENTREGADO) {
            return false;
        }
        
        return true;
    }
    
    // Métodos de consulta específicos
    public List<Pedido> getPedidosPorCliente(String codigoCliente) {
        return pedidos.values().stream()
                .filter(pedido -> pedido.getCliente().getCodigo().equals(codigoCliente))
                .collect(Collectors.toList());
    }
    
    public List<Pedido> getPedidosPorEstado(EstadoPedido estado) {
        return pedidos.values().stream()
                .filter(pedido -> pedido.getEstado() == estado)
                .collect(Collectors.toList());
    }
    
    public List<Pedido> getPedidosPorFecha(LocalDate fecha) {
        return pedidos.values().stream()
                .filter(pedido -> pedido.getFechaPedido().equals(fecha))
                .collect(Collectors.toList());
    }
    
    public List<Pedido> getPedidosEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return pedidos.values().stream()
                .filter(pedido -> {
                    LocalDate fecha = pedido.getFechaPedido();
                    return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
                })
                .collect(Collectors.toList());
    }
    
    public List<Pedido> getPedidosPendientes() {
        return getPedidosPorEstado(EstadoPedido.PENDIENTE);
    }
    
    public List<Pedido> getPedidosIncompletos() {
        return pedidos.values().stream()
                .filter(pedido -> {
                    // Solo considerar pedidos que están siendo procesados o ya procesados
                    EstadoPedido estado = pedido.getEstado();
                    boolean estadoValido = estado == EstadoPedido.PROCESADO || 
                                         estado == EstadoPedido.EN_PREPARACION || 
                                         estado == EstadoPedido.ENVIADO;
                    
                    // Y que tengan cantidades pendientes
                    return estadoValido && !pedido.estaCompleto();
                })
                .collect(Collectors.toList());
    }
    
    public double getTotalVentasPorFecha(LocalDate fecha) {
        return getPedidosPorFecha(fecha).stream()
                .filter(pedido -> pedido.getEstado() != EstadoPedido.CANCELADO)
                .mapToDouble(Pedido::getMontoTotal)
                .sum();
    }
    
    public double getTotalVentasPorCliente(String codigoCliente) {
        return getPedidosPorCliente(codigoCliente).stream()
                .filter(pedido -> pedido.getEstado() != EstadoPedido.CANCELADO)
                .mapToDouble(Pedido::getMontoTotal)
                .sum();
    }
    
    public Map<EstadoPedido, Long> getEstadisticasPorEstado() {
        return pedidos.values().stream()
                .collect(Collectors.groupingBy(
                    Pedido::getEstado,
                    Collectors.counting()
                ));
    }
    
    public List<Articulo> getArticulosMasPedidos(int limite) {
        Map<Articulo, Integer> conteoArticulos = new HashMap<>();
        
        for (Pedido pedido : pedidos.values()) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                Articulo articulo = detalle.getArticulo();
                conteoArticulos.put(articulo, 
                    conteoArticulos.getOrDefault(articulo, 0) + detalle.getCantidadOrdenada());
            }
        }
        
        return conteoArticulos.entrySet().stream()
                .sorted(Map.Entry.<Articulo, Integer>comparingByValue().reversed())
                .limit(limite)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    
    public int getCantidadPedidos() {
        return pedidos.size();
    }
    
    // Métodos auxiliares
    private String generarNumeroPedido() {
        return String.format("PED-%06d", contadorPedidos++);
    }
    
    public boolean validarPedido(Pedido pedido) {
        if (pedido == null) return false;
        if (pedido.getNumeroPedido() == null || pedido.getNumeroPedido().trim().isEmpty()) return false;
        if (pedido.getCliente() == null) return false;
        if (pedido.getDireccionEnvio() == null || pedido.getDireccionEnvio().trim().isEmpty()) return false;
        
        return true;
    }
}
