package dtos;

public class Paleta {
    private int x, y; // Posición de la esquina superior izquierda
    private int ancho, alto; // Dimensiones de la paleta
    private int dx; // Velocidad horizontal de la paleta

    public Paleta(int x, int y, int ancho, int alto, int dx) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.dx = dx;
    }

    // Getters y setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

      
    public void mover(int anchoPanel) {
        x += dx;

        // Mantener la paleta dentro de los límites del panel
        if (x < 0) {
            x = 0;
        } else if (x + ancho > anchoPanel) {
            x = anchoPanel - ancho;
        }
    }
}
