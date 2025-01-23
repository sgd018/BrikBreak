package servicios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import dtos.*;

public class PanelJuegoNivelMedio extends JPanel implements ActionListener {
    private Pelota pelota;
    private Paleta paleta;
    private ArrayList<Ladrillo> ladrillos;
    private Timer timer;
    private int puntuacion = 0;
    private int vidas = 3;
    private JFrame frame;
    private boolean juegoIniciado = false;
    private IJuegoService juegoService;
    private JLabel instruccionLabel;
    private int tiempoJuego = 300000; // 5 minutos en milisegundos
    private int tiempoLadrillos = 15000; // Cada 15 segundos
    private long tiempoInicio;
    private long tiempoUltimosLadrillos;
    private Ladrillo bloqueSuperior;
    private Ladrillo bloqueInferior;

    public PanelJuegoNivelMedio(JFrame frame) {
        this.frame = frame;
        setLayout(null);
        setBackground(Color.BLACK);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        int pelotaX = screenWidth / 2 - 5;
        int pelotaY = screenHeight - 150;
        pelota = new Pelota(pelotaX, pelotaY, 10, 10, 0, 0);

        int paletaX = screenWidth / 2 - 50;
        int paletaY = screenHeight - 100;
        paleta = new Paleta(paletaX, paletaY, 100, 10, 0);

        bloqueSuperior = new Ladrillo(0, 0, screenWidth, 20);
        bloqueInferior = new Ladrillo(0, screenHeight - 20, screenWidth, 20);

        ladrillos = new ArrayList<>();
        inicializarLadrillos();

        this.juegoService = new JuegoServiceImpl(this, "medio", 300, 150);

        configurarBotonSalir(screenWidth);
        configurarInstrucciones(screenWidth, screenHeight);

        timer = new Timer(10, this);
        timer.start();
        tiempoInicio = System.currentTimeMillis();
        tiempoUltimosLadrillos = tiempoInicio;

        configurarControles();
    }

