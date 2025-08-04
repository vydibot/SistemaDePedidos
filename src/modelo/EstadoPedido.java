package modelo;

/**
 * Enumeración que representa los posibles estados de un pedido
 */
public enum EstadoPedido {
    PENDIENTE("Pendiente"),
    PROCESADO("Procesado"),
    EN_PREPARACION("En Preparación"),
    ENVIADO("Enviado"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado");
    
    private final String descripcion;
    
    EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
