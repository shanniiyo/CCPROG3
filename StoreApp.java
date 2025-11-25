import javax.swing.SwingUtilities;

public class StoreApp {

    public static void main(String[] args) {
        // Ensure the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            StoreGUI storeGUI = new StoreGUI();
            storeGUI.setVisible(true);
        });
    }
}
