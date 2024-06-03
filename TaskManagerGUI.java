import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;
import java.util.ArrayList;

public class TaskManagerGUI extends JFrame {
    private ArrayList<Task> tasks;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextArea summaryArea;

    public TaskManagerGUI() {
        tasks = new ArrayList<>();
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        summaryArea = new JTextArea(10, 30);
        summaryArea.setEditable(false);
        setupGUI();
    }

    private void setupGUI() {
        setTitle("Task Manager - Rudainah's App");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set a modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Setup task list panel
        JPanel taskPanel = new JPanel(new BorderLayout(5, 5));
        taskPanel.setBorder(BorderFactory.createTitledBorder("Tasks"));
        taskPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);

        // Setup button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        
        JButton addTaskButton = new JButton("Add Task");
        JButton showTasksButton = new JButton("Show Tasks");
        JButton increaseButton = new JButton("Increase Percentage");
        JButton decreaseButton = new JButton("Decrease Percentage");
        JButton summaryButton = new JButton("Show Summary");
        JButton exitButton = new JButton("Exit");

        addTaskButton.setToolTipText("Add a new task");
        showTasksButton.setToolTipText("Show all tasks");
        increaseButton.setToolTipText("Increase the percentage of the selected task");
        decreaseButton.setToolTipText("Decrease the percentage of the selected task");
        summaryButton.setToolTipText("Show summary of all tasks");
        exitButton.setToolTipText("Exit the application");

        addTaskButton.addActionListener(e -> addTask());
        showTasksButton.addActionListener(e -> showTasks());
        increaseButton.addActionListener(e -> changePercentage(true));
        decreaseButton.addActionListener(e -> changePercentage(false));
        summaryButton.addActionListener(e -> showSummary());
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(addTaskButton);
        buttonPanel.add(showTasksButton);
        buttonPanel.add(increaseButton);
        buttonPanel.add(decreaseButton);
        buttonPanel.add(summaryButton);
        buttonPanel.add(exitButton);

        // Setup summary panel
        JPanel summaryPanel = new JPanel(new BorderLayout(5, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
        summaryPanel.add(new JScrollPane(summaryArea), BorderLayout.CENTER);

        panel.add(taskPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(summaryPanel, BorderLayout.EAST);

        add(panel);
    }

    private void addTask() {
        String name = JOptionPane.showInputDialog(this, "Enter task name:");
        if (name != null && !name.trim().isEmpty()) {
            try {
                int percentage = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter task completion percentage:"));
                if (percentage >= 0 && percentage <= 100) {
                    Task newTask = new Task(name, percentage);
                    tasks.add(newTask);
                    taskListModel.addElement(name + " - " + percentage + "%");
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a valid percentage (0-100).");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for percentage.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Task name cannot be empty.");
        }
    }

    private void showTasks() {
        taskListModel.clear();
        for (Task task : tasks) {
            taskListModel.addElement(task.taskName + " - " + task.taskPercentage + "%");
        }
    }

    private void changePercentage(boolean increase) {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            try {
                int value = Integer.parseInt(JOptionPane.showInputDialog(this, (increase ? "Increase" : "Decrease") + " percentage by:"));
                Task selectedTask = tasks.get(index);
                int newPercentage = increase ? selectedTask.taskPercentage + value : selectedTask.taskPercentage - value;
                if (newPercentage >= 0 && newPercentage <= 100) {
                    selectedTask.taskPercentage = newPercentage;
                    taskListModel.set(index, selectedTask.taskName + " - " + selectedTask.taskPercentage + "%");
                } else {
                    JOptionPane.showMessageDialog(this, "Percentage must be between 0 and 100.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task first.");
        }
    }

    private void showSummary() {
        int totalPercentage = 0;
        summaryArea.setText("");
        for (Task task : tasks) {
            totalPercentage += task.taskPercentage;
            summaryArea.append(task.taskName + " - " + task.taskPercentage + "%\n");
        }
        int averagePercentage = tasks.isEmpty() ? 0 : totalPercentage / tasks.size();
        summaryArea.append("\nAverage Completion: " + averagePercentage + "%\n");
        if (averagePercentage >= 80) {
            summaryArea.append("You have completed your tasks perfectly!\n");
        } else if (averagePercentage >= 50) {
            summaryArea.append("You have completed your tasks well!\n");
        } else if (averagePercentage >= 30) {
            summaryArea.append("I expect more from you to complete your tasks in coming days!\n");
        } else {
            summaryArea.append("Don't neglect your tasks, try your best!\n");
        }
    }
}
