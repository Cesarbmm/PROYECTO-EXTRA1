package UserInterface.CustomerControls;

import javax.swing.*;
import UserInterface.Styles;
import java.awt.*;

// Clase personalizada para el JSplitPane
public class SplitPane extends JSplitPane {
    public SplitPane(int orientation, Component leftComponent, Component rightComponent) {
        super(orientation, leftComponent, rightComponent);
        customizeComponent();
    }

    private void customizeComponent() {
        setDividerSize(5); // Tamaño más delgado para el divisor
        setBorder(null); // Elimina el borde por defecto
        
        // Personaliza el color del divisor
        setBackground(Styles.COLOR_BORDER_BUTTON);
        
        // Aplica el UIManager para personalizar el aspecto del divisor
        UIManager.put("SplitPane.background", Styles.COLOR_BORDER_BUTTON);
        UIManager.put("SplitPane.dividerFocusColor", Styles.COLOR_BORDER_BUTTON);
        UIManager.put("SplitPane.darkShadow", Styles.COLOR_BORDER_BUTTON);
        UIManager.put("SplitPane.light", Styles.COLOR_BORDER_BUTTON);
    }
}