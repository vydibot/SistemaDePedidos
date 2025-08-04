package modelo;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase que representa un Artículo en el sistema de pedidos
 */
public class Articulo {
    private String codigo;
    private String nombre;
    private String descripcion;
    private Map<String, Integer> cantidadPorPlanta; // Planta -> Cantidad
    private Map<String, Integer> stockMinimoPorPlanta; // Planta -> Stock mínimo
    private double precio;
    
    // Constructor
    public Articulo(String codigo, String nombre, String descripcion, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidadPorPlanta = new HashMap<>();
        this.stockMinimoPorPlanta = new HashMap<>();
    }
    
    // Métodos de negocio
    public void agregarPlanta(String nombrePlanta, int cantidadInicial, int stockMinimo) {
        cantidadPorPlanta.put(nombrePlanta, cantidadInicial);
        stockMinimoPorPlanta.put(nombrePlanta, stockMinimo);
    }
    
    public boolean hayDisponibilidad(int cantidadSolicitada) {
        int totalDisponible = cantidadPorPlanta.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        return totalDisponible >= cantidadSolicitada;
    }
    
    public boolean necesitaReabastecimiento(String planta) {
        Integer cantidad = cantidadPorPlanta.get(planta);
        Integer stockMinimo = stockMinimoPorPlanta.get(planta);
        
        if (cantidad == null || stockMinimo == null) {
            return false;
        }
        
        // Solo considera bajo stock cuando está POR DEBAJO del mínimo
        return cantidad < stockMinimo;
    }
    
    public void reducirStock(String planta, int cantidad) {
        Integer stockActual = cantidadPorPlanta.get(planta);
        if (stockActual != null && stockActual >= cantidad) {
            cantidadPorPlanta.put(planta, stockActual - cantidad);
        }
    }
    
    public void aumentarStock(String planta, int cantidad) {
        cantidadPorPlanta.put(planta, 
            cantidadPorPlanta.getOrDefault(planta, 0) + cantidad);
    }
    
    public void establecerStock(String planta, int cantidad) {
        if (cantidad >= 0) {
            cantidadPorPlanta.put(planta, cantidad);
        }
    }
    
    public int getTotalStock() {
        return cantidadPorPlanta.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
    
    /**
     * Calcula cuántas unidades faltan para alcanzar el stock mínimo en una planta específica
     */
    public int getUnidadesFaltantesParaMinimo(String planta) {
        if (!cantidadPorPlanta.containsKey(planta) || !stockMinimoPorPlanta.containsKey(planta)) {
            return 0;
        }
        
        int stockActual = cantidadPorPlanta.get(planta);
        int stockMinimo = stockMinimoPorPlanta.get(planta);
        
        // Si el stock actual es menor que el mínimo, calcular la diferencia
        return Math.max(0, stockMinimo - stockActual + 1); // +1 para estar por encima del mínimo
    }
    
    /**
     * Calcula cuántas unidades faltan total para que el artículo no esté en bajo stock
     */
    public int getTotalUnidadesFaltantesParaMinimo() {
        int totalFaltante = 0;
        
        for (String planta : cantidadPorPlanta.keySet()) {
            totalFaltante += getUnidadesFaltantesParaMinimo(planta);
        }
        
        return totalFaltante;
    }
    
    /**
     * Obtiene un reporte detallado del estado de stock por planta
     */
    public String getReporteEstadoStock() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE DE STOCK: ").append(codigo).append(" - ").append(nombre).append(" ===\n");
        
        for (String planta : cantidadPorPlanta.keySet()) {
            int stockActual = cantidadPorPlanta.get(planta);
            int stockMinimo = stockMinimoPorPlanta.get(planta);
            int faltante = getUnidadesFaltantesParaMinimo(planta);
            
            String estado = stockActual >= stockMinimo ? "✅ OK" : "❌ BAJO";
            
            reporte.append(String.format("📍 %s: Stock=%d, Mínimo=%d %s", 
                planta, stockActual, stockMinimo, estado));
            
            if (faltante > 0) {
                reporte.append(String.format(" → Faltan %d unidades", faltante));
            }
            reporte.append("\n");
        }
        
        int totalFaltante = getTotalUnidadesFaltantesParaMinimo();
        if (totalFaltante > 0) {
            reporte.append(String.format("\n🔥 TOTAL FALTAN: %d unidades para cumplir mínimos\n", totalFaltante));
        } else {
            reporte.append("\n✅ STOCK ADECUADO en todas las plantas\n");
        }
        
        return reporte.toString();
    }
    
    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Map<String, Integer> getCantidadPorPlanta() {
        return new HashMap<>(cantidadPorPlanta);
    }
    
    public Map<String, Integer> getStockMinimoPorPlanta() {
        return new HashMap<>(stockMinimoPorPlanta);
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    @Override
    public String toString() {
        return "Articulo{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", totalStock=" + getTotalStock() +
                '}';
    }
}
