package UserInterface.CustomerControls;

import javax.swing.*;
import UserInterface.Styles;
import java.awt.*;
import java.net.URL;

public class Label extends JLabel {
    // Constructor básico con texto
    public Label(String text) {
        customizeComponent(text, null);
    }

    // Constructor adicional con texto e ícono
    public Label(String text, Icon icon) {
        customizeComponent(text, icon);
    }

    // Constructor para crear un label solo con imagen desde URL
    public Label(URL imageUrl) {
        this(imageUrl, -1, -1); // Sin redimensionar por defecto
    }

    // Constructor para crear un label con imagen redimensionada
    public Label(URL imageUrl, int width, int height) {
        if (imageUrl != null) {
            setIcon(resizeIcon(new ImageIcon(imageUrl), width, height));
        } else {
            setText("Imagen no encontrada");
        }
        customizeComponent(null, getIcon());
    }

    // Método para redimensionar imágenes
    private Icon resizeIcon(ImageIcon icon, int width, int height) {
        if (icon != null && width > 0 && height > 0) {
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return icon; // Devuelve el icono original si no se requiere redimensionar
    }

    // Configurar estilo base para el Label
    private void customizeComponent(String text, Icon icon) {
        if (text != null) {
            setText(text);
        }
        if (icon != null) {
            setIcon(icon);
        }
        setOpaque(true);
        setFont(Styles.FONT_BOLD);
        // Se eliminó la línea: setBorder(Styles.createBorderRect());
        setBackground(Styles.COLOR_BACKGROUND);
        setHorizontalAlignment(Styles.ALIGNMENT_CENTER);
        setVerticalAlignment(SwingConstants.CENTER); // Centrar verticalmente también
    }

    // Método para configurar un tooltip
    public void setTooltip(String tooltip) {
        setToolTipText(tooltip);
    }
}