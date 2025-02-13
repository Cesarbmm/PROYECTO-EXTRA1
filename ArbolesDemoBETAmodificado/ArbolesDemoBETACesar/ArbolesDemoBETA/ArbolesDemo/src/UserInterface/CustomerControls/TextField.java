package UserInterface.CustomerControls;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import UserInterface.Styles;

public class TextField extends JTextField {

    // Constructor con configuración básica
    public TextField() {
        customizeComponent();
    }

    // Constructor para inicializar con un número de columnas
    public TextField(int columns) {
        super(columns);
        customizeComponent();
    }

    // Configuración básica del componente
    private void customizeComponent() {
        setFont(Styles.FONT_BOLD); // Fuente definida en Styles
        setHorizontalAlignment(Styles.ALIGNMENT_RIGHT); // Alineación derecha
        setEditable(true); // Editable por defecto
        setBackground(Styles.COLOR_FONT_BG); // Fondo definido en Styles
        setForeground(Styles.COLOR_FONT); // Color del texto definido en Styles
        setBorder(Styles.createBorderRect()); // Borde definido en Styles

        // Asignar filtro para entrada numérica
        ((AbstractDocument) this.getDocument()).setDocumentFilter(new NumericFilter());
    }

    // Método para limpiar el campo de texto
    public void clear() {
        setText("");
    }

    // Método para agregar texto adicional
    public void appendText(String text) {
        setText(getText() + text);
    }

    // Método para habilitar o deshabilitar edición
    public void setEditableState(boolean state) {
        setEditable(state);
    }

    // Clase interna para filtrar entradas numéricas
    private static class NumericFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                throws BadLocationException {
            if (string.matches("\\d*")) { // Permitir solo números
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) 
                throws BadLocationException {
            if (string.matches("\\d*")) { // Permitir solo números
                super.replace(fb, offset, length, string, attr);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) 
                throws BadLocationException {
            super.remove(fb, offset, length);
        }
    }
}
