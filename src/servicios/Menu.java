package servicios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel {

    private JFrame frame;

    public Menu(JFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        JLabel titulo = new JLabel("Brick Breaker");
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);

        JButton btnFacil = new JButton("Nivel Fácil");
        JButton btnMedio = new JButton("Nivel Medio");
        JButton btnSalir = new JButton("Salir");

        Font botonFont = new Font("Arial", Font.BOLD, 18);
        btnFacil.setFont(botonFont);
        btnMedio.setFont(botonFont);
        btnSalir.setFont(botonFont);

        btnFacil.setFocusPainted(false);
        btnMedio.setFocusPainted(false);
        btnSalir.setFocusPainted(false);

        btnFacil.setBackground(Color.WHITE);
        btnMedio.setBackground(Color.WHITE);
        btnSalir.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        gbc.gridy = 0;
        add(titulo, gbc);

        gbc.gridy = 1;
        add(btnFacil, gbc);

        gbc.gridy = 2;
        add(btnMedio, gbc);

        gbc.gridy = 3;
        add(btnSalir, gbc);

        btnFacil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarJuego("facil");
            }
        });

        btnMedio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarJuego("medio");
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void iniciarJuego(String nivel) {
        frame.getContentPane().removeAll();
        if (nivel.equals("facil")) {
            frame.getContentPane().add(new PanelJuegoNivelFacil(frame));
        }
        frame.revalidate();
        frame.repaint();
    }
}
