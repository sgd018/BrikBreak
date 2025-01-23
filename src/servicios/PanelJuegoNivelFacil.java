package servicios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import dtos.*;

public class PanelJuegoNivelFacil extends JPanel implements ActionListener {
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
    private int tiempoJuego = 600000; // 10 minutos en milisegundos
    private int tiempoLadrillos = 120000; // Cada 2 minutos en milisegundos
    private long tiempoInicio; // Momento en que inicia el juego
    private long tiempoUltimosLadrillos; // Última vez que se añadieron ladrillos


    public PanelJuegoNivelFacil(JFrame frame) {
        this.frame = frame;
        setLayout(null);
        setBackground(Color.BLACK);

        // Obtener dimensiones de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        // Inicializar pelota en el centro inferior, justo encima de la paleta
        int pelotaX = screenWidth / 2 - 5; // 5 es la mitad del ancho de la pelota
        int pelotaY = screenHeight - 150; // 150 pixeles desde abajo
        pelota = new Pelota(pelotaX, pelotaY, 10, 10, 0, 0);

        // Inicializar paleta centrada en la parte inferior
        int paletaX = screenWidth / 2 - 50; // 50 es la mitad del ancho de la paleta
        int paletaY = screenHeight - 100; // 100 pixeles desde abajo
        paleta = new Paleta(paletaX, paletaY, 100, 10, 0);

        ladrillos = new ArrayList<>();
        inicializarLadrillos();

        // Inicializar el servicio
        this.juegoService = new JuegoServiceImpl(this, "facil", 600, 300);

        // Configurar y añadir el botón de salir
        configurarBotonSalir(screenWidth);

        // Configurar instrucciones
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
                    paleta.setDx(-8); // Aumentado la velocidad de la paleta
                    if (!juegoIniciado) iniciarJuego();
                } else if (key == KeyEvent.VK_RIGHT) {
                    paleta.setDx(8); // Aumentado la velocidad de la paleta
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
        btnSalir.setFocusable(false); // Importante para que no tome el foco del teclado
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

        // Calcular el ancho total necesario para una fila de ladrillos
        int anchoFila = numLadrillosPorFila * ladrilloAncho + (numLadrillosPorFila - 1) * espacioEntreX;

        // Centrar los ladrillos horizontalmente
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
        
        // Calcular la posición para los nuevos ladrillos
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

        // Dibujar la pelota
        g.setColor(Color.WHITE);
        g.fillOval(pelota.getX(), pelota.getY(), pelota.getAncho(), pelota.getAlto());

        // Dibujar la paleta
        g.setColor(Color.BLUE);
        g.fillRect(paleta.getX(), paleta.getY(), paleta.getAncho(), paleta.getAlto());

        // Dibujar los ladrillos
        g.setColor(Color.RED);
        for (Ladrillo ladrillo : ladrillos) {
            if (ladrillo.isVisible()) {
                g.fillRect(ladrillo.getX(), ladrillo.getY(), ladrillo.getAncho(), ladrillo.getAlto());
            }
        }

        // Dibujar puntuación y vidas
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Puntuación: " + puntuacion, 20, 40);
        g.drawString("Vidas: " + vidas, 20, 70);
    }

    public void iniciarJuego() {
        if (!juegoIniciado) {
            pelota.setDx(5); // Velocidad inicial de la pelota
            pelota.setDy(-5);
            juegoIniciado = true;
            remove(instruccionLabel);
            repaint();
        }
    }

    

    private void detectarColisiones() {
        // Colisiones con los bordes
        if (pelota.getX() <= 0 || pelota.getX() + pelota.getAncho() >= getWidth()) {
            pelota.invertirDireccionX();
        }
        if (pelota.getY() <= 0) {
            pelota.invertirDireccionY();
        }

        // Colisión con la paleta
        if (pelota.intersects(paleta)) {
            // Calcular el punto de impacto relativo en la paleta
            double relativoImpacto = (pelota.getX() + (pelota.getAncho() / 2.0) - paleta.getX()) / paleta.getAncho();
            
            // Ajustar el ángulo de rebote basado en el punto de impacto
            double angulo = relativoImpacto * Math.PI - Math.PI/2;
            double velocidad = Math.sqrt(pelota.getDx() * pelota.getDx() + pelota.getDy() * pelota.getDy());
            
            pelota.setDx(-pelota.getDx());
            pelota.setDy(-pelota.getDy());
            
            // Asegurar que la pelota no quede "pegada" a la paleta
            pelota.setY(paleta.getY() - pelota.getAlto());
        }

        // Colisión con los ladrillos
        for (Ladrillo ladrillo : ladrillos) {
            if (ladrillo.isVisible() && pelota.intersects(ladrillo)) {
                ladrillo.setVisible(false);
                juegoService.actualizarPuntuacion(10);
                
                // Determinar desde qué lado viene la colisión
                int centroLadrilloX = ladrillo.getX() + ladrillo.getAncho() / 2;
                int centroLadrilloY = ladrillo.getY() + ladrillo.getAlto() / 2;
                int centroPelotaX = pelota.getX() + pelota.getAncho() / 2;
                int centroPelotaY = pelota.getY() + pelota.getAlto() / 2;
                
                // Si la diferencia en X es mayor que en Y, invertir dirección X
                if (Math.abs(centroPelotaX - centroLadrilloX) > Math.abs(centroPelotaY - centroLadrilloY)) {
                    pelota.invertirDireccionX();
                } else {
                    pelota.invertirDireccionY();
                }
                break; // Salir del bucle después de la primera colisión
            }
        }
        
        if (puntuacion >= 300) {
            mostrarMensajeVictoria();
            return;
        }

    }

    void reiniciarPelota() {
        // Reiniciar la pelota justo encima de la paleta
        pelota.setX(paleta.getX() + paleta.getAncho()/2 - pelota.getAncho()/2);
        pelota.setY(paleta.getY() - pelota.getAlto() - 5);
        pelota.setDx(0);
        pelota.setDy(0);
        juegoIniciado = false;
        
        // Mostrar nuevamente las instrucciones
        configurarInstrucciones(getWidth(), getHeight());
    }

    private void salirAlMenu() {
        timer.stop();
        frame.dispose(); // Cerrar la ventana de juego
        
        // Crear y mostrar nueva ventana con el menú
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

            // Verificar tiempo restante
            if (tiempoActual - tiempoInicio >= tiempoJuego) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "¡Tiempo agotado! Game Over.");
                salirAlMenu();
                return;
            }

            paleta.mover(getWidth());
            pelota.mover();
            detectarColisiones();

            verificarYAgregarLadrillos(); // Verificar si es momento de añadir ladrillos

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


    // Getters y setters necesarios
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
    
    private void verificarYAgregarLadrillos() {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - tiempoUltimosLadrillos >= tiempoLadrillos && tiempoActual - tiempoInicio <= 240000) {
            agregarLadrillos(5); // Añadir 5 ladrillos
            tiempoUltimosLadrillos = tiempoActual; // Actualizar el tiempo de última adición
        }
    }

}