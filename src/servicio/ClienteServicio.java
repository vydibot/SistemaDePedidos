package servicio;

import modelo.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar clientes
 */
public class ClienteServicio {
    private Map<String, Cliente> clientes;
    
    public ClienteServicio() {
        this.clientes = new HashMap<>();
    }
    
    // Operaciones CRUD para clientes
    public boolean agregarCliente(Cliente cliente) {
        if (clientes.containsKey(cliente.getCodigo())) {
            return false; // Cliente ya existe
        }
        clientes.put(cliente.getCodigo(), cliente);
        return true;
    }
    
    public Cliente buscarCliente(String codigo) {
        return clientes.get(codigo);
    }
    
    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes.values());
    }
    
    public boolean actualizarCliente(Cliente cliente) {
        if (!clientes.containsKey(cliente.getCodigo())) {
            return false; // Cliente no existe
        }
        clientes.put(cliente.getCodigo(), cliente);
        return true;
    }
    
    public boolean eliminarCliente(String codigo) {
        return clientes.remove(codigo) != null;
    }
    
    // Métodos de consulta específicos
    public List<Cliente> buscarClientesPorNombre(String nombre) {
        return clientes.values().stream()
                .filter(cliente -> cliente.getNombre().toLowerCase()
                        .contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Cliente> getClientesConSaldoVencido() {
        return clientes.values().stream()
                .filter(cliente -> cliente.getSaldo() > cliente.getLimiteCredito())
                .collect(Collectors.toList());
    }
    
    public List<Cliente> getClientesConDescuento() {
        return clientes.values().stream()
                .filter(cliente -> cliente.getPorcentajeDescuento() > 0)
                .collect(Collectors.toList());
    }
    
    public double getTotalSaldosClientes() {
        return clientes.values().stream()
                .mapToDouble(Cliente::getSaldo)
                .sum();
    }
    
    public int getCantidadClientes() {
        return clientes.size();
    }
    
    // Método para validar datos del cliente
    public boolean validarCliente(Cliente cliente) {
        if (cliente == null) return false;
        if (cliente.getCodigo() == null || cliente.getCodigo().trim().isEmpty()) return false;
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) return false;
        if (cliente.getLimiteCredito() < 0) return false;
        if (cliente.getPorcentajeDescuento() < 0 || cliente.getPorcentajeDescuento() > 100) return false;
        
        return true;
    }
}
