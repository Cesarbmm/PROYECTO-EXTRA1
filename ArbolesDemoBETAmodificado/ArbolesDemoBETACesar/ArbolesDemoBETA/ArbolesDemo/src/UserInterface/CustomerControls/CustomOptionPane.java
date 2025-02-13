package UserInterface.CustomerControls;

import javax.swing.*;
import java.awt.*;
import UserInterface.Styles;

public class CustomOptionPane extends JOptionPane {
    
    // Constructor
    public CustomOptionPane() {
        super();
        customizeComponent();
    }

    // Método para personalizar el JOptionPane
    private void customizeComponent() {
        // Estilo del fondo
        UIManager.put("OptionPane.background", Styles.COLOR_BACKGROUND);
        UIManager.put("Panel.background", Styles.COLOR_BACKGROUND);
        UIManager.put("OptionPane.border", Styles.createBorderRect());

        // Estilo de los botones
        UIManager.put("Button.background", Styles.COLOR_FOREGROUND);
        UIManager.put("Button.foreground", Styles.COLOR_FONT);
        UIManager.put("Button.font", Styles.FONT_BOLD);
        UIManager.put("Button.select", Styles.COLOR_FOREGROUND_PRESSED);
        UIManager.put("Button.focus", Styles.COLOR_BORDER_BUTTON);
        UIManager.put("Button.border", Styles.createBorderRect());

        // Estilo del texto
        UIManager.put("OptionPane.messageForeground", Styles.COLOR_FONT);
        UIManager.put("OptionPane.messageFont", Styles.FONT_BOLD);
        
        // Iconos personalizados
        UIManager.put("OptionPane.errorIcon", createColorIcon(Color.RED));
        UIManager.put("OptionPane.informationIcon", createColorIcon(Styles.COLOR_FONT_LIGHT));
        UIManager.put("OptionPane.warningIcon", createColorIcon(Color.YELLOW));
        UIManager.put("OptionPane.questionIcon", createColorIcon(Styles.COLOR_FONT_LIGHT));
    }

    // Método auxiliar para crear iconos personalizados
    private Icon createColorIcon(Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(color);
                g2d.fillOval(x, y, getIconWidth(), getIconHeight());
                g2d.dispose();
            }

            @Override
            public int getIconWidth() {
                return 32;
            }

            @Override
            public int getIconHeight() {
                return 32;
            }
        };
    }

    // Métodos de utilidad mejorados
    public static void showMessage(Component parentComponent, String message, String title, int messageType) {
        CustomOptionPane optionPane = new CustomOptionPane();
        JDialog dialog = optionPane.createDialog(parentComponent, title);
        dialog.setModal(true);
        
        // Configurar el mensaje
        optionPane.setMessage(message);
        optionPane.setMessageType(messageType);
        
        // Mostrar el diálogo
        dialog.setVisible(true);
    }

    public static boolean showConfirmDialog(Component parentComponent, String message, String title) {
        CustomOptionPane optionPane = new CustomOptionPane();
        optionPane.setMessage(message);
        optionPane.setMessageType(QUESTION_MESSAGE);
        optionPane.setOptionType(YES_NO_OPTION);
        
        JDialog dialog = optionPane.createDialog(parentComponent, title);
        dialog.setVisible(true);
        
        Object selectedValue = optionPane.getValue();
        return selectedValue != null && selectedValue.equals(YES_OPTION);
    }

    // Métodos de conveniencia
    public static void showError(Component parentComponent, String message) {
        showMessage(parentComponent, message, "Error", ERROR_MESSAGE);
    }

    public static void showInfo(Component parentComponent, String message) {
        showMessage(parentComponent, message, "Información", INFORMATION_MESSAGE);
    }

    public static void showWarning(Component parentComponent, String message) {
        showMessage(parentComponent, message, "Advertencia", WARNING_MESSAGE);
    }
}