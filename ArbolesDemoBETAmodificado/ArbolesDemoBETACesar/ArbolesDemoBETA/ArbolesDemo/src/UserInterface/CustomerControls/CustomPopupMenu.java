package UserInterface.CustomerControls;

import javax.swing.*;
import UserInterface.Styles;
import java.awt.*;

public class CustomPopupMenu extends JPopupMenu {
    // Constructor básico
    public CustomPopupMenu() {
        customizeComponent();
    }

    // Constructor con título
    public CustomPopupMenu(String label) {
        customizeComponent();
        setBorder(BorderFactory.createTitledBorder(label));
    }

    // Método para añadir items al menú con estilo personalizado
    public JMenuItem addStyledItem(String text) {
        JMenuItem item = new JMenuItem(text);
        customizeMenuItem(item);
        add(item);
        return item;
    }

    // Configurar estilo base para el PopupMenu
    private void customizeComponent() {
        setOpaque(true);
        setBackground(Styles.COLOR_BACKGROUND);
        setBorderPainted(true);
        setFont(Styles.FONT_BOLD);
    }

    // Configurar estilo para cada item del menú
    private void customizeMenuItem(JMenuItem item) {
        item.setFont(Styles.FONT_BOLD);
        item.setBackground(Styles.COLOR_BACKGROUND);
        item.setForeground(Color.BLACK);
        
        // Efectos hover
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(Styles.COLOR_BACKGROUND.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(Styles.COLOR_BACKGROUND);
            }
        });
    }

    // Método para establecer tooltip en items específicos
    public void setItemTooltip(JMenuItem item, String tooltip) {
        item.setToolTipText(tooltip);
    }
}