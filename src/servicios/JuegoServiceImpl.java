package servicios;

import javax.swing.Timer;

public class JuegoServiceImpl implements IJuegoService {
    private int tiempoTranscurrido = 0;
    private int tiempoLimite;
    private int puntosObjetivo;
    private Timer timerLadrillos;
    private PanelJuegoNivelFacil panelJuego;

    public JuegoServiceImpl(PanelJuegoNivelFacil panelJuego, String nivel, int tiempoLimite, int puntosObjetivo) {
        this.panelJuego = panelJuego;
        this.tiempoLimite = tiempoLimite;
        this.puntosObjetivo = puntosObjetivo;
        
        if (nivel.equals("facil")) {
            iniciarTimerLadrillosFacil();
        } else {
            iniciarTimerLadrillosMedio();
        }
    }

    public JuegoServiceImpl(PanelJuegoNivelMedio panelJuegoNivelMedio, String nivel, int tiempoLimite2,
			int puntosObjetivo2) {
		// TODO Auto-generated constructor stub
	}

	@Override
    public void iniciarJuego() {
        panelJuego.iniciarJuego();
    }

    @Override
    public void pausarJuego() {
        // Implementación de pausa
    }

    @Override
    public void reiniciarNivel() {
        tiempoTranscurrido = 0;
        panelJuego.reiniciarPelota();
    }

    @Override
    public void actualizarPuntuacion(int puntos) {
        panelJuego.actualizarPuntuacion(puntos);
        verificarVictoria();
    }

    @Override
    public void perderVida() {
        panelJuego.perderVida();
    }

    @Override
    public boolean verificarVictoria() {
        if (panelJuego.getPuntuacion() >= puntosObjetivo) {
            panelJuego.mostrarMensajeVictoria();
            return true;
        }
        return false;
    }

    @Override
    public void agregarLadrillos(int cantidad) {
        panelJuego.agregarLadrillos(cantidad);
    }

    private void iniciarTimerLadrillosFacil() {
        timerLadrillos = new Timer(120000, e -> { // 2 minutos
            if (tiempoTranscurrido < 240) { // Hasta el minuto 4
                agregarLadrillos(5);
                tiempoTranscurrido += 120;
            }
        });
        timerLadrillos.start();
    }

    private void iniciarTimerLadrillosMedio() {
        timerLadrillos = new Timer(15000, e -> { // 15 segundos
            if (panelJuego.getPuntuacion() < puntosObjetivo) {
                agregarLadrillos(5);
            }
        });
        timerLadrillos.start();
    }
}