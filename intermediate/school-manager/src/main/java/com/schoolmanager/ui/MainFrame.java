package com.schoolmanager.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.schoolmanager.config.ApplicationConfig;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private final ApplicationConfig applicationConfig;

    public MainFrame(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;

        this.setTitle("School Manager");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        createContent();

        NavigationPanel navigationPanel = new NavigationPanel(
                () -> show("students"),
                () -> show("teachers"),
                () -> show("subjects"),
                () -> show("classrooms"),
                () -> show("enrollments"),
                () -> show("grades")
        );

        setLayout(new BorderLayout());

        add(navigationPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void createContent() {
        contentPanel.add(
                new StudentPanel(
                        applicationConfig.studentApplication()
                ),
                "students"
        );

        contentPanel.add(
                createPlaceholder("Teachers"),
                "teachers"
        );

        contentPanel.add(
                createPlaceholder("Subjects"),
                "subjects"
        );

        contentPanel.add(
                createPlaceholder("Classrooms"),
                "classrooms"
        );

        contentPanel.add(
                createPlaceholder("Enrollments"),
                "enrollments"
        );

        contentPanel.add(
                createPlaceholder("Grades"),
                "grades"
        );
    }

    private JPanel createPlaceholder(String title) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(
                title,
                SwingConstants.CENTER
        );

        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private void show(String card) {
        cardLayout.show(contentPanel, card);
    }

    public static void showApplication() {
        SwingUtilities.invokeLater(() -> {
            ApplicationConfig applicationConfig = new ApplicationConfig();

            MainFrame frame = new MainFrame(applicationConfig);

            frame.setVisible(true);
        });
    }
}
