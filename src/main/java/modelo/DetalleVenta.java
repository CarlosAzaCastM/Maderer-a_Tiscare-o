package modelo;

public class DetalleVenta {
    private int idVariante;
    private String nombre;
    private String medida;
    private String clase;
    private String grosor;
    private double ftTotal;
    private int cantidad;
    private double precioVenta; 
    private double subtotal;
    private double costoCompra;

    public DetalleVenta(Variante v, int cantidad) {
        this.idVariante = v.getId();
        this.nombre = v.getNombreProducto();
        this.medida = v.getMedida();
        this.clase = v.getClase();
        this.grosor = v.getGrosor();
        
        String[] medidaPartes = this.medida.split("x");
        String part1Medida = medidaPartes[0]; 
        String part2Medida = medidaPartes[1];
        int part1EnteroMedida = Integer.parseInt(part1Medida);
        int part2EnteroMedida = Integer.parseInt(part2Medida);
        
        this.cantidad = cantidad;
        
        if (this.grosor.contains("/") && this.grosor.split("/").length == 2) {

            String[] partes = this.grosor.split("/");
            try {
                float numerador = Float.parseFloat(partes[0]);
                float denominador = Float.parseFloat(partes[1]);

                float grosorFloat = numerador / denominador;   

                this.ftTotal = ((grosorFloat * part1EnteroMedida * part2EnteroMedida) / 12) * this.cantidad;

            } catch (NumberFormatException e) {
                System.out.println("Error: formato de fracción inválido");
            }

        } else {
            // No es fracción → número normal
            float grosorFloat = Float.parseFloat(this.grosor);
            this.ftTotal = ((grosorFloat * part1EnteroMedida * part2EnteroMedida) / 12) * this.cantidad;
        }
        
        this.costoCompra = v.getCostoCompra();
        this.precioVenta = v.getPrecioVenta(); // Precio sugerido por defecto
        this.subtotal = cantidad * v.getPrecioVenta();
    }
    
    
    // Getters y Setters...
    // IMPORTANTE: Al hacer setPrecioVenta o setCantidad, recalcula el subtotal.
    public void setPrecioVenta(double precio) {
        this.precioVenta = precio;
        this.subtotal = this.cantidad * precio;
    }
    
    public double getPrecioVenta(){
        return this.precioVenta;
    }

    public double getCostoCompra() {
        return costoCompra;
    }

    public void setCostoCompra(double costoCompra) {
        this.costoCompra = costoCompra;
    }
    
    

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getGrosor() {
        return grosor;
    }

    public void setGrosor(String grosor) {
        this.grosor = grosor;
    }

    public double getFtTotal() {
        return ftTotal;
    }

    public void setFtTotal(double ftTotal) {
        this.ftTotal = ftTotal;
    }
    
    

    public int getIdVariante() {
        return idVariante;
    }

    public void setIdVariante(int idVariante) {
        this.idVariante = idVariante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = cantidad * this.precioVenta;
        
        String[] medidaPartes = this.medida.split("x");
        String part1Medida = medidaPartes[0]; 
        String part2Medida = medidaPartes[1];
        int part1EnteroMedida = Integer.parseInt(part1Medida);
        int part2EnteroMedida = Integer.parseInt(part2Medida);
        
        if (this.grosor.contains("/") && this.grosor.split("/").length == 2) {

            String[] partes = this.grosor.split("/");
            try {
                float numerador = Float.parseFloat(partes[0]);
                float denominador = Float.parseFloat(partes[1]);

                float grosorFloat = numerador / denominador;

                this.ftTotal = ((grosorFloat * part1EnteroMedida * part2EnteroMedida) / 12) * this.cantidad;

            } catch (NumberFormatException e) {
                System.out.println("Error: formato de fracción inválido");
            }

        } else {
            // No es fracción → número normal
            float grosorFloat = Float.parseFloat(this.grosor);
            this.ftTotal = ((grosorFloat * part1EnteroMedida * part2EnteroMedida) / 12) * this.cantidad;
        }
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    
}