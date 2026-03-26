
/**
 * Write a description of class GUI here.
 *
 * @author (SYAZWI DANIAL)
 * @version (26/3/2026)
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class IncidentGUI extends JFrame {

    private JTextArea outputArea;
    private JButton loadBtn, routeBtn, stackBtn, clearBtn;

    private LinkedList<AnalystInfo> analystList;
    private Queue<AnalystInfo> internalQ, externalQ, criticalQ;

    public IncidentGUI() {
        setTitle("Cyber Incident Routing System");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // MAIN PANEL 
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(230, 240, 250));
        add(mainPanel);

        // HEADER
        JLabel title = new JLabel("Cyber Incident Management Dashboard", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(30, 60, 120));
        mainPanel.add(title, BorderLayout.NORTH);

        // TEXT AREA 
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setBackground(new Color(245, 245, 245));
        outputArea.setForeground(new Color(40, 40, 40));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70,130,180), 2),
                "System Output",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(70,130,180)
        ));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // BUTTON PANEL 
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBackground(new Color(230, 240, 250));

        loadBtn = new JButton("Load Data");
        routeBtn = new JButton("Route");
        stackBtn = new JButton("Process Stack");
        clearBtn = new JButton("Clear");

        styleButton(loadBtn, new Color(70,130,180));
        styleButton(routeBtn, new Color(46,204,113));
        styleButton(stackBtn, new Color(155,89,182)); // purple
        styleButton(clearBtn, new Color(231,76,60));

        buttonPanel.add(loadBtn);
        buttonPanel.add(routeBtn);
        buttonPanel.add(stackBtn);
        buttonPanel.add(clearBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ACTIONS
        loadBtn.addActionListener(e -> loadData());
        routeBtn.addActionListener(e -> routeAnalysts());
        stackBtn.addActionListener(e -> processStack());
        clearBtn.addActionListener(e -> outputArea.setText(""));
    }

    // BUTTON STYLE 
    private void styleButton(JButton btn, Color color) {
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
    }

    // LOAD DATA
    private void loadData() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("cyber_incidents.txt"));

            analystList = new LinkedList<>();
            String line;

            while ((line = in.readLine()) != null) {

                StringTokenizer st = new StringTokenizer(line, "|");

                String id = st.nextToken();
                String name = st.nextToken();
                String area = st.nextToken();

                String incId = st.nextToken();
                String type = st.nextToken();
                int severity = Integer.parseInt(st.nextToken());
                String date = st.nextToken();
                int ert = Integer.parseInt(st.nextToken());
                double cost = Double.parseDouble(st.nextToken());

                IncidentInfo incident = new IncidentInfo(incId, type, severity, date, ert, cost);

                AnalystInfo current = null;

                for (AnalystInfo a : analystList) {
                    if (a.getAnalystId().equals(id)) {
                        current = a;
                        break;
                    }
                }

                if (current == null) {
                    current = new AnalystInfo(id, name, area);
                    analystList.add(current);
                }

                current.addIncident(incident);
            }

            in.close();
            outputArea.setText("✅ Data Loaded!\nClick Route next.");

        } catch (Exception ex) {
            outputArea.setText("❌ Error: " + ex.getMessage());
        }
    }

    // ROUTING
    private void routeAnalysts() {

        if (analystList == null) {
            outputArea.setText("⚠ Load data first!");
            return;
        }

        internalQ = new LinkedList<>();
        externalQ = new LinkedList<>();
        criticalQ = new LinkedList<>();

        boolean toggle = true;

        for (AnalystInfo a : analystList) {
            if (a.getIncidentCount() <= 3) {
                if (toggle) internalQ.add(a);
                else externalQ.add(a);
                toggle = !toggle;
            } else {
                criticalQ.add(a);
            }
        }

        outputArea.setText("✅ Routing done!\nClick Process Stack.");
    }

    // STACK PROCESS 
    private void processStack() {

        if (internalQ == null) {
            outputArea.setText("⚠ Please route first!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        Stack<AnalystInfo> resolvedStack = new Stack<>();

        while (!internalQ.isEmpty() || !externalQ.isEmpty() || !criticalQ.isEmpty()) {
            processBatch(internalQ, resolvedStack, 5);
            processBatch(externalQ, resolvedStack, 5);
            processBatch(criticalQ, resolvedStack, 5);
        }

        sb.append("\n===== RESOLVED STACK (LIFO) =====\n");

        while (!resolvedStack.isEmpty()) {
            AnalystInfo a = resolvedStack.pop();

            sb.append("\n ").append(a.getAnalystName());
            sb.append("\n ").append(a.getExpertiseArea());

            double total = 0;
            sb.append("\n Incidents: ");

            for (IncidentInfo i : a.getIncidents()) {
                sb.append(i.getIncidentId()).append(" ");
                total += i.getImpactCost();
            }

            sb.append("\n Total Cost: RM ").append(String.format("%,.2f", total));
            sb.append("\n-------------------------\n");
        }

        outputArea.setText(sb.toString());
    }

    private void processBatch(Queue<AnalystInfo> q, Stack<AnalystInfo> s, int size) {
        for (int i = 0; i < size; i++) {
            if (!q.isEmpty()) {
                s.push(q.poll());
            }
        }
    }

    public static void main(String[] args) {
        new IncidentGUI().setVisible(true);
    }
}
