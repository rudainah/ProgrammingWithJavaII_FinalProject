import javax.swing.SwingUtilities;

public class TaskManagerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaskManagerGUI app = new TaskManagerGUI();
            app.setVisible(true);
        });
    }
}
