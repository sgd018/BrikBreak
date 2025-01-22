package servicios;

public interface IJuegoService {
    void iniciarJuego();
    void pausarJuego();
    void reiniciarNivel();
    void actualizarPuntuacion(int puntos);
    void perderVida();
    boolean verificarVictoria();
    void agregarLadrillos(int cantidad);
}