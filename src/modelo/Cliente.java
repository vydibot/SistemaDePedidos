package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un Cliente en el sistema de pedidos
 */
public class Cliente {
    private String codigo;
    private String nombre;
    private List<String> direccionesEnvio;
    private double saldo;
    private double limiteCredito;
    private double porcentajeDescuento;
    private List<Pedido> pedidos;
    
    // Constructor
    public Cliente(String codigo, String nombre, double limiteCredito, double porcentajeDescuento) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccionesEnvio = new ArrayList<>();
        this.saldo = 0.0;
        this.limiteCredito = limiteCredito;
        this.porcentajeDescuento = porcentajeDescuento;
        this.pedidos = new ArrayList<>();
    }
    
    // Métodos de negocio
    public boolean puedeRealizarPedido(double montopedido) {
        return (saldo + montopedido) <= limiteCredito;
    }
    
    public void agregarDireccionEnvio(String direccion) {
        if (!direccionesEnvio.contains(direccion)) {
            direccionesEnvio.add(direccion);
        }
    }
    
    public void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }
    
    public double calcularDescuento(double monto) {
        return monto * (porcentajeDescuento / 100.0);
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
    
    public List<String> getDireccionesEnvio() {
        return new ArrayList<>(direccionesEnvio);
    }
    
    public void setDireccionesEnvio(List<String> direccionesEnvio) {
        this.direccionesEnvio = new ArrayList<>(direccionesEnvio);
    }
    
    public double getSaldo() {
        return saldo;
    }
    
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    
    public double getLimiteCredito() {
        return limiteCredito;
    }
    
    public void setLimiteCredito(double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }
    
    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }
    
    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }
    
    public List<Pedido> getPedidos() {
        return new ArrayList<>(pedidos);
    }
    
    @Override
    public String toString() {
        return "Cliente{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", saldo=" + saldo +
                ", limiteCredito=" + limiteCredito +
                ", porcentajeDescuento=" + porcentajeDescuento +
                '}';
    }
}
