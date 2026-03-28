
/**
 * Write a description of class GUI here.
 *
 * @author (SYAZWI DANIAL)
 * @version (26/3/2026)
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

public class IncidentGUI extends JFrame {

    private JTextArea outputArea;
    private JButton loadBtn, routeBtn, resolveBtn, clearBtn;

    private LinkedList<AnalystInfo> analystList = new LinkedList<>();
    private Queue<AnalystInfo> internalQ = new LinkedList<>();
    private Queue<AnalystInfo> externalQ = new LinkedList<>();
    private Queue<AnalystInfo> criticalQ = new LinkedList<>();

    DecimalFormat df = new DecimalFormat("#,###.00");

    public IncidentGUI() {
        setTitle("Cyber Incident Management System");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBackground(new Color(220,235,250));
        add(panel);

        JLabel title = new JLabel("Cybersecurity Incident Dashboard", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0,70,140));
        panel.add(title, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setBackground(new Color(245,245,245));

        JScrollPane scroll = new JScrollPane(outputArea);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(1,4,10,10));
        btnPanel.setBackground(new Color(220,235,250));

        loadBtn = new JButton("Load Data");
        routeBtn = new JButton("Show Queues");
        resolveBtn = new JButton("Process Stack");
        clearBtn = new JButton("Clear");

        btnPanel.add(loadBtn);
        btnPanel.add(routeBtn);
        btnPanel.add(resolveBtn);
        btnPanel.add(clearBtn);

        panel.add(btnPanel, BorderLayout.SOUTH);

        loadBtn.addActionListener(e -> loadData());
        routeBtn.addActionListener(e -> showQueues());
        resolveBtn.addActionListener(e -> processStack());
        clearBtn.addActionListener(e -> outputArea.setText(""));
    }

    // ===== LOAD =====
    private void loadData() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("cyber_incidents.txt"));
            analystList.clear();
            String line;

            while ((line = br.readLine()) != null) {
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

                IncidentInfo inc = new IncidentInfo(incId, type, severity, date, ert, cost);

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

                current.addIncident(inc);
            }

            br.close();
            outputArea.setText("✅ Data loaded successfully.\n");

        } catch (Exception e) {
            outputArea.setText("❌ Error loading file.");
        }
    }

    // ===== QUEUE =====
    private void showQueues() {

        internalQ.clear();
        externalQ.clear();
        criticalQ.clear();

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

        StringBuilder sb = new StringBuilder();

        displayQueue(sb, "INTERNAL QUEUE", internalQ);
        displayQueue(sb, "EXTERNAL QUEUE", externalQ);
        displayQueue(sb, "CRITICAL QUEUE", criticalQ);

        outputArea.setText(sb.toString());
    }

    private void displayQueue(StringBuilder sb, String title, Queue<AnalystInfo> q) {

        sb.append("\n==============================\n");
        sb.append("   ").append(title).append("\n");
        sb.append("==============================\n");

        int num = 1;

        for (AnalystInfo a : q) {

            sb.append("\n[").append(num++).append("] Analyst: ")
              .append(a.getAnalystName()).append("\n");

            sb.append("    Expertise: ").append(a.getExpertiseArea()).append("\n");

            double total = 0;
            int incNum = 1;

            for (IncidentInfo i : a.getIncidents()) {

                sb.append("    (").append(incNum++).append(") ")
                  .append(i.getIncidentId()).append(", ")
                  .append(i.getIncidentType()).append(", Sev:")
                  .append(i.getSeverityLevel()).append(", Date:")
                  .append(i.getReportDate()).append(", ERT:")
                  .append(i.getERT()).append("h, RM ")
                  .append(df.format(i.getImpactCost()))
                  .append("\n");

                total += i.getImpactCost();
            }

            sb.append("    Total Cost: RM ").append(df.format(total)).append("\n");
        }
    }

    // ===== STACK =====
    private void processStack() {

        Stack<AnalystInfo> stack = new Stack<>();

        while (!internalQ.isEmpty() || !externalQ.isEmpty() || !criticalQ.isEmpty()) {
            processBatch(internalQ, stack);
            processBatch(externalQ, stack);
            processBatch(criticalQ, stack);
        }

        StringBuilder sb = new StringBuilder();

        sb.append("\n==============================\n");
        sb.append("   RESOLVED STACK (LIFO)\n");
        sb.append("==============================\n");

        int num = 1;

        while (!stack.isEmpty()) {

            AnalystInfo a = stack.pop();

            sb.append("\n[").append(num++).append("] ")
              .append(a.getAnalystName()).append("\n");

            sb.append("    Expertise: ").append(a.getExpertiseArea()).append("\n");

            double total = 0;

            for (IncidentInfo i : a.getIncidents()) {

                sb.append("    - ").append(i.getIncidentId()).append(", ")
                  .append(i.getIncidentType()).append(", Sev:")
                  .append(i.getSeverityLevel()).append(", RM ")
                  .append(df.format(i.getImpactCost()))
                  .append("\n");

                total += i.getImpactCost();
            }

            sb.append("    Total Resolved: RM ").append(df.format(total)).append("\n");
        }

        outputArea.setText(sb.toString());
    }

    private void processBatch(Queue<AnalystInfo> q, Stack<AnalystInfo> s) {
        for (int i = 0; i < 5; i++) {
            if (!q.isEmpty()) {
                s.push(q.poll());
            }
        }
    }

    public static void main(String[] args) {
        new IncidentGUI().setVisible(true);
    }
}
