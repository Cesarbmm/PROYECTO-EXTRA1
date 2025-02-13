package UserInterface.CustomerControls;

import UserInterface.Styles;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;

public class Button extends JButton implements MouseListener {

    // Constructor con texto
    public Button(String text) {
        customizeComponent(text);
        addMouseListener(this);
    }

    // Constructor con texto e icono
    public Button(String text, String iconPath) {
        this(text); // Reutilizar la configuración básica
        setIcon(new javax.swing.ImageIcon(getClass().getResource(iconPath))); // Establecer el icono
    }

    // Configuración básica del componente
    private void customizeComponent(String text) {
        setText(text);
        setFont(Styles.FONT_BOLD); // Fuente definida en Styles
        setOpaque(true);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(true);
        setBackground(Styles.COLOR_FOREGROUND); // Fondo morado definido en Styles
        setForeground(Styles.COLOR_FONT); // Texto blanco definido en Styles
        setHorizontalAlignment(Styles.ALIGNMENT_CENTER); // Alineación centrada
        setCursor(Styles.CURSOR_HAND); // Cursor de mano
        setBorder(Styles.createBorderRect()); // Borde definido en Styles
    }

    // Métodos del MouseListener para personalizar eventos del ratón
    @Override
    public void mouseClicked(MouseEvent e) {
        // No se implementa ninguna acción aquí
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setBackground(Styles.COLOR_FOREGROUND_PRESSED); // Cambiar color al presionar
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setBackground(Styles.COLOR_FOREGROUND); // Restaurar color al soltar
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackground(Styles.COLOR_FOREGROUND.brighter()); // Aclarar color al pasar el ratón
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackground(Styles.COLOR_FOREGROUND); // Restaurar color al salir
    }
}
