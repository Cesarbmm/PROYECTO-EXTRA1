package UserInterface.CustomerControls;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import UserInterface.Styles;

// Clase personalizada para el ScrollBar
public class ScrollBar extends JScrollBar {
    public ScrollBar(int orientation) {
        super(orientation);
        setUI(new ScrollBarUI());
        setPreferredSize(new Dimension(8, 0)); // Ancho más delgado para la barra
        setForeground(Styles.COLOR_FOREGROUND);
        setBackground(Styles.COLOR_BACKGROUND);
    }
}

// Clase para personalizar la UI del ScrollBar
class ScrollBarUI extends BasicScrollBarUI {
    private final Dimension ZERO_DIM = new Dimension(0, 0);

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return new InvisibleScrollBarButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return new InvisibleScrollBarButton();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(Styles.COLOR_BACKGROUND);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Color cuando el mouse está sobre la barra
        if (isDragging || isThumbRollover()) {
            g2.setColor(Styles.COLOR_FOREGROUND_PRESSED);
        } else {
            g2.setColor(Styles.COLOR_FOREGROUND);
        }

        // Dibuja el thumb con esquinas redondeadas
        g2.fillRoundRect(thumbBounds.x + 1, thumbBounds.y + 1, 
                        thumbBounds.width - 2, thumbBounds.height - 2, 
                        8, 8);
        g2.dispose();
    }

    private class InvisibleScrollBarButton extends JButton {
        private InvisibleScrollBarButton() {
            setOpaque(false);
            setFocusable(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBorder(BorderFactory.createEmptyBorder());
        }

        @Override
        public Dimension getPreferredSize() {
            return ZERO_DIM;
        }
    }
}

