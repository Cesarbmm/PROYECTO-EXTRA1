package UserInterface.CustomerControls;

import javax.swing.JPanel;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import UserInterface.Styles;

public class Panel extends JPanel {

    // Constructor básico sin layout personalizado
    public Panel() {
        this(null); // Llama al constructor con LayoutManager pasando null
    }

    // Constructor con LayoutManager personalizado
    public Panel(LayoutManager layout) {
        if (layout != null) {
            setLayout(layout); // Establece el layout proporcionado
        } else {
            setLayout(new java.awt.GridLayout()); // Establece un layout por defecto si es null
        }
        customizeComponent();
    }

    // Método para personalizar el panel
    private void customizeComponent() {
        setOpaque(true);
        setBackground(Styles.COLOR_BACKGROUND); // Fondo del panel definido en Styles
        setBorder(Styles.createBorderRect()); // Borde definido en Styles
        addMouseListener(new PanelMouseAdapter()); // Añade eventos del ratón
    }

    // Clase interna para manejar eventos del ratón
    private class PanelMouseAdapter extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            setBackground(Styles.COLOR_BACKGROUND.brighter()); // Cambia el color al pasar el ratón
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBackground(Styles.COLOR_BACKGROUND); // Restaura el color al salir
        }
    }
}
