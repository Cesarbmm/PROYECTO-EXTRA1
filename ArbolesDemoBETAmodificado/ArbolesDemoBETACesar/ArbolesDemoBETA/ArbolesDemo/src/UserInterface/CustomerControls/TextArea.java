package UserInterface.CustomerControls;

import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import UserInterface.Styles;

public class TextArea extends JTextArea {

    // Constructor básico con dimensiones predeterminadas
    public TextArea() {
        this(1, 1); // Dimensiones predeterminadas
    }

    // Constructor con filas y columnas personalizadas
    public TextArea(int rows, int columns) {
        super(rows, columns); // Establece las dimensiones iniciales
        customizeComponent();
    }

    // Constructor específico para estilo consola
    public TextArea(int rows, int columns, boolean consoleStyle) {
        super(rows, columns);
        if (consoleStyle) {
            customizeConsoleStyle();
        } else {
            customizeComponent();
        }
    }

    // Método para crear un TextArea estilo consola
    public static TextArea createConsole(int rows, int columns) {
        return new TextArea(rows, columns, true);
    }

    // Método para personalizar el área de texto estilo consola
    private void customizeConsoleStyle() {
        setEditable(false);
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setFont(new Font("Monospaced", Font.PLAIN, 12));
        setLineWrap(true);
        setWrapStyleWord(true);
        setBorder(Styles.createBorderRect());
    }

    // Método para personalizar el área de texto normal
    private void customizeComponent() {
        setFont(Styles.FONT);
        setForeground(Styles.COLOR_FONT_LIGHT);
        setBackground(Styles.COLOR_BACKGROUND);
        setLineWrap(true);
        setWrapStyleWord(true);
        setBorder(Styles.createBorderRect());
        addFocusListener(new TextAreaFocusListener());
    }

    // Clase interna para manejar eventos de enfoque
    private class TextAreaFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            setBackground(Styles.COLOR_BACKGROUND.brighter());
        }

        @Override
        public void focusLost(FocusEvent e) {
            setBackground(Styles.COLOR_BACKGROUND);
        }
    }
}