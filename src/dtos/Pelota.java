package dtos;

public class Pelota {
    private int x;
    private int y;
    private int ancho;
    private int alto;
    private int dx;
    private int dy;

    public Pelota(int x, int y, int ancho, int alto, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.dx = dx;
        this.dy = dy;
    }

    // Getters y setters
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getAncho() { return ancho; }
    public void setAncho(int ancho) { this.ancho = ancho; }

    public int getAlto() { return alto; }
    public void setAlto(int alto) { this.alto = alto; }

    public int getDx() { return dx; }
    public void setDx(int dx) { this.dx = dx; }

    public int getDy() { return dy; }
    public void setDy(int dy) { this.dy = dy; }

    // Método para mover la pelota
    public void mover() {
        x += dx;
        y += dy;
    }

    // Métodos para invertir dirección
    public void invertirDireccionX() {
        dx = -dx;
    }

    public void invertirDireccionY() {
        dy = -dy;
    }

    // Método para detectar colisión con otro objeto
    public boolean intersects(Paleta paleta) {
        return x < paleta.getX() + paleta.getAncho() &&
               x + ancho > paleta.getX() &&
               y < paleta.getY() + paleta.getAlto() &&
               y + alto > paleta.getY();
    }

    public boolean intersects(Ladrillo ladrillo) {
        return x < ladrillo.getX() + ladrillo.getAncho() &&
               x + ancho > ladrillo.getX() &&
               y < ladrillo.getY() + ladrillo.getAlto() &&
               y + alto > ladrillo.getY();
    }
}
