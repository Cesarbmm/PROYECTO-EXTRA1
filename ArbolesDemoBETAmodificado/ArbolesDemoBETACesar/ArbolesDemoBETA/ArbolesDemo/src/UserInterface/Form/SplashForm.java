package UserInterface.Form;

import java.awt.*;
import javax.swing.*;

import UserInterface.Styles;

public class SplashForm extends JFrame {

    private int progress = 0;

    public SplashForm() {
        // Eliminar decoraciones y hacer la ventana circular
        setUndecorated(true);
        setSize(340, 340);
        setShape(new java.awt.geom.Ellipse2D.Double(0, 0, getWidth(), getHeight()));

        // Fondo transparente
        setBackground(new Color(0, 0, 0, 0));
        setLayout(new BorderLayout());

        // Panel personalizado para el círculo
        CircularPanel circularPanel = new CircularPanel();

        // Añadir el logo en el centro
        ImageIcon icon = new ImageIcon(Styles.URL_LOGO);
        ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(320, 320, Image.SCALE_SMOOTH));
        JLabel logo = new JLabel(scaledIcon);
        logo.setHorizontalAlignment(JLabel.CENTER);
        logo.setVerticalAlignment(JLabel.CENTER);
        circularPanel.add(logo);

        add(circularPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);

        // Simular el progreso
        for (int i = 0; i <= 100; i++) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progress = i;
            circularPanel.setProgress(progress);
        }

        setVisible(false);
        dispose();
    }

    // Clase personalizada para dibujar el círculo y el progreso
    private static class CircularPanel extends JPanel {
        private int progress = 0;

        public CircularPanel() {
            setOpaque(false);
            setLayout(new GridBagLayout());
        }

        public void setProgress(int progress) {
            this.progress = progress;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Dibujar el fondo circular
            g2d.setColor(Styles.COLOR_BACKGROUND);
            g2d.fillOval(0, 0, getWidth(), getHeight());

            // Dibujar el borde circular del progreso
            g2d.setColor(Styles.COLOR_FOREGROUND);
            g2d.setStroke(new BasicStroke(8f));
            int angle = (int) (360 * (progress / 100.0));
            g2d.drawArc(10, 10, getWidth() - 20, getHeight() - 20, 90, -angle);
        }
    }

}
