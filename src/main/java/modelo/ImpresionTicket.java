package modelo;

import br.com.adilson.util.Extenso;
import br.com.adilson.util.PrinterMatrix;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JOptionPane;

public class ImpresionTicket {

    public void imprimirTicket(Venta venta, List<DetalleVenta> productos, Usuario vendedor) {
        
        // Calcular altura dinámica como antes
        int alturaBase = 11;
        int alturaProductos = productos.size();
        int alturaTotales = venta.getDescuentoDinero() > 0 ? 6 : 5;
        int alturaPie = 3;
        int alturaTicket = alturaBase + alturaProductos + alturaTotales + alturaPie;
        int anchoTicket = 32;

        PrinterMatrix printer = new PrinterMatrix();
        printer.setOutSize(alturaTicket, anchoTicket);

        // --- ENCABEZADO ---
        // Usamos printTextWrap como en el código que funcionaba
        printer.printTextWrap(0, 1, 0, anchoTicket, "================================");
        printer.printTextWrap(1, 1, 0, anchoTicket, "      MADERERIA TISCARENO"); 
        printer.printTextWrap(2, 1, 0, anchoTicket, "La mejor calidad al mejor precio");
        printer.printTextWrap(3, 1, 0, anchoTicket, "--------------------------------");
        printer.printTextWrap(4, 1, 0, anchoTicket, "Folio: #" + venta.getFolioTicket());
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        printer.printTextWrap(5, 1, 0, anchoTicket, "Fecha: " + dtf.format(LocalDateTime.now()));
        printer.printTextWrap(6, 1, 0, anchoTicket, "--------------------------------");
        printer.printTextWrap(7, 1, 0, anchoTicket, "CANT  DESCRIPCION       SUBTOTAL");
        printer.printTextWrap(8, 1, 0, anchoTicket, "--------------------------------");

        // --- PRODUCTOS ---
        int fila = 9;
        for (DetalleVenta item : productos) {
            // Formatear la línea completa manualmente
            String cantidad = String.format("%-3d", item.getCantidad());
            String descripcion = item.getNombre() + " " + item.getMedida()+" "+item.getGrosor()+" "+item.getClase();
            if (descripcion.length() > 20) descripcion = descripcion.substring(0, 20);
            descripcion = String.format("%-19s", descripcion);
            String precio = String.format("$%.0f", item.getSubtotal());
            
            // Imprimir toda la línea como una sola cadena
            String lineaProducto = cantidad + " " + descripcion + " " + precio;
            printer.printTextWrap(fila, 1, 0, anchoTicket, lineaProducto);
            
            fila++;
        }

        // --- TOTALES ---
        printer.printTextWrap(fila, 1, 0, anchoTicket, "--------------------------------");
        fila++;
        
        if (venta.getDescuentoDinero() > 0) {
            printer.printTextWrap(fila, 1, 0, anchoTicket, 
                "Subtotal:       $" + String.format("%.2f", venta.getSubtotal()));
            fila++;
            printer.printTextWrap(fila, 1, 0, anchoTicket, 
                "Descuento:     -$" + String.format("%.2f", venta.getDescuentoDinero()));
            fila++;
        }

        printer.printTextWrap(fila, 1, 0, anchoTicket, 
            "TOTAL:          $" + String.format("%.2f", venta.getTotal()));
        fila++;
        printer.printTextWrap(fila, 1, 0, anchoTicket, 
            "EFECTIVO:       $" + String.format("%.2f", venta.getPagoEfectivo()));
        fila++;
        printer.printTextWrap(fila, 1, 0, anchoTicket, 
            "TARJETA:        $" + String.format("%.2f", venta.getPagoTarjeta()));
        fila++;
        
        // --- PIE DE PÁGINA ---
        printer.printTextWrap(fila, 1, 0, anchoTicket, "================================");
        fila++;
        printer.printTextWrap(fila, 1, 0, anchoTicket, "    GRACIAS POR SU COMPRA");

        // --- MANDAR A IMPRIMIR ---
        String rutaTicket = System.getProperty("java.io.tmpdir") + "ticket.txt";
        printer.toFile(rutaTicket);

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(rutaTicket);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar archivo de impresión");
            return;
        }

        DocFlavor docFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc document = new SimpleDoc(inputStream, docFormat, null);
        PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
        PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();

        if (defaultPrintService != null) {
            DocPrintJob printJob = defaultPrintService.createPrintJob();
            try {
                printJob.print(document, attributeSet);
            } catch (PrintException ex) {
                System.out.println("Error imprimiendo: " + ex.toString());
                ex.printStackTrace();
            }
        } else {
            System.err.println("No hay una impresora predeterminada instalada");
            JOptionPane.showMessageDialog(null, "No se encontró impresora predeterminada.");
        }
    }
}