    private void configurarControles() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT) {
                    paleta.setDx(-5);
                    if (!juegoIniciado) iniciarJuego();
                } else if (key == KeyEvent.VK_RIGHT) {
                    paleta.setDx(5);
                    if (!juegoIniciado) iniciarJuego();
                } else if (key == KeyEvent.VK_SPACE && !juegoIniciado) {
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
        requestFocusInWindow();
    }

    private void configurarBotonSalir(int screenWidth) {
        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 16));
        btnSalir.setBackground(Color.WHITE);
        btnSalir.setForeground(Color.BLACK);
        btnSalir.setBounds(screenWidth - 150, 20, 100, 30);
        btnSalir.addActionListener(e -> salirAlMenu());
        btnSalir.setFocusable(false);
        add(btnSalir);
    }
    
    private void configurarInstrucciones(int screenWidth, int screenHeight) {
        instruccionLabel = new JLabel("Presiona ESPACIO o las FLECHAS para comenzar", SwingConstants.CENTER);
        instruccionLabel.setFont(new Font("Arial", Font.BOLD, 24));
        instruccionLabel.setForeground(Color.WHITE);
        instruccionLabel.setBounds(0, screenHeight/2 - 100, screenWidth, 30);
        add(instruccionLabel);
    }

    private void inicializarLadrillos() {
        int numLadrillosPorFila = 5;
        int ladrilloAncho = 80;
        int ladrilloAlto = 20;
        int espacioEntreX = 10;
        int espacioEntreY = 10;
        int margenSuperior = 150;
        int margenIzquierdo = 400;

        for (int i = 0; i < 20; i++) {
            int fila = i / numLadrillosPorFila;
            int columna = i % numLadrillosPorFila;

            int x = margenIzquierdo + columna * (ladrilloAncho + espacioEntreX);
            int y = margenSuperior + fila * (ladrilloAlto + espacioEntreY);

            ladrillos.add(new Ladrillo(x, y, ladrilloAncho, ladrilloAlto));
        }
    }

    public void agregarLadrillos(int cantidad) {
        int numLadrillosPorFila = 5;
        int ladrilloAncho = 80;
        int ladrilloAlto = 20;
        int espacioEntreX = 10;
        int espacioEntreY = 10;
        
        int filaActual = ladrillos.size() / numLadrillosPorFila;
        int margenSuperior = 50 + (filaActual * (ladrilloAlto + espacioEntreY));
        int margenIzquierdo = 200;

        for (int i = 0; i < cantidad; i++) {
            int columna = ladrillos.size() % numLadrillosPorFila;
            int x = margenIzquierdo + columna * (ladrilloAncho + espacioEntreX);
            int y = margenSuperior;
            
            if (columna == 0 && ladrillos.size() > 0) {
                margenSuperior += ladrilloAlto + espacioEntreY;
            }
            
            ladrillos.add(new Ladrillo(x, y, ladrilloAncho, ladrilloAlto));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillOval(pelota.getX(), pelota.getY(), pelota.getAncho(), pelota.getAlto());

        g.setColor(Color.BLUE);
        g.fillRect(paleta.getX(), paleta.getY(), paleta.getAncho(), paleta.getAlto());

        g.setColor(Color.GRAY);
        g.fillRect(bloqueSuperior.getX(), bloqueSuperior.getY(), bloqueSuperior.getAncho(), bloqueSuperior.getAlto());
        g.fillRect(bloqueInferior.getX(), bloqueInferior.getY(), bloqueInferior.getAncho(), bloqueInferior.getAlto());

        g.setColor(Color.RED);
        for (Ladrillo ladrillo : ladrillos) {
            if (ladrillo.isVisible()) {
                g.fillRect(ladrillo.getX(), ladrillo.getY(), ladrillo.getAncho(), ladrillo.getAlto());
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Puntuación: " + puntuacion, 20, 40);
        g.drawString("Vidas: " + vidas, 20, 70);
    }

    public void iniciarJuego() {
        if (!juegoIniciado) {
            pelota.setDx(4);
            pelota.setDy(-4);
            juegoIniciado = true;
            remove(instruccionLabel);
            repaint();
        }
    }

    private void detectarColisiones() {
        if (pelota.intersects(bloqueSuperior) || pelota.intersects(bloqueInferior)) {
            pelota.invertirDireccionY();
        }

        if (pelota.getX() <= 0 || pelota.getX() + pelota.getAncho() >= getWidth()) {
            pelota.invertirDireccionX();
        }

        if (pelota.intersects(paleta)) {
            double relativoImpacto = (pelota.getX() + (pelota.getAncho() / 2.0) - paleta.getX()) / paleta.getAncho();
            pelota.setDx(-pelota.getDx() * 0.8);
            pelota.setDy(-Math.abs(pelota.getDy()));
            pelota.setY(paleta.getY() - pelota.getAlto());
        }

        for (Ladrillo ladrillo : ladrillos) {
            if (ladrillo.isVisible() && pelota.intersects(ladrillo)) {
                ladrillo.setVisible(false);
                juegoService.actualizarPuntuacion(10);
                
                int centroLadrilloX = ladrillo.getX() + ladrillo.getAncho() / 2;
                int centroLadrilloY = ladrillo.getY() + ladrillo.getAlto() / 2;
                int centroPelotaX = pelota.getX() + pelota.getAncho() / 2;
                int centroPelotaY = pelota.getY() + pelota.getAlto() / 2;
                
                if (Math.abs(centroPelotaX - centroLadrilloX) > Math.abs(centroPelotaY - centroLadrilloY)) {
                    pelota.invertirDireccionX();
                } else {
                    pelota.invertirDireccionY();
                }
                break;
            }
        }
        
        if (puntuacion >= 500) {
            mostrarMensajeVictoria();
        }
    }

    void reiniciarPelota() {
        pelota.setX(paleta.getX() + paleta.getAncho()/2 - pelota.getAncho()/2);
        pelota.setY(paleta.getY() - pelota.getAlto() - 5);
        pelota.setDx(0);
        pelota.setDy(0);
        juegoIniciado = false;
        
        configurarInstrucciones(getWidth(), getHeight());
    }

    private void salirAlMenu() {
        timer.stop();
        frame.dispose();
        
        JFrame menuFrame = new JFrame("Brick Breaker");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(600, 400);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.add(new Menu(menuFrame));
        menuFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (juegoIniciado) {
            long tiempoActual = System.currentTimeMillis();

            if (tiempoActual - tiempoInicio >= tiempoJuego) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "¡Tiempo agotado! Game Over.");
                salirAlMenu();
                return;
            }

            paleta.mover(getWidth());
            pelota.mover();
            detectarColisiones();

            verificarYAgregarLadrillos();

            if (pelota.getY() > getHeight()) {
                juegoService.perderVida();
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

    private void verificarYAgregarLadrillos() {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - tiempoUltimosLadrillos >= tiempoLadrillos) {
            agregarLadrillos(5);
            tiempoUltimosLadrillos = tiempoActual;
        }
    }

    public int getPuntuacion() { 
        return puntuacion; 
    }
    
    public void actualizarPuntuacion(int puntos) {
        this.puntuacion += puntos;
    }
    
    public void perderVida() {
        this.vidas--;
        this.puntuacion -= 5;
    }
    
    public void mostrarMensajeVictoria() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "¡Felicidades! Has ganado!");
        salirAlMenu();
    }
}