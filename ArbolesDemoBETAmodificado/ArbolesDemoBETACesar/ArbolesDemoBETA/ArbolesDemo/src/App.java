import UserInterface.Form.MenuPrincipal;
import UserInterface.Form.SplashForm;

public class App {

    public static void main(String[] args) {
        
        SplashForm splashForm = new SplashForm();
        
        MenuPrincipal menu = new MenuPrincipal();
        menu.setVisible(true);

        splashForm.dispose();

    }
}
