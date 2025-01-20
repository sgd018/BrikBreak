package servicios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dtos.*;

public class PanelJuego extends JPanel implements ActionListener {
    private Pelota pelota;
    private Paleta paleta;
    private Ladrillo[] ladrillos;
    private Timer timer;
    private int puntuacion = 0;
    private int vidas = 3;

    public PanelJuego() {
        setPreferredSize(new Dimension(400, 600)); // Tamaño del panel
        setBackground(Color.BLACK); // Fondo negro

        // Inicializar objetos del juego
        pelota = new Pelota(200, 300, 10, 10, 2, 2); // Posición inicial y velocidad de la pelota
        paleta = new Paleta(150, 550, 100, 10, 0); // Posición inicial y tamaño de la paleta
        ladrillos = new Ladrillo[20]; // Inicializar un array de ladrillos

        // Crear los ladrillos (se pueden ajustar posiciones y tamaños)
        for (int i = 0; i < ladrillos.length; i++) {
            ladrillos[i] = new Ladrillo(50 + (i % 5) * 60, 50 + (i / 5) * 30, 50, 20);
        }

        // Configurar el temporizador para actualizar el juego
        timer = new Timer(10, this);
        timer.start();

        // Configurar el KeyListener para mover la paleta
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT) {
                    paleta.setDx(-5); // Mover a la izquierda
                } else if (key == KeyEvent.VK_RIGHT) {
                    paleta.setDx(5); // Mover a la derecha
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                    paleta.setDx(0); // Detener el movimiento
                }
            }
        });

        setFocusable(true); // Asegurar que el panel reciba eventos de teclado
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar pelota
        g.setColor(Color.WHITE);
        g.fillOval(pelota.getX(), pelota.getY(), pelota.getAncho(), pelota.getAlto());

        // Dibujar paleta
        g.setColor(Color.BLUE);
        g.fillRect(paleta.getX(), paleta.getY(), paleta.getAncho(), paleta.getAlto());

        // Dibujar ladrillos
        g.setColor(Color.RED);
        for (Ladrillo ladrillo : ladrillos) {
            if (ladrillo.isVisible()) {
                g.fillRect(ladrillo.getX(), ladrillo.getY(), ladrillo.getAncho(), ladrillo.getAlto());
            }
        }

        // Dibujar puntuación y vidas
        g.setColor(Color.WHITE);
        g.drawString("Puntuación: " + puntuacion, 10, 20);
        g.drawString("Vidas: " + vidas, 300, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Actualizar posición de la paleta
        paleta.mover(getWidth());

        // Actualizar posición de la pelota
        pelota.setX(pelota.getX() + pelota.getDx());
        pelota.setY(pelota.getY() + pelota.getDy());

        // Detectar colisiones
        detectarColisiones();

        // Verificar si se pierde la pelota
        if (pelota.getY() > getHeight()) {
            vidas--;
            puntuacion -= 5; // Penalización
            if (vidas == 0) {
                timer.stop(); // Fin del juego
                JOptionPane.showMessageDialog(this, "Game Over");
            } else {
                reiniciarPelota(); // Reiniciar posición de la pelota
            }
        }

        // Repintar el panel
        repaint();
    }

    private void detectarColisiones() {
        // Colisión con las paredes
        if (pelota.getX() < 0 || pelota.getX() + pelota.getAncho() > getWidth()) {
            pelota.setDx(-pelota.getDx());
        }
        if (pelota.getY() < 0) {
            pelota.setDy(-pelota.getDy());
        }

        // Colisión con la paleta
        if (new Rectangle(paleta.getX(), paleta.getY(), paleta.getAncho(), paleta.getAlto())
                .intersects(new Rectangle(pelota.getX(), pelota.getY(), pelota.getAncho(), pelota.getAlto()))) {
            pelota.setDy(-pelota.getDy());
        }

        // Colisión con los ladrillos
        for (Ladrillo ladrillo : ladrillos) {
            if (ladrillo.isVisible() &&
                    new Rectangle(ladrillo.getX(), ladrillo.getY(), ladrillo.getAncho(), ladrillo.getAlto())
                            .intersects(new Rectangle(pelota.getX(), pelota.getY(), pelota.getAncho(), pelota.getAlto()))) {
                ladrillo.setVisible(false);
                pelota.setDy(-pelota.getDy());
                puntuacion += 10; // Incrementar puntuación
            }
        }
    }

    private void reiniciarPelota() {
        pelota.setX(200);
        pelota.setY(300);
        pelota.setDx(2);
        pelota.setDy(2);
    }
}
