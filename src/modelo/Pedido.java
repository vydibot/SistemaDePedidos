package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un Pedido en el sistema
 */
public class Pedido {
    private String numeroPedido;
    private Cliente cliente;
    private String direccionEnvio;
    private LocalDate fechaPedido;
    private List<DetallePedido> detalles;
    private EstadoPedido estado;
    private double montoTotal;
    
    // Constructor
    public Pedido(String numeroPedido, Cliente cliente, String direccionEnvio) {
        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
        this.direccionEnvio = direccionEnvio;
        this.fechaPedido = LocalDate.now();
        this.detalles = new ArrayList<>();
        this.estado = EstadoPedido.PENDIENTE;
        this.montoTotal = 0.0;
    }
    
    // Métodos de negocio
    public void agregarDetalle(Articulo articulo, int cantidadOrdenada) {
        DetallePedido detalle = new DetallePedido(articulo, cantidadOrdenada);
        detalles.add(detalle);
        calcularMontoTotal();
    }
    
    public void calcularMontoTotal() {
        montoTotal = detalles.stream()
                .mapToDouble(detalle -> detalle.getSubtotal())
                .sum();
                
        // Aplicar descuento del cliente
        double descuento = cliente.calcularDescuento(montoTotal);
        montoTotal = montoTotal - descuento;
    }
    
    public boolean puedeSerProcesado() {
        // Verificar límite de crédito
        if (!cliente.puedeRealizarPedido(montoTotal)) {
            return false;
        }
        
        // Verificar disponibilidad de todos los artículos
        for (DetallePedido detalle : detalles) {
            if (!detalle.getArticulo().hayDisponibilidad(detalle.getCantidadOrdenada())) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Obtiene el motivo por el cual un pedido no puede ser procesado
     */
    public String motivoNoProcesable() {
        // Verificar límite de crédito
        if (!cliente.puedeRealizarPedido(montoTotal)) {
            return "El cliente ha excedido su límite de crédito. " +
                   "Saldo actual: $" + String.format("%,.0f", cliente.getSaldo()) + 
                   ", Límite: $" + String.format("%,.0f", cliente.getLimiteCredito()) + 
                   ", Monto pedido: $" + String.format("%,.0f", montoTotal);
        }
        
        // Verificar disponibilidad de artículos
        for (DetallePedido detalle : detalles) {
            Articulo articulo = detalle.getArticulo();
            if (!articulo.hayDisponibilidad(detalle.getCantidadOrdenada())) {
                return "Stock insuficiente para " + articulo.getNombre() + ". " +
                       "Solicitado: " + detalle.getCantidadOrdenada() + 
                       ", Disponible: " + articulo.getTotalStock();
            }
        }
        
        return "El pedido puede ser procesado";
    }
    
    public void procesarPedido() {
        if (puedeSerProcesado()) {
            estado = EstadoPedido.PROCESADO;
            cliente.setSaldo(cliente.getSaldo() + montoTotal);
            
            // Reducir stock de artículos de manera inteligente
            for (DetallePedido detalle : detalles) {
                Articulo articulo = detalle.getArticulo();
                int cantidadPendiente = detalle.getCantidadOrdenada();
                
                // Buscar plantas con suficiente stock primero
                for (String planta : articulo.getCantidadPorPlanta().keySet()) {
                    int stockPlanta = articulo.getCantidadPorPlanta().get(planta);
                    if (stockPlanta >= cantidadPendiente) {
                        articulo.reducirStock(planta, cantidadPendiente);
                        cantidadPendiente = 0;
                        break;
                    }
                }
                
                // Si no se pudo satisfacer completamente, tomar de múltiples plantas
                if (cantidadPendiente > 0) {
                    for (String planta : articulo.getCantidadPorPlanta().keySet()) {
                        int stockPlanta = articulo.getCantidadPorPlanta().get(planta);
                        if (stockPlanta > 0 && cantidadPendiente > 0) {
                            int cantidadTomar = Math.min(stockPlanta, cantidadPendiente);
                            articulo.reducirStock(planta, cantidadTomar);
                            cantidadPendiente -= cantidadTomar;
                        }
                    }
                }
                
                // Actualizar cantidad pendiente (0 si se completó, resto si no)
                detalle.setCantidadPendiente(cantidadPendiente);
            }
        }
    }
    
    public boolean estaCompleto() {
        return detalles.stream().allMatch(detalle -> detalle.getCantidadPendiente() == 0);
    }
    
    // Getters y Setters
    public String getNumeroPedido() {
        return numeroPedido;
    }
    
    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public String getDireccionEnvio() {
        return direccionEnvio;
    }
    
    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }
    
    public LocalDate getFechaPedido() {
        return fechaPedido;
    }
    
    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }
    
    public List<DetallePedido> getDetalles() {
        return new ArrayList<>(detalles);
    }
    
    public EstadoPedido getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }
    
    public double getMontoTotal() {
        return montoTotal;
    }
    
    @Override
    public String toString() {
        return "Pedido{" +
                "numeroPedido='" + numeroPedido + '\'' +
                ", cliente=" + cliente.getNombre() +
                ", fechaPedido=" + fechaPedido +
                ", estado=" + estado +
                ", montoTotal=" + montoTotal +
                '}';
    }
}
