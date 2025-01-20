package servicios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import dtos.*;

class PanelJuegoNivelFacil extends JPanel implements ActionListener {
    private Pelota pelota;
    private Paleta paleta;
    private ArrayList<Ladrillo> ladrillos;
    private Timer timer;

    private int puntuacion = 0;
    private int vidas = 3;
    private JFrame frame;
    private boolean juegoIniciado = false;

    public PanelJuegoNivelFacil(JFrame frame) {
        this.frame = frame;
        setLayout(null);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setBackground(Color.BLACK);

        pelota = new Pelota(200, 300, 10, 10, 0, 0);
        paleta = new Paleta(350, 550, 100, 10, 0);
        ladrillos = new ArrayList<>();
        inicializarLadrillos();

        timer = new Timer(10, this);
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT) {
                    paleta.setDx(-5);
                } else if (key == KeyEvent.VK_RIGHT) {
                    paleta.setDx(5);
                } else if (key == KeyEvent.VK_SPACE) {
                    iniciarJuego();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                    paleta.setDx(0);
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow(); // Para asegurar que el panel tenga el foco

        JButton btnSalir = new JButton("Salir");
        btnSalir.setBounds(getWidth() - 120, 20, 100, 30);
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salirAlMenu();
            }
        });
        add(btnSalir);
    }

    private void inicializarLadrillos() {
        int numLadrillosPorFila = 5;
        int ladrilloAncho = getWidth() / numLadrillosPorFila;
        for (int i = 0; i < 20; i++) {
            int x = (i % numLadrillosPorFila) * ladrilloAncho;
            int y = (i / numLadrillosPorFila) * 30 + 50;
            ladrillos.add(new Ladrillo(x, y, ladrilloAncho - 10, 20));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        System.out.println("ajksdhfasjhdfajsdfjasdfjsdf");
        g.setColor(Color.WHITE);
        g.fillOval(pelota.getX(), pelota.getY(), pelota.getAncho(), pelota.getAlto());

        g.setColor(Color.BLUE);
        g.fillRect(paleta.getX(), paleta.getY(), paleta.getAncho(), paleta.getAlto());

        g.setColor(Color.RED);
        for (Ladrillo ladrillo : ladrillos) {
            if (ladrillo.isVisible()) {
                g.fillRect(ladrillo.getX(), ladrillo.getY(), ladrillo.getAncho(), ladrillo.getAlto());
            }
        }

        g.setColor(Color.WHITE);
        g.drawString("Puntuación: " + puntuacion, 10, 20);
        g.drawString("Vidas: " + vidas, getWidth() - 100, 20);
    }

    private void iniciarJuego() {
        if (!juegoIniciado) {
            pelota.setDx(2);

            System.out.println("------------ajksdhfasjhdfajsdfjasdfjsdf");
            pelota.setDy(-2);
            juegoIniciado = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (juegoIniciado) {
            paleta.mover(getWidth());
            pelota.mover();

            System.out.println("+++++++++++++++++++ajksdhfasjhdfajsdfjasdfjsdf");
            detectarColisiones();

            if (pelota.getY() > getHeight()) {
                vidas--;
                puntuacion -= 5;
                if (vidas == 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(this, "Game Over");
                    salirAlMenu();
                } else {
                    reiniciarPelota();
                }
            }

            repaint();
        }
    }

    private void detectarColisiones() {
        if (pelota.getX() < 0 || pelota.getX() + pelota.getAncho() > getWidth()) {
            pelota.invertirDireccionX();
        }
        if (pelota.getY() < 0) {
            pelota.invertirDireccionY();
        }

        if (pelota.intersects(paleta)) {
            pelota.invertirDireccionY();
        }

        for (Ladrillo ladrillo : ladrillos) {
            if (ladrillo.isVisible() && pelota.intersects(ladrillo)) {
                ladrillo.setVisible(false);
                pelota.invertirDireccionY();
                puntuacion += 10;
            }
        }
    }

    private void reiniciarPelota() {
        pelota.setX(200);
        pelota.setY(300);
        pelota.setDx(0);
        pelota.setDy(0);
    }

    private void salirAlMenu() {
        timer.stop();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new Menu(frame));
        frame.revalidate();
        frame.repaint();
    }
}
