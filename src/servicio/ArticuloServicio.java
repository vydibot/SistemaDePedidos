package servicio;

import modelo.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar artículos
 */
public class ArticuloServicio {
    private Map<String, Articulo> articulos;
    
    public ArticuloServicio() {
        this.articulos = new HashMap<>();
    }
    
    // Operaciones CRUD para artículos
    public boolean agregarArticulo(Articulo articulo) {
        if (articulos.containsKey(articulo.getCodigo())) {
            return false; // Artículo ya existe
        }
        articulos.put(articulo.getCodigo(), articulo);
        return true;
    }
    
    public Articulo buscarArticulo(String codigo) {
        return articulos.get(codigo);
    }
    
    public List<Articulo> listarArticulos() {
        return new ArrayList<>(articulos.values());
    }
    
    public boolean actualizarArticulo(Articulo articulo) {
        if (!articulos.containsKey(articulo.getCodigo())) {
            return false; // Artículo no existe
        }
        articulos.put(articulo.getCodigo(), articulo);
        return true;
    }
    
    public boolean eliminarArticulo(String codigo) {
        return articulos.remove(codigo) != null;
    }
    
    // Métodos de consulta específicos
    public List<Articulo> buscarArticulosPorNombre(String nombre) {
        return articulos.values().stream()
                .filter(articulo -> articulo.getNombre().toLowerCase()
                        .contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Articulo> getArticulosConBajoStock() {
        List<Articulo> articulosBajoStock = new ArrayList<>();
        
        for (Articulo articulo : articulos.values()) {
            boolean tieneBajoStock = false;
            
            // Verificar cada planta del artículo
            for (String planta : articulo.getCantidadPorPlanta().keySet()) {
                if (articulo.necesitaReabastecimiento(planta)) {
                    tieneBajoStock = true;
                    break; // Si al menos una planta tiene bajo stock, el artículo se considera bajo stock
                }
            }
            
            if (tieneBajoStock) {
                articulosBajoStock.add(articulo);
            }
        }
        
        return articulosBajoStock;
    }
    
    /**
     * Método de debugging para mostrar el estado del stock
     */
    public void mostrarEstadoStock() {
        System.out.println("\n=== ESTADO DEL STOCK ===");
        for (Articulo articulo : articulos.values()) {
            System.out.println("\n" + articulo.getCodigo() + " - " + articulo.getNombre());
            
            boolean tieneBajoStock = false;
            for (String planta : articulo.getCantidadPorPlanta().keySet()) {
                int stock = articulo.getCantidadPorPlanta().get(planta);
                int minimo = articulo.getStockMinimoPorPlanta().getOrDefault(planta, 0);
                boolean bajStock = articulo.necesitaReabastecimiento(planta);
                
                System.out.printf("  %s: Stock=%d, Mínimo=%d, BajoStock=%s%n", 
                    planta, stock, minimo, bajStock ? "SÍ" : "NO");
                
                if (bajStock) tieneBajoStock = true;
            }
            
            System.out.println("  RESULTADO: " + (tieneBajoStock ? "BAJO STOCK" : "STOCK OK"));
        }
        System.out.println("========================\n");
    }
    
    public List<Articulo> getArticulosDisponibles() {
        return articulos.values().stream()
                .filter(articulo -> articulo.getTotalStock() > 0)
                .collect(Collectors.toList());
    }
    
    public List<Articulo> getArticulosPorPlanta(String nombrePlanta) {
        return articulos.values().stream()
                .filter(articulo -> articulo.getCantidadPorPlanta().containsKey(nombrePlanta))
                .collect(Collectors.toList());
    }
    
    public boolean verificarDisponibilidad(String codigoArticulo, int cantidadSolicitada) {
        Articulo articulo = buscarArticulo(codigoArticulo);
        if (articulo == null) return false;
        
        return articulo.hayDisponibilidad(cantidadSolicitada);
    }
    
    public void actualizarStock(String codigoArticulo, String planta, int cantidad) {
        Articulo articulo = buscarArticulo(codigoArticulo);
        if (articulo != null) {
            if (cantidad > 0) {
                articulo.aumentarStock(planta, cantidad);
            } else {
                articulo.reducirStock(planta, Math.abs(cantidad));
            }
        }
    }
    
    public void establecerStock(String codigoArticulo, String planta, int cantidad) {
        Articulo articulo = buscarArticulo(codigoArticulo);
        if (articulo != null) {
            articulo.establecerStock(planta, cantidad);
        }
    }
    
    public Map<String, Integer> getResumenStockPorPlanta() {
        Map<String, Integer> resumen = new HashMap<>();
        
        for (Articulo articulo : articulos.values()) {
            for (Map.Entry<String, Integer> entry : articulo.getCantidadPorPlanta().entrySet()) {
                String planta = entry.getKey();
                Integer cantidad = entry.getValue();
                resumen.put(planta, resumen.getOrDefault(planta, 0) + cantidad);
            }
        }
        
        return resumen;
    }
    
    public int getCantidadArticulos() {
        return articulos.size();
    }
    
    /**
     * Obtiene un reporte detallado de cuánto stock falta para un artículo específico
     */
    public String getReporteStockFaltante(String codigoArticulo) {
        Articulo articulo = buscarArticulo(codigoArticulo);
        if (articulo == null) {
            return "❌ Artículo no encontrado: " + codigoArticulo;
        }
        
        return articulo.getReporteEstadoStock();
    }
    
    /**
     * Calcula exactamente cuánto stock falta para que un artículo no esté en bajo stock
     */
    public int getStockFaltanteParaMinimo(String codigoArticulo) {
        Articulo articulo = buscarArticulo(codigoArticulo);
        if (articulo == null) {
            return 0;
        }
        
        return articulo.getTotalUnidadesFaltantesParaMinimo();
    }
    
    /**
     * Obtiene un reporte completo de todos los artículos con bajo stock
     * y cuánto necesitan para salir de bajo stock
     */
    public String getReporteCompletoStockBajo() {
        List<Articulo> articulosBajoStock = getArticulosConBajoStock();
        
        if (articulosBajoStock.isEmpty()) {
            return "✅ TODOS LOS ARTÍCULOS TIENEN STOCK ADECUADO\n";
        }
        
        StringBuilder reporte = new StringBuilder();
        reporte.append("🔥 REPORTE DE ARTÍCULOS CON BAJO STOCK\n");
        reporte.append("==========================================\n\n");
        
        for (Articulo articulo : articulosBajoStock) {
            reporte.append(articulo.getReporteEstadoStock());
            reporte.append("\n");
        }
        
        return reporte.toString();
    }
    
    // Método para validar datos del artículo
    public boolean validarArticulo(Articulo articulo) {
        if (articulo == null) return false;
        if (articulo.getCodigo() == null || articulo.getCodigo().trim().isEmpty()) return false;
        if (articulo.getNombre() == null || articulo.getNombre().trim().isEmpty()) return false;
        if (articulo.getPrecio() < 0) return false;
        
        return true;
    }
}
