package controlador;

import javax.swing.JFrame;
import servicios.Menu;

public class Inicio {
    public static void main(String[] args) {
        JFrame ventana = new JFrame("Brick Breaker");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(600, 400); // Tamaño inicial del frame
        ventana.setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Crear y agregar el menú inicial
        Menu menu = new Menu(ventana);
        ventana.add(menu);

        ventana.setVisible(true);
    }
}