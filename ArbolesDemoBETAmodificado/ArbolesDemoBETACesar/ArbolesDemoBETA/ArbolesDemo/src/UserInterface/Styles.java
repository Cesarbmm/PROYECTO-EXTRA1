package UserInterface;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public abstract class Styles {

    // Futuristic and elegant color palette
    public static final Color COLOR_FONT = new Color(0xE0E0E0);        // Light gray for main text
    public static final Color COLOR_FONT_LIGHT = new Color(0xFFFFFF);  // White for light text
    public static final Color COLOR_FONT_BG = new Color(0x263238);     // Dark blue-gray for backgrounds
    public static final Color COLOR_CURSOR = new Color(0x00BCD4);      // Cyan for cursor
    public static final Color COLOR_BORDER_BUTTON = new Color(0x37474F); // Dark gray for borders
    public static final Color COLOR_BACKGROUND = new Color(0x37474F);   // Dark gray for backgrounds
    public static final Color COLOR_FOREGROUND = new Color(0x00ACC1);   // Light cyan for primary elements
    public static final Color COLOR_FOREGROUND_PRESSED = new Color(0x0097A7); // Darker cyan for pressed states

    // Modern typography using Roboto font
    public static final Font FONT = new Font("Roboto", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Roboto", Font.BOLD, 14);
    public static final Font FONT_BOLD_24 = new Font("Roboto", Font.BOLD, 24);
    public static final Font FONT_SMALL = new Font("Roboto", Font.PLAIN, 12);

    // Alignment constants
    public static final int ALIGNMENT_LEFT = SwingConstants.LEFT;
    public static final int ALIGNMENT_RIGHT = SwingConstants.RIGHT;
    public static final int ALIGNMENT_CENTER = SwingConstants.CENTER;

    // Cursors
    public static final Cursor CURSOR_HAND = new Cursor(Cursor.HAND_CURSOR);
    public static final Cursor CURSOR_DEFAULT = new Cursor(Cursor.DEFAULT_CURSOR);

    // Graphic resources
    public static final URL URL_MENU = Styles.class.getResource("/UserInterface/Resources/Img/MENU.png");
    public static final URL URL_LOGO = Styles.class.getResource("/UserInterface/Resources/Img/LOGO.png");

    // Border creation methods with elegant styling
    public static Border createTitledBorder(String title) {
        var border = BorderFactory.createTitledBorder(
            createBorderRect(), 
            title
        );
        border.setTitleColor(COLOR_FONT);
        border.setTitleFont(FONT_BOLD);
        return border;
    }

    public static final CompoundBorder createBorderRect() {
        return BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER_BUTTON, 1),
                new EmptyBorder(10, 12, 10, 12)  // Padding for better spacing
        );
    }

    // JOptionPane customization with elegant styling
    public static void customizeOptionPane() {
        UIManager.put("OptionPane.background", COLOR_BACKGROUND);
        UIManager.put("Panel.background", COLOR_BACKGROUND);
        UIManager.put("OptionPane.messageForeground", COLOR_FONT);
        UIManager.put("OptionPane.messageFont", FONT_BOLD);
        UIManager.put("OptionPane.border", createBorderRect());
        UIManager.put("Button.background", COLOR_FOREGROUND);
        UIManager.put("Button.foreground", COLOR_FONT_LIGHT);
        UIManager.put("Button.font", FONT_BOLD);
    }

    // Utility methods for messages
    public static final void showMsg(String msg) {
        customizeOptionPane();
        JOptionPane.showMessageDialog(null, msg, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static final void showMsgError(String msg) {
        customizeOptionPane();
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static final boolean showConfirmYesNo(String msg) {
        customizeOptionPane();
        return (JOptionPane.showConfirmDialog(null, msg, "Confirmation",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
    }
}