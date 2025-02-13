package UserInterface.CustomerControls;

import javax.swing.*;
import UserInterface.Styles;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuItem extends JMenuItem {
    // Constructor básico
    public MenuItem(String text) {
        customizeComponent(text, null);
    }

    // Constructor con texto e icono
    public MenuItem(String text, Icon icon) {
        customizeComponent(text, icon);
    }

    // Constructor con texto e icono redimensionado
    public MenuItem(String text, Icon icon, int iconWidth, int iconHeight) {
        if (icon != null) {
            setIcon(resizeIcon(new ImageIcon(icon.toString()), iconWidth, iconHeight));
        }
        customizeComponent(text, getIcon());
    }

    // Método para redimensionar iconos
    private Icon resizeIcon(ImageIcon icon, int width, int height) {
        if (icon != null && width > 0 && height > 0) {
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return icon;
    }

    // Configurar estilo base para el MenuItem
    private void customizeComponent(String text, Icon icon) {
        if (text != null) {
            setText(text);
        }
        if (icon != null) {
            setIcon(icon);
        }

        // Configuración básica
        setOpaque(true);
        setFont(Styles.FONT_BOLD);
        setBackground(Styles.COLOR_BACKGROUND);
        setForeground(Color.BLACK);

        // Margen y padding
        setMargin(new Insets(5, 10, 5, 10));

        // Efectos hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Styles.COLOR_BACKGROUND.darker());
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Styles.COLOR_BACKGROUND);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(Styles.COLOR_BACKGROUND.darker().darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(Styles.COLOR_BACKGROUND.darker());
            }
        });
    }

    // Método para configurar tooltip
    public void setTooltip(String tooltip) {
        setToolTipText(tooltip);
    }

    // Método para deshabilitar el ítem con estilo personalizado
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            setBackground(new Color(240, 240, 240));
            setForeground(Color.GRAY);
        } else {
            setBackground(Styles.COLOR_BACKGROUND);
            setForeground(Color.BLACK);
        }
    }

    // Método para establecer un acceso rápido de teclado
    public void setShortcut(KeyStroke keyStroke) {
        setAccelerator(keyStroke);
    }
}