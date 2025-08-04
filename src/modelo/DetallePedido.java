package modelo;

/**
 * Clase que representa el detalle de un pedido (línea de pedido)
 */
public class DetallePedido {
    private Articulo articulo;
    private int cantidadOrdenada;
    private int cantidadPendiente;
    private double precioUnitario;
    private double subtotal;
    
    // Constructor
    public DetallePedido(Articulo articulo, int cantidadOrdenada) {
        this.articulo = articulo;
        this.cantidadOrdenada = cantidadOrdenada;
        this.cantidadPendiente = cantidadOrdenada; // Inicialmente toda la cantidad está pendiente
        this.precioUnitario = articulo.getPrecio();
        this.subtotal = calcularSubtotal();
    }
    
    // Métodos de negocio
    private double calcularSubtotal() {
        return cantidadOrdenada * precioUnitario;
    }
    
    public void actualizarCantidadPendiente(int cantidadEntregada) {
        if (cantidadEntregada <= cantidadPendiente) {
            cantidadPendiente -= cantidadEntregada;
        }
    }
    
    public boolean estaCompleto() {
        return cantidadPendiente == 0;
    }
    
    public int getCantidadEntregada() {
        return cantidadOrdenada - cantidadPendiente;
    }
    
    public double getPorcentajeCompletado() {
        if (cantidadOrdenada == 0) return 0.0;
        return ((double) getCantidadEntregada() / cantidadOrdenada) * 100.0;
    }
    
    // Getters y Setters
    public Articulo getArticulo() {
        return articulo;
    }
    
    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
        this.precioUnitario = articulo.getPrecio();
        this.subtotal = calcularSubtotal();
    }
    
    public int getCantidadOrdenada() {
        return cantidadOrdenada;
    }
    
    public void setCantidadOrdenada(int cantidadOrdenada) {
        this.cantidadOrdenada = cantidadOrdenada;
        this.subtotal = calcularSubtotal();
    }
    
    public int getCantidadPendiente() {
        return cantidadPendiente;
    }
    
    public void setCantidadPendiente(int cantidadPendiente) {
        this.cantidadPendiente = cantidadPendiente;
    }
    
    public double getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        this.subtotal = calcularSubtotal();
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    @Override
    public String toString() {
        return "DetallePedido{" +
                "articulo=" + articulo.getNombre() +
                ", cantidadOrdenada=" + cantidadOrdenada +
                ", cantidadPendiente=" + cantidadPendiente +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + subtotal +
                '}';
    }
}
