package department.hibernate.gui;

import department.hibernate.Client;
import department.hibernate.Project;
import department.hibernate.Task;
import department.hibernate.Worker;
import department.hibernate.services.*;
import org.hibernate.Hibernate;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.plaf.basic.CalendarHeaderHandler;
import org.jdesktop.swingx.plaf.basic.SpinningCalendarHeaderHandler;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainInterface {
    private JFrame Frame;
    private JToolBar toolBar;
    private JButton add;
    private JButton edit;
    private JButton remove;
    private JButton info;
    private JButton report;
    private JButton addWorkerToProject;
    private JButton addTaskToWorker;
    private JTextField searchField;
    private JTabbedPane tabbedPanel;
    private JPanel projectsPanel;
    private JPanel workersPanel;
    private JPanel clientsPanel;
    private JPanel tasksPanel;
    private JTable projectsTable;
    private JTable workersTable;
    private JTable clientsTable;
    private JTable tasksTable;
    private JScrollPane projectsScroll;
    private JScrollPane workersScroll;
    private JScrollPane clientsScroll;
    private JScrollPane tasksScroll;
    private TableRowSorter<TableModel> projectsSorter;
    private TableRowSorter<TableModel> workersSorter;
    private TableRowSorter<TableModel> clientsSorter;
    private TableRowSorter<TableModel> tasksSorter;

    public boolean confirmDialog() {
        int getInfo = JOptionPane.showConfirmDialog(null, "Confirm operation?");
        boolean check;

        if (getInfo == 0) check = true;
        else check = false;

        return check;
    }

    public boolean checkSelection(JTable table) {
        boolean check;
        if (table.getSelectedRow() == -1) {
            check = false;
            JOptionPane.showMessageDialog(Frame, "Select row", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else check = true;
        return check;
    }

    public void show() {
        ProjectService projectService = new ProjectService();
        WorkerService workerService = new WorkerService();
        ClientService clientService = new ClientService();
        TaskService taskService = new TaskService();

        Frame = new JFrame("Department");
        Frame.setResizable(false);
        Frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        toolBar = new JToolBar();
        // Create buttons for toolbar
        add = new JButton(new ImageIcon("src/main/resources/pic/add.png"));
        edit = new JButton(new ImageIcon("src/main/resources/pic/edit.png"));
        remove = new JButton(new ImageIcon("src/main/resources/pic/remove.png"));
        info = new JButton(new ImageIcon("src/main/resources/pic/info.png"));
        report = new JButton(new ImageIcon("src/main/resources/pic/report.png"));

        // Create Tabbed panel and Tabs
        tabbedPanel = new JTabbedPane();
        projectsPanel = new JPanel();
        workersPanel = new JPanel();
        clientsPanel = new JPanel();
        tasksPanel = new JPanel();

        // Projects Tab
        projectsTable = new JTable();
        projectsScroll = new JScrollPane();

        // Workers Tab
        workersTable = new JTable();
        workersScroll = new JScrollPane();
        addWorkerToProject = new JButton("Connect");
        addWorkerToProject.setToolTipText("Connect user to project");

        // Clients Tab
        clientsTable = new JTable();
        clientsScroll = new JScrollPane();

        // Tasks Tab
        tasksTable = new JTable();
        tasksScroll = new JScrollPane();
        addTaskToWorker = new JButton("Connect");
        addTaskToWorker.setToolTipText("Connect task to worker");

        // Create search field
        searchField = new JTextField();

        // Tips for buttons in toolbar
        add.setToolTipText("Add");
        edit.setToolTipText("Change");
        remove.setToolTipText("Delete");
        info.setToolTipText("Info");
        report.setToolTipText("Report");

        // Complete toolbar
        toolBar = new JToolBar("Tool bar");
        toolBar.add(add);
        toolBar.add(edit);
        toolBar.add(remove);
        toolBar.add(info);
        toolBar.add(report);
        toolBar.setFloatable(false); // Off movement
        toolBar.addSeparator();

        // Layout of Projects tab
        GroupLayout projectsLayout = new GroupLayout(projectsPanel);
        projectsPanel.setLayout(projectsLayout);
        projectsLayout.setHorizontalGroup(
                projectsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(projectsLayout.createSequentialGroup()
                                .addComponent(projectsScroll)
                        )
        );
        projectsLayout.setVerticalGroup(
                projectsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(projectsLayout.createSequentialGroup()
                                .addComponent(projectsScroll)
                        )
        );
        // Layout of Workers tab
        GroupLayout workersLayout = new GroupLayout(workersPanel);
        workersPanel.setLayout(workersLayout);
        workersLayout.setHorizontalGroup(
                workersLayout.createParallelGroup()
                        .addComponent(workersScroll)
        );
        workersLayout.setVerticalGroup(
                workersLayout.createSequentialGroup()
                        .addComponent(workersScroll)
        );
        // Layout of Clients tab
        GroupLayout clientsLayout = new GroupLayout(clientsPanel);
        clientsPanel.setLayout(clientsLayout);
        clientsLayout.setHorizontalGroup(
                clientsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(clientsLayout.createSequentialGroup()
                                .addComponent(clientsScroll)
                        )
        );
        clientsLayout.setVerticalGroup(
                clientsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(clientsLayout.createSequentialGroup()
                                .addComponent(clientsScroll)
                        )
        );
        // Layout of Tasks tab
        GroupLayout tasksLayout = new GroupLayout(tasksPanel);
        tasksPanel.setLayout(tasksLayout);
        tasksLayout.setHorizontalGroup(
                tasksLayout.createParallelGroup()
                        .addComponent(tasksScroll)
        );
        tasksLayout.setVerticalGroup(
                tasksLayout.createSequentialGroup()
                        .addComponent(tasksScroll)
        );

        tabbedPanel.addTab("Projects", projectsPanel);
        tabbedPanel.addTab("Workers", workersPanel);
        tabbedPanel.addTab("Clients", clientsPanel);
        tabbedPanel.addTab("Tasks", tasksPanel);

        tableDesign();

        // Layout of all tabs
        GroupLayout layout = new GroupLayout(Frame.getContentPane());
        Frame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(toolBar, 150, 150, 150)
                        .addComponent(searchField, 300, 300, 300)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(tabbedPanel, 600, 600, 600)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(toolBar)
                        .addComponent(searchField, 32, 32, 32)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(tabbedPanel, 500, 500, 500)
                )
        );

        searchTab(projectsSorter); // Set search in Projects tab
        // When user change tab - search start working in this tab
        tabbedPanel.addChangeListener(e -> {
            if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Projects")) searchTab(projectsSorter);
            else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Workers")) searchTab(workersSorter);
            else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Clients")) searchTab(clientsSorter);
            else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Tasks")) searchTab(tasksSorter);
        });

        add.addActionListener(e -> {
            if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Projects")) {
                addProjectInterface();
            } else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Workers")) {
                addWorkerInterface();
            }else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Clients")) {
                addClientInterface();
            } else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Tasks")) {
                addTaskInterface();
            }
        });

        edit.addActionListener(e -> {
            if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Projects")) {
                if (projectsTable.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(Frame, "Select row", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        editProjectInterface(projectsTable.getSelectedRow());
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Workers")) {
                if (workersTable.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(Frame, "Select row", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        editWorkerInterface(workersTable.getSelectedRow());
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Clients")) {
                if (clientsTable.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(Frame, "Select row", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    editClientInterface(clientsTable.getSelectedRow());
                }
            } else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Tasks")) {
                if (tasksTable.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(Frame, "Select row", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    editTaskInterface(tasksTable.getSelectedRow());
                }
            }
        });

        remove.addActionListener(e -> {
            if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Projects")) {
                if (checkSelection(projectsTable)) {
                    if (confirmDialog()) {
                        Project project = projectService
                                .getProject((int)projectsTable.getValueAt(projectsTable.getSelectedRow(), 0));
                        projectService.deleteProject(project);
                        tableDesign();
                    }
                }
            } else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Workers")) {
                if (checkSelection(workersTable)) {
                    if (confirmDialog()) {
                        Worker worker = workerService
                                .getWorker((int)workersTable.getValueAt(workersTable.getSelectedRow(), 0));
                        workerService.deleteWorker(worker);
                        tableDesign();
                    }
                }
            } else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Clients")) {
                if (checkSelection(clientsTable)) {
                    if (confirmDialog()) {
                        Client client = clientService
                                .getClient((int)clientsTable.getValueAt(clientsTable.getSelectedRow(), 0));
                        clientService.deleteClient(client);
                        tableDesign();
                    }
                }
            } else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Tasks")) {
                if (checkSelection(tasksTable)) {
                    if (confirmDialog()) {
                        Task task = taskService
                                .getTask((int)tasksTable.getValueAt(tasksTable.getSelectedRow(), 0));
                        taskService.deleteTask(task);
                        tableDesign();
                    }
                }
            }

        });

        info.addActionListener(e ->{
            if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Projects")) {
                if (checkSelection(projectsTable)) {
                    infoProjectInterface(projectService
                            .getProject((int)projectsTable.getValueAt(projectsTable.getSelectedRow(), 0)));
                }
            } else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Workers")) {
                if (checkSelection(workersTable)) {
                    infoWorkerInterface(workerService
                            .getWorker((int)workersTable.getValueAt(workersTable.getSelectedRow(), 0)));
                }
            } else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Clients")) {
                if (checkSelection(clientsTable)) {
                    infoClientInterface(clientService
                            .getClient((int)clientsTable.getValueAt(clientsTable.getSelectedRow(), 0)));
                }
            } else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Tasks")) {
                if (checkSelection(tasksTable)) {
                    infoTaskInterface(taskService
                            .getTask((int)tasksTable.getValueAt(tasksTable.getSelectedRow(), 0)));
                }
            }
        });

        Frame.pack();
        Frame.setVisible(true);
    }

    public void tableDesign() {
        // Get tables with actual data
        projectsTable = new TableService().getProjectsTable();
        workersTable = new TableService().getWorkersTable();
        clientsTable = new TableService().getClientsTable();
        tasksTable = new TableService().getTasksTable();

        // Select only 1 row
        projectsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        workersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tasksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Connect tables to scroll panels
        projectsScroll.setViewportView(projectsTable);
        workersScroll.setViewportView(workersTable);
        clientsScroll.setViewportView(clientsTable);
        tasksScroll.setViewportView(tasksTable);

        // Search in any table
        projectsSorter = new TableRowSorter<>(projectsTable.getModel());
        projectsTable.setRowSorter(projectsSorter);
        workersSorter = new TableRowSorter<>(workersTable.getModel());
        workersTable.setRowSorter(workersSorter);
        clientsSorter = new TableRowSorter<>(clientsTable.getModel());
        clientsTable.setRowSorter(clientsSorter);
        tasksSorter = new TableRowSorter<>(tasksTable.getModel());
        tasksTable.setRowSorter(tasksSorter);

        // Set where search is on
        if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Projects")) searchTab(projectsSorter);
        else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Workers")) searchTab(workersSorter);
        else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Clients")) searchTab(clientsSorter);
        else if (tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()).equals("Tasks")) searchTab(tasksSorter);

        // Set sizes of table
        projectsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        projectsTable.getColumnModel().getColumn(0).setPreferredWidth(25); // ID
        projectsTable.getColumnModel().getColumn(1).setPreferredWidth(140); // Name
        projectsTable.getColumnModel().getColumn(2).setPreferredWidth(65); // Deadline
        projectsTable.getColumnModel().getColumn(3).setPreferredWidth(60); // Status
        projectsTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Client

        workersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        workersTable.getColumnModel().getColumn(0).setPreferredWidth(25); // ID
        workersTable.getColumnModel().getColumn(1).setPreferredWidth(140); // Name
        workersTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Date of Birth
        //workersTable.getColumnModel().getColumn(3).setPreferredWidth(0);
        workersTable.getColumnModel().removeColumn(workersTable.getColumnModel().getColumn(3));

        clientsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        clientsTable.getColumnModel().getColumn(0).setPreferredWidth(25); // ID
        clientsTable.getColumnModel().getColumn(1).setPreferredWidth(140); // Name
        clientsTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Address
        clientsTable.getColumnModel().getColumn(3).setPreferredWidth(80); // Type
        //clientsTable.getColumnModel().getColumn(4).setPreferredWidth(0);
        clientsTable.getColumnModel().removeColumn(clientsTable.getColumnModel().getColumn(4));

        tasksTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tasksTable.getColumnModel().getColumn(0).setPreferredWidth(25); // ID
        tasksTable.getColumnModel().getColumn(1).setPreferredWidth(140); // Name
        tasksTable.getColumnModel().getColumn(2).setPreferredWidth(65); // Status
        tasksTable.getColumnModel().getColumn(3).setPreferredWidth(140); // Project
        tasksTable.getColumnModel().getColumn(4).setPreferredWidth(140); // Worker
    }

    // Get model of table where search is on
    public void searchTab(TableRowSorter<TableModel> sorter) {
        searchField.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = searchField.getText();

                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = searchField.getText();

                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    public void addProjectInterface() {
        ProjectService projectService = new ProjectService();
        ClientService clientService = new ClientService();

        JDialog addFrame = new JDialog(Frame, "Add Task");
        addFrame.setResizable(false);
        addFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        List<Client> clients = clientService.getClients();
        List<Project> projects = projectService.getProjects();

        ArrayList<String> arrayClients = new ArrayList<String>();

        for (Client client : clients) {
            arrayClients.add(client.getName());
        }


        UIManager.put(CalendarHeaderHandler.uiControllerID,
                "org.jdesktop.swingx.plaf.basic.SpinningCalendarHeaderHandler");
        UIManager.put(SpinningCalendarHeaderHandler.ARROWS_SURROUND_MONTH, Boolean.TRUE);

        JXDatePicker timeSet = new JXDatePicker();
        JXMonthView monthView = timeSet.getMonthView();
        timeSet.setDate(Calendar.getInstance().getTime());
        timeSet.getMonthView().setLowerBound(new Date());
        timeSet.getMonthView().setUpperBound(new Date(3153600000000L));
        timeSet.getMonthView().setZoomable(true);
        SpinningCalendarHeaderHandler handler = new SpinningCalendarHeaderHandler();
        handler.install(monthView);
        timeSet.getEditor().setEditable(false);
        timeSet.setFormats(new SimpleDateFormat("dd.MM.yyyy"));


        JTextField nameField = new JTextField();

        JTextArea additionalField = new JTextArea(5, 35);
        additionalField.setLineWrap(true);
        additionalField.setWrapStyleWord(true);
        JScrollPane additionalPanel = new JScrollPane(additionalField);

        JComboBox clientCombo = new JComboBox(arrayClients.toArray());
        clientCombo.setEditable(false);

        JLabel nameLabel = new JLabel("Name: ");
        JLabel additionalLabel = new JLabel("Additional: ");
        JLabel deadlineLabel = new JLabel("Deadline: ");
        JLabel clientLabel = new JLabel("Client: ");

        JButton saveAddProject = new JButton("Save");
        JButton cancelAddProject = new JButton("Cancel");

        GroupLayout layout = new GroupLayout(addFrame.getContentPane());
        addFrame.getContentPane().setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(nameLabel, 120, 120, 120)
                        .addComponent(nameField, 200, 200, 200)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(additionalLabel, 120, 120, 120)
                        .addComponent(additionalPanel, 250, 250, 250)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(deadlineLabel, 120, 120, 120)
                        .addComponent(timeSet, 150, 150, 150)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(clientLabel, 120, 120, 120)
                        .addComponent(clientCombo, 150, 150, 150)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(205)
                        .addComponent(cancelAddProject, 80, 80, 80)
                        .addGap(10)
                        .addComponent(saveAddProject, 80, 80, 80)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel, 30, 30, 30)
                        .addComponent(nameField, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(additionalLabel, 30, 30, 30)
                        .addComponent(additionalPanel, 90, 90, 90)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(deadlineLabel, 30, 30, 30)
                        .addComponent(timeSet, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(clientLabel, 30, 30, 30)
                        .addComponent(clientCombo, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(cancelAddProject, 30, 30, 30)
                        .addComponent(saveAddProject, 30, 30, 30)
                )
                .addGap(5)
        );

        saveAddProject.addActionListener(e -> {
            String name = nameField.getText();
            String additional = additionalField.getText();
            Client client = clients.get(clientCombo.getSelectedIndex());

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(addFrame, "Enter name", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (additional.isEmpty()) {
                JOptionPane.showMessageDialog(addFrame, "Describe the project", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (projects.stream().anyMatch(t -> (t.getName().equals(name)))) {
                JOptionPane.showMessageDialog(addFrame, "Project with that name already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (name.length() >= 64) {
                JOptionPane.showMessageDialog(addFrame, "It is too long name (>64)", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (additional.length() >= 512) {
                JOptionPane.showMessageDialog(addFrame, "It is too long address (>512)", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (timeSet.getDate() == null) {
                JOptionPane.showMessageDialog(addFrame, "Not allowed date", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                String deadline = (new SimpleDateFormat("dd.MM.yyyy")).format(timeSet.getDate());
                Project project = new Project(name, deadline, additional);
                project.setClient(client);
                project.updateStatus();

                projectService.addProject(project);
                clientService.updateClient(client);

                tableDesign();

                addFrame.dispose();
            }
        });

        cancelAddProject.addActionListener(e -> {
            addFrame.dispose();
        });

        addFrame.pack();
        addFrame.setVisible(true);
    }

    public void addWorkerInterface() {
        WorkerService workerService = new WorkerService();

        JDialog addFrame = new JDialog(Frame, "Add Worker");
        addFrame.setResizable(false);
        addFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        List<Worker> workers = workerService.getWorkers();


        UIManager.put(CalendarHeaderHandler.uiControllerID,
                      "org.jdesktop.swingx.plaf.basic.SpinningCalendarHeaderHandler");
        UIManager.put(SpinningCalendarHeaderHandler.ARROWS_SURROUND_MONTH, Boolean.TRUE);

        JXDatePicker timeSet = new JXDatePicker();
        JXMonthView monthView = timeSet.getMonthView();
        timeSet.setDate(Calendar.getInstance().getTime());
        timeSet.getMonthView().setLowerBound(new Date(0));
        timeSet.getMonthView().setUpperBound(new Date());
        timeSet.getMonthView().setZoomable(true);
        SpinningCalendarHeaderHandler handler = new SpinningCalendarHeaderHandler();
        handler.install(monthView);
        timeSet.getEditor().setEditable(false);
        timeSet.setFormats(new SimpleDateFormat("dd.MM.yyyy"));


        JTextField nameField = new JTextField();
        JTextField surnameField = new JTextField();
        JTextArea additionalField = new JTextArea(5, 35);
        additionalField.setLineWrap(true);
        additionalField.setWrapStyleWord(true);
        JScrollPane additionalPanel = new JScrollPane(additionalField);

        JLabel nameLabel = new JLabel("Name: ");
        JLabel surnameLabel = new JLabel("Surname: ");
        JLabel additionalLabel = new JLabel("Additional: ");
        JLabel timeLabel = new JLabel("Date of birth: ");

        JButton saveAddWorker = new JButton("Save");
        JButton cancelAddWorker = new JButton("Cancel");

        GroupLayout layout = new GroupLayout(addFrame.getContentPane());
        addFrame.getContentPane().setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(nameLabel, 120, 120, 120)
                        .addComponent(nameField, 150, 150, 150)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(surnameLabel, 120, 120, 120)
                        .addComponent(surnameField, 150, 150, 150)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(additionalLabel, 120, 120, 120)
                        .addComponent(additionalPanel, 250, 250, 250)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(timeLabel, 120, 120, 120)
                        .addComponent(timeSet, 150, 150, 150)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(205)
                        .addComponent(cancelAddWorker, 80, 80, 80)
                        .addGap(10)
                        .addComponent(saveAddWorker, 80, 80, 80)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel, 30, 30, 30)
                        .addComponent(nameField, 30, 30, 30)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(surnameLabel, 30, 30, 30)
                        .addComponent(surnameField, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(additionalLabel, 30, 30, 30)
                        .addComponent(additionalPanel, 90, 90, 90)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(timeLabel, 30, 30, 30)
                        .addComponent(timeSet, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(cancelAddWorker, 30, 30, 30)
                        .addComponent(saveAddWorker, 30, 30, 30)
                )
                .addGap(5)
        );

        saveAddWorker.addActionListener(e -> {
            String name = nameField.getText();
            String surname = surnameField.getText();
            String additional = additionalField.getText();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(addFrame, "Enter name", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (surname.isEmpty()) {
                JOptionPane.showMessageDialog(addFrame, "Enter surname", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (workers.stream().anyMatch(t -> (t.getName().equals(name)) & (t.getSurname().equals(name)))) {
                JOptionPane.showMessageDialog(addFrame, "That worker already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (name.length() >= 32) {
                JOptionPane.showMessageDialog(addFrame, "It is too long name (>32)", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (surname.length() >= 32) {
                JOptionPane.showMessageDialog(addFrame, "It is too long surname (>32)", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (timeSet.getDate() == null) {
                JOptionPane.showMessageDialog(addFrame, "Not allowed date", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                String dateOfBirth = (new SimpleDateFormat("dd.MM.yyyy")).format(timeSet.getDate());

                Worker worker = new Worker(name, surname, dateOfBirth, additional);

                workerService.addWorker(worker);

                tableDesign();

                addFrame.dispose();
            }
        });

        cancelAddWorker.addActionListener(e -> {
            addFrame.dispose();
        });

        addFrame.pack();
        addFrame.setVisible(true);
    }

    public void addClientInterface() {
        ClientService clientService = new ClientService();

        JDialog addFrame = new JDialog(Frame, "Add Client");
        addFrame.setResizable(false);
        addFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        List<Client> clients = clientService.getClients();

        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();

        JComboBox typeCombo = new JComboBox(new String[]{"Private", "Government"});
        typeCombo.setEditable(false);

        JLabel nameLabel = new JLabel("Name: ");
        JLabel addressLabel = new JLabel("Address: ");
        JLabel typeLabel = new JLabel("Company type: ");

        JButton saveAddClient = new JButton("Save");
        JButton cancelAddClient = new JButton("Cancel");

        GroupLayout layout = new GroupLayout(addFrame.getContentPane());
        addFrame.getContentPane().setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(nameLabel, 120, 120, 120)
                        .addComponent(nameField, 200, 200, 200)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(addressLabel, 120, 120, 120)
                        .addComponent(addressField, 300, 300, 300)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(typeLabel, 120, 120, 120)
                        .addComponent(typeCombo, 100, 100, 100)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(205)
                        .addComponent(cancelAddClient, 80, 80, 80)
                        .addGap(10)
                        .addComponent(saveAddClient, 80, 80, 80)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel, 30, 30, 30)
                        .addComponent(nameField, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addressLabel, 30, 30, 30)
                        .addComponent(addressField, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(typeLabel, 30, 30, 30)
                        .addComponent(typeCombo, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(cancelAddClient, 30, 30, 30)
                        .addComponent(saveAddClient, 30, 30, 30)
                )
                .addGap(5)
        );

        saveAddClient.addActionListener(e -> {
            String name = nameField.getText();
            String address = addressField.getText();
            String type = typeCombo.getSelectedItem().toString();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(addFrame, "Enter name", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (address.isEmpty()) {
                JOptionPane.showMessageDialog(addFrame, "Enter address", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (clients.stream().anyMatch(t -> (t.getName().equals(name)) & (t.getType().equals(type)))) {
                JOptionPane.showMessageDialog(addFrame, "That client already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (name.length() >= 64) {
                JOptionPane.showMessageDialog(addFrame, "It is too long name (>64)", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (address.length() >= 128) {
                JOptionPane.showMessageDialog(addFrame, "It is too long address (>128)", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                Client client = new Client(name, address, type);

                clientService.addClient(client);

                tableDesign();

                addFrame.dispose();
            }
        });

        cancelAddClient.addActionListener(e -> {
            addFrame.dispose();
        });

        addFrame.pack();
        addFrame.setVisible(true);
    }

    public void addTaskInterface() {
        TaskService taskService = new TaskService();
        ProjectService projectService = new ProjectService();
        WorkerService workerService = new WorkerService();

        JDialog addFrame = new JDialog(Frame, "Add Task");
        addFrame.setResizable(false);
        addFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        List<Task> tasks = taskService.getTasks();
        List<Worker> workers = workerService.getWorkers();
        List<Project> projects = projectService.getProjects();

        ArrayList<String> arrayProjects = new ArrayList<String>();
        ArrayList<String> arrayWorkers = new ArrayList<String>();

        for (Project project : projects) {
            arrayProjects.add(project.getName());
        }
        for (Worker worker : workers) {
            arrayWorkers.add(worker.getName() + " " + worker.getSurname());
        }

        JTextField nameField = new JTextField();

        JTextArea additionalField = new JTextArea(5, 35);
        additionalField.setLineWrap(true);
        additionalField.setWrapStyleWord(true);
        JScrollPane additionalPanel = new JScrollPane(additionalField);

        JComboBox statusCombo = new JComboBox(new String[]{"Done", "Not done"});
        statusCombo.setEditable(false);
        JComboBox projectCombo = new JComboBox(arrayProjects.toArray());
        projectCombo.setEditable(false);
        JComboBox workerCombo = new JComboBox(arrayWorkers.toArray());
        workerCombo.setEditable(false);

        JLabel nameLabel = new JLabel("Name: ");
        JLabel additionalLabel = new JLabel("Additional: ");
        JLabel statusLabel = new JLabel("Status: ");
        JLabel projectLabel = new JLabel("Project: ");
        JLabel workerLabel = new JLabel("Worker: ");

        JButton saveAddTask = new JButton("Save");
        JButton cancelAddTask = new JButton("Cancel");

        GroupLayout layout = new GroupLayout(addFrame.getContentPane());
        addFrame.getContentPane().setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(nameLabel, 120, 120, 120)
                        .addComponent(nameField, 200, 200, 200)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(additionalLabel, 120, 120, 120)
                        .addComponent(additionalPanel, 300, 300, 300)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(statusLabel, 120, 120, 120)
                        .addComponent(statusCombo, 100, 100, 100)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(projectLabel, 120, 120, 120)
                        .addComponent(projectCombo, 150, 150, 150)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(workerLabel, 120, 120, 120)
                        .addComponent(workerCombo, 150, 150, 150)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(205)
                        .addComponent(cancelAddTask, 80, 80, 80)
                        .addGap(10)
                        .addComponent(saveAddTask, 80, 80, 80)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel, 30, 30, 30)
                        .addComponent(nameField, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(additionalLabel, 30, 30, 30)
                        .addComponent(additionalPanel, 90, 90, 90)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(statusLabel, 30, 30, 30)
                        .addComponent(statusCombo, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(projectLabel, 30, 30, 30)
                        .addComponent(projectCombo, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(workerLabel, 30, 30, 30)
                        .addComponent(workerCombo, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(cancelAddTask, 30, 30, 30)
                        .addComponent(saveAddTask, 30, 30, 30)
                )
                .addGap(5)
        );

        saveAddTask.addActionListener(e -> {
            String name = nameField.getText();
            String additional = additionalField.getText();
            String status = statusCombo.getSelectedItem().toString();
            Project project = projects.get(projectCombo.getSelectedIndex());
            Worker worker = workers.get(workerCombo.getSelectedIndex());

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(addFrame, "Enter name", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (additional.isEmpty()) {
                JOptionPane.showMessageDialog(addFrame, "Describe the task", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (tasks.stream().anyMatch(t -> (t.getName().equals(name)))) {
                JOptionPane.showMessageDialog(addFrame, "Task with that name already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (name.length() >= 64) {
                JOptionPane.showMessageDialog(addFrame, "It is too long name (>64)", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (additional.length() >= 512) {
                JOptionPane.showMessageDialog(addFrame, "It is too long address (>512)", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                Task task = new Task(name, status, additional);
//                if (!project.getWorkers().stream().noneMatch(t -> (t.equals(worker)))) {
//                    project.addWorker(worker);
//                }
                project.addTask(task);
                worker.addTask(task);

                taskService.addTask(task);

                projectService.updateProject(project);
                workerService.updateWorker(worker);

                tableDesign();

                addFrame.dispose();
            }
        });

        cancelAddTask.addActionListener(e -> {
            addFrame.dispose();
        });

        addFrame.pack();
        addFrame.setVisible(true);
    }

    public void editProjectInterface(int row) throws ParseException {
        ProjectService projectService = new ProjectService();

        Project project = projectService.getProject((int)projectsTable.getValueAt(row, 0));

        JDialog editFrame = new JDialog(Frame, "Edit Project");
        editFrame.setResizable(false);
        editFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        List<Project> projects = projectService.getProjects();


        UIManager.put(CalendarHeaderHandler.uiControllerID,
                "org.jdesktop.swingx.plaf.basic.SpinningCalendarHeaderHandler");
        UIManager.put(SpinningCalendarHeaderHandler.ARROWS_SURROUND_MONTH, Boolean.TRUE);

        JXDatePicker timeSet = new JXDatePicker();
        JXMonthView monthView = timeSet.getMonthView();
        timeSet.setDate(Calendar.getInstance().getTime());
        timeSet.getMonthView().setLowerBound(new Date());
        timeSet.getMonthView().setUpperBound(new Date(3153600000000L));
        timeSet.getMonthView().setZoomable(true);
        SpinningCalendarHeaderHandler handler = new SpinningCalendarHeaderHandler();
        handler.install(monthView);
        timeSet.getEditor().setEditable(false);
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        timeSet.setFormats(format);
        timeSet.setDate(format.parse(project.getDeadline()));


        JTextField nameField = new JTextField(project.getName());

        JTextArea additionalField = new JTextArea(project.getAdditional(),5, 35);
        additionalField.setLineWrap(true);
        additionalField.setWrapStyleWord(true);
        JScrollPane additionalPanel = new JScrollPane(additionalField);

        JLabel nameLabel = new JLabel("Name: ");
        JLabel additionalLabel = new JLabel("Additional: ");
        JLabel deadlineLabel = new JLabel("Deadline: ");

        JButton saveEditProject = new JButton("Save");
        JButton cancelEditProject = new JButton("Cancel");

        GroupLayout layout = new GroupLayout(editFrame.getContentPane());
        editFrame.getContentPane().setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(nameLabel, 120, 120, 120)
                        .addComponent(nameField, 200, 200, 200)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(additionalLabel, 120, 120, 120)
                        .addComponent(additionalPanel, 250, 250, 250)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(deadlineLabel, 120, 120, 120)
                        .addComponent(timeSet, 150, 150, 150)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(205)
                        .addComponent(cancelEditProject, 80, 80, 80)
                        .addGap(10)
                        .addComponent(saveEditProject, 80, 80, 80)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel, 30, 30, 30)
                        .addComponent(nameField, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(additionalLabel, 30, 30, 30)
                        .addComponent(additionalPanel, 90, 90, 90)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(deadlineLabel, 30, 30, 30)
                        .addComponent(timeSet, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(cancelEditProject, 30, 30, 30)
                        .addComponent(saveEditProject, 30, 30, 30)
                )
                .addGap(5)
        );

        saveEditProject.addActionListener(e -> {
            String name = nameField.getText();
            String additional = additionalField.getText();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(editFrame, "Enter name", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (additional.isEmpty()) {
                JOptionPane.showMessageDialog(editFrame, "Describe the project", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (projects.stream().anyMatch(t -> (t.getName().equals(name))) && !(project.getName().equals(name))) {
                JOptionPane.showMessageDialog(editFrame, "Project with that name already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (name.length() >= 64) {
                JOptionPane.showMessageDialog(editFrame, "It is too long name (>64)", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (additional.length() >= 512) {
                JOptionPane.showMessageDialog(editFrame, "It is too long address (>512)", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (timeSet.getDate() == null) {
                JOptionPane.showMessageDialog(editFrame, "Not allowed date", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                String deadline = (new SimpleDateFormat("dd.MM.yyyy")).format(timeSet.getDate());
                project.setName(name);
                project.setAdditional(additional);
                project.setDeadline(deadline);
                project.updateStatus();

                projectService.updateProject(project);

                tableDesign();

                editFrame.dispose();
            }
        });

        cancelEditProject.addActionListener(e -> {
            editFrame.dispose();
        });
        editFrame.pack();
        editFrame.setVisible(true);
    }

    public void editWorkerInterface(int row) throws ParseException {
        WorkerService workerService = new WorkerService();

        Worker worker = workerService.getWorker((int)workersTable.getValueAt(row, 0));

        JDialog editFrame = new JDialog(Frame, "Edit Worker");
        editFrame.setResizable(false);
        editFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        List<Worker> workers = workerService.getWorkers();


        UIManager.put(CalendarHeaderHandler.uiControllerID,
                "org.jdesktop.swingx.plaf.basic.SpinningCalendarHeaderHandler");
        UIManager.put(SpinningCalendarHeaderHandler.ARROWS_SURROUND_MONTH, Boolean.TRUE);

        JXDatePicker timeSet = new JXDatePicker();
        JXMonthView monthView = timeSet.getMonthView();
        timeSet.setDate(Calendar.getInstance().getTime());
        timeSet.getMonthView().setLowerBound(new Date(0));
        timeSet.getMonthView().setUpperBound(new Date());
        timeSet.getMonthView().setZoomable(true);
        SpinningCalendarHeaderHandler handler = new SpinningCalendarHeaderHandler();
        handler.install(monthView);
        timeSet.getEditor().setEditable(false);
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        timeSet.setFormats(format);
        timeSet.setDate(format.parse(worker.getDateOfBirth()));


        JTextField nameField = new JTextField(worker.getName());
        JTextField surnameField = new JTextField(worker.getSurname());
        JTextArea additionalField = new JTextArea(worker.getAdditional(),5, 35);
        additionalField.setLineWrap(true);
        additionalField.setWrapStyleWord(true);
        JScrollPane additionalPanel = new JScrollPane(additionalField);

        JLabel nameLabel = new JLabel("Name: ");
        JLabel surnameLabel = new JLabel("Surname: ");
        JLabel additionalLabel = new JLabel("Additional: ");
        JLabel dateofbirthLabel = new JLabel("Date of birth: ");

        JButton saveEditWorker = new JButton("Save");
        JButton cancelEditWorker = new JButton("Cancel");

        GroupLayout layout = new GroupLayout(editFrame.getContentPane());
        editFrame.getContentPane().setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(nameLabel, 120, 120, 120)
                        .addComponent(nameField, 200, 200, 200)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(surnameLabel, 120, 120, 120)
                        .addComponent(surnameField, 200, 200, 200)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(additionalLabel, 120, 120, 120)
                        .addComponent(additionalPanel, 250, 250, 250)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(dateofbirthLabel, 120, 120, 120)
                        .addComponent(timeSet, 150, 150, 150)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(205)
                        .addComponent(cancelEditWorker, 80, 80, 80)
                        .addGap(10)
                        .addComponent(saveEditWorker, 80, 80, 80)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel, 30, 30, 30)
                        .addComponent(nameField, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(surnameLabel, 30, 30, 30)
                        .addComponent(surnameField, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(additionalLabel, 30, 30, 30)
                        .addComponent(additionalPanel, 90, 90, 90)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(dateofbirthLabel, 30, 30, 30)
                        .addComponent(timeSet, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(cancelEditWorker, 30, 30, 30)
                        .addComponent(saveEditWorker, 30, 30, 30)
                )
                .addGap(5)
        );

        saveEditWorker.addActionListener(e -> {
            String name = nameField.getText();
            String surname = surnameField.getText();
            String additional = additionalField.getText();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(editFrame, "Enter name", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (surname.isEmpty()) {
                JOptionPane.showMessageDialog(editFrame, "Enter surname", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (additional.isEmpty()) {
                JOptionPane.showMessageDialog(editFrame, "Add information", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (workers.stream().anyMatch(t -> (t.getName().equals(name) && t.getSurname().equals(surname)))
                    && !(worker.getName().equals(name) && worker.getSurname().equals(surname))) {
                JOptionPane.showMessageDialog(editFrame, "Worker with that name already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (name.length() >= 32) {
                JOptionPane.showMessageDialog(editFrame, "It is too long name (>32)", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (surname.length() >= 32) {
                JOptionPane.showMessageDialog(editFrame, "It is too long surname (>32)", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (additional.length() >= 512) {
                JOptionPane.showMessageDialog(editFrame, "It is too long additional (>512)", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (timeSet.getDate() == null) {
                JOptionPane.showMessageDialog(editFrame, "Not allowed date", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                String date = (new SimpleDateFormat("dd.MM.yyyy")).format(timeSet.getDate());
                worker.setName(name);
                worker.setSurname(surname);
                worker.setDateOfBirth(date);
                worker.setAdditional(additional);

                workerService.updateWorker(worker);

                tableDesign();

                editFrame.dispose();
            }
        });

        cancelEditWorker.addActionListener(e -> {
            editFrame.dispose();
        });
        editFrame.pack();
        editFrame.setVisible(true);
    }

    public void editClientInterface(int row) {
        ClientService clientService = new ClientService();

        Client client = clientService.getClient((int)clientsTable.getValueAt(row, 0));

        JDialog editFrame = new JDialog(Frame, "Edit Client");
        editFrame.setResizable(false);
        editFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        List<Client> clients = clientService.getClients();

        JTextField nameField = new JTextField(client.getName());
        JTextField addressField = new JTextField(client.getAddress());

        JComboBox typeCombo = new JComboBox(new String[]{"Private", "Government"});
        typeCombo.setEditable(false);

        JLabel nameLabel = new JLabel("Name: ");
        JLabel addressLabel = new JLabel("Address: ");
        JLabel typeLabel = new JLabel("Company type: ");

        JButton saveEditClient = new JButton("Save");
        JButton cancelEditClient = new JButton("Cancel");

        GroupLayout layout = new GroupLayout(editFrame.getContentPane());
        editFrame.getContentPane().setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(nameLabel, 120, 120, 120)
                        .addComponent(nameField, 200, 200, 200)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(addressLabel, 120, 120, 120)
                        .addComponent(addressField, 200, 200, 200)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(typeLabel, 120, 120, 120)
                        .addComponent(typeCombo, 100, 100, 100)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(205)
                        .addComponent(cancelEditClient, 80, 80, 80)
                        .addGap(10)
                        .addComponent(saveEditClient, 80, 80, 80)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel, 30, 30, 30)
                        .addComponent(nameField, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addressLabel, 30, 30, 30)
                        .addComponent(addressField, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(typeLabel, 30, 30, 30)
                        .addComponent(typeCombo, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(cancelEditClient, 30, 30, 30)
                        .addComponent(saveEditClient, 30, 30, 30)
                )
                .addGap(5)
        );

        saveEditClient.addActionListener(e -> {
            String name = nameField.getText();
            String address = addressField.getText();
            String type = typeCombo.getSelectedItem().toString();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(editFrame, "Enter name", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (address.isEmpty()) {
                JOptionPane.showMessageDialog(editFrame, "Enter address", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (clients.stream().anyMatch(t -> (t.getName().equals(name)) & (t.getType().equals(type)))) {
                JOptionPane.showMessageDialog(editFrame, "That client already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (name.length() >= 64) {
                JOptionPane.showMessageDialog(editFrame, "It is too long name (>64)", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (address.length() >= 128) {
                JOptionPane.showMessageDialog(editFrame, "It is too long address (>128)", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                client.setName(name);
                client.setAddress(address);
                client.setType(type);

                clientService.updateClient(client);

                tableDesign();

                editFrame.dispose();
            }
        });

        cancelEditClient.addActionListener(e -> {
            editFrame.dispose();
        });
        editFrame.pack();
        editFrame.setVisible(true);
    }

    public void editTaskInterface(int row) {
        TaskService taskService = new TaskService();
        ProjectService projectService = new ProjectService();
        WorkerService workerService = new WorkerService();

        Task task = taskService.getTask((int)tasksTable.getValueAt(row, 0));

        JDialog editFrame = new JDialog(Frame, "Edit Task");
        editFrame.setResizable(false);
        editFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        List<Task> tasks = taskService.getTasks();
        List<Worker> workers = workerService.getWorkers();

        ArrayList<String> arrayWorkers = new ArrayList<String>();

        for (Worker worker : workers) {
            arrayWorkers.add(worker.getName() + " " + worker.getSurname());
        }

        JTextField nameField = new JTextField(task.getName());

        JTextArea additionalField = new JTextArea(task.getAdditional(),5, 35);
        additionalField.setLineWrap(true);
        additionalField.setWrapStyleWord(true);
        JScrollPane additionalPanel = new JScrollPane(additionalField);

        JComboBox statusCombo = new JComboBox(new String[]{"Not done", "Done"});
        statusCombo.setEditable(false);
        JComboBox workerCombo = new JComboBox(arrayWorkers.toArray());
        workerCombo.setEditable(false);

        JLabel nameLabel = new JLabel("Name: ");
        JLabel additionalLabel = new JLabel("Additional: ");
        JLabel statusLabel = new JLabel("Status: ");
        JLabel workerLabel = new JLabel("Worker: ");

        JButton saveEditTask = new JButton("Save");
        JButton cancelEditTask = new JButton("Cancel");

        GroupLayout layout = new GroupLayout(editFrame.getContentPane());
        editFrame.getContentPane().setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(nameLabel, 120, 120, 120)
                        .addComponent(nameField, 200, 200, 200)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(additionalLabel, 120, 120, 120)
                        .addComponent(additionalPanel, 200, 200, 200)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(statusLabel, 120, 120, 120)
                        .addComponent(statusCombo, 250, 250, 250)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(workerLabel, 120, 120, 120)
                        .addComponent(workerCombo, 150, 150, 150)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(205)
                        .addComponent(cancelEditTask, 80, 80, 80)
                        .addGap(10)
                        .addComponent(saveEditTask, 80, 80, 80)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel, 30, 30, 30)
                        .addComponent(nameField, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(additionalLabel, 30, 30, 30)
                        .addComponent(additionalPanel, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(statusLabel, 30, 30, 30)
                        .addComponent(statusCombo, 90, 90, 90)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(workerLabel, 30, 30, 30)
                        .addComponent(workerCombo, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(cancelEditTask, 30, 30, 30)
                        .addComponent(saveEditTask, 30, 30, 30)
                )
                .addGap(5)
        );

        saveEditTask.addActionListener(e -> {
            String name = nameField.getText();
            String additional = additionalField.getText();
            String status = statusCombo.getSelectedItem().toString();
            Worker worker = workers.get(workerCombo.getSelectedIndex());

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(editFrame, "Enter name", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (additional.isEmpty()) {
                JOptionPane.showMessageDialog(editFrame, "Describe the task", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (tasks.stream().anyMatch(t -> (t.getName().equals(name)))) {
                JOptionPane.showMessageDialog(editFrame, "Task with that name already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (name.length() >= 64) {
                JOptionPane.showMessageDialog(editFrame, "It is too long name (>64)", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (additional.length() >= 512) {
                JOptionPane.showMessageDialog(editFrame, "It is too long address (>512)", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                task.setName(name);
                task.setAdditional(additional);
                task.setStatus(status);
                Project project = task.getProject();
//                if (project.getWorkers().stream().noneMatch(t -> (t.equals(worker)))) {
//                    project.addWorker(worker);
//                }

                taskService.updateTask(task);

                projectService.updateProject(project);
                workerService.updateWorker(worker);

                tableDesign();

                editFrame.dispose();
            }
        });

        cancelEditTask.addActionListener(e -> {
            editFrame.dispose();
        });
        editFrame.pack();
        editFrame.setVisible(true);
    }

    public void infoProjectInterface(Project project) {
        JDialog infoFrame = new JDialog(Frame, "About Project");
        infoFrame.setResizable(false);
        infoFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        String tasksAndWorkers = new String();
        for (Task task : project.getTasks()) {
            if (task.getWorker() != null) {
                tasksAndWorkers = tasksAndWorkers + "Task: " + task.getName() + "\n"
                        + "Worker: " + task.getWorker().getName() + " " + task.getWorker().getSurname() + "\n\n";
            }
            else {
                tasksAndWorkers = tasksAndWorkers + task.getName() + ": "
                        + "No worker\n";
            }
        }
        JLabel nameLabel = new JLabel("Name: ");
        JLabel additionalLabel = new JLabel("Additional: ");
        JLabel deadlineLabel = new JLabel("Deadline: ");
        JLabel statusLabel = new JLabel("Status: ");
        JLabel clientLabel = new JLabel("Client: ");
        JLabel tasksLabel = new JLabel("Tasks: ");

        JLabel nameProjectLabel = new JLabel(project.getName());

        JTextArea additionalProjectArea = new JTextArea(project.getAdditional(),20, 35);
        additionalProjectArea.setLineWrap(true);
        additionalProjectArea.setWrapStyleWord(true);
        additionalProjectArea.setEditable(false);
        JScrollPane additionalProjectPanel = new JScrollPane(additionalProjectArea);

        JLabel deadlineProjectLabel = new JLabel(project.getDeadline());
        JLabel statusProjectLabel = new JLabel(project.getStatus());
        JLabel clientProjectLabel = new JLabel(project.getClient().getName());

        JTextArea tasksProjectArea = new JTextArea(tasksAndWorkers,20, 35);
        tasksProjectArea.setLineWrap(true);
        tasksProjectArea.setWrapStyleWord(true);
        tasksProjectArea.setEditable(false);
        JScrollPane tasksProjectPanel = new JScrollPane(tasksProjectArea);

        JButton okInfoProject = new JButton("OK");

        GroupLayout layout = new GroupLayout(infoFrame.getContentPane());
        infoFrame.getContentPane().setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(nameLabel, 80, 80, 80)
                        .addComponent(nameProjectLabel, 200, 200, 200)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(additionalLabel, 80, 80, 80)
                        .addComponent(additionalProjectPanel, 300, 300, 300)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(deadlineLabel, 80, 80, 80)
                        .addComponent(deadlineProjectLabel, 200, 200, 200)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(statusLabel, 80, 80, 80)
                        .addComponent(statusProjectLabel, 150, 150, 150)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(clientLabel, 80, 80, 80)
                        .addComponent(clientProjectLabel, 150, 150, 150)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(tasksLabel, 80, 80, 80)
                        .addComponent(tasksProjectPanel, 300, 300, 300)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(155)
                        .addComponent(okInfoProject, 80, 80, 80)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel, 30, 30, 30)
                        .addComponent(nameProjectLabel, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(additionalLabel, 30, 30, 30)
                        .addComponent(additionalProjectPanel, 90, 90, 90)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(deadlineLabel, 30, 30, 30)
                        .addComponent(deadlineProjectLabel, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(statusLabel, 30, 30, 30)
                        .addComponent(statusProjectLabel, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(clientLabel, 30, 30, 30)
                        .addComponent(clientProjectLabel, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(tasksLabel, 30, 30, 30)
                        .addComponent(tasksProjectPanel, 90, 90, 90)
                )
                .addGap(10)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(okInfoProject, 30, 30, 30)
                )
                .addGap(10)
        );

        okInfoProject.addActionListener(e -> {
            infoFrame.dispose();
        });
        infoFrame.pack();
        infoFrame.setVisible(true);
    }

    public void infoWorkerInterface(Worker worker) {
        JDialog infoFrame = new JDialog(Frame, "About Worker");
        infoFrame.setResizable(false);
        infoFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        String tasksOfWorker = new String();
        List<Task> tasks = new TaskService().getTasks();
        for (Task task : tasks) {
            if (worker.getId() == task.getWorker().getId()) {
                tasksOfWorker = tasksOfWorker + "Project: " + task.getProject().getName() + "\n"
                        + "Task: " + task.getName() + "\n\n";
            }
        }

        JLabel nameLabel = new JLabel("Name: ");
        JLabel surnameLabel = new JLabel("Surname: ");
        JLabel dateLabel = new JLabel("Date of birth: ");
        JLabel additionalLabel = new JLabel("Additional: ");
        JLabel tasksLabel = new JLabel("Tasks: ");

        JLabel nameWorkerLabel = new JLabel(worker.getName());
        JLabel surnameWorkerLabel = new JLabel(worker.getSurname());
        JLabel dateWorkerLabel = new JLabel(worker.getDateOfBirth());

        JTextArea additionalWorkerArea = new JTextArea(worker.getAdditional(),20, 35);
        additionalWorkerArea.setLineWrap(true);
        additionalWorkerArea.setWrapStyleWord(true);
        additionalWorkerArea.setEditable(false);
        JScrollPane additionalWorkerPanel = new JScrollPane(additionalWorkerArea);

        JTextArea tasksWorkerArea = new JTextArea(tasksOfWorker,20, 35);
        tasksWorkerArea.setLineWrap(true);
        tasksWorkerArea.setWrapStyleWord(true);
        tasksWorkerArea.setEditable(false);
        JScrollPane tasksWorkerPanel = new JScrollPane(tasksWorkerArea);

        JButton okInfoWorker = new JButton("OK");

        GroupLayout layout = new GroupLayout(infoFrame.getContentPane());
        infoFrame.getContentPane().setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(nameLabel, 80, 80, 80)
                        .addComponent(nameWorkerLabel, 200, 200, 200)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(surnameLabel, 80, 80, 80)
                        .addComponent(surnameWorkerLabel, 300, 300, 300)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(dateLabel, 80, 80, 80)
                        .addComponent(dateWorkerLabel, 200, 200, 200)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(additionalLabel, 80, 80, 80)
                        .addComponent(additionalWorkerPanel, 300, 300, 300)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(tasksLabel, 80, 80, 80)
                        .addComponent(tasksWorkerPanel, 300, 300, 300)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(155)
                        .addComponent(okInfoWorker, 80, 80, 80)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel, 30, 30, 30)
                        .addComponent(nameWorkerLabel, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(surnameLabel, 30, 30, 30)
                        .addComponent(surnameWorkerLabel, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(dateLabel, 30, 30, 30)
                        .addComponent(dateWorkerLabel, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(additionalLabel, 30, 30, 30)
                        .addComponent(additionalWorkerPanel, 90, 90, 90)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(tasksLabel, 30, 30, 30)
                        .addComponent(tasksWorkerPanel, 90, 90, 90)
                )
                .addGap(10)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(okInfoWorker, 30, 30, 30)
                )
                .addGap(10)
        );

        okInfoWorker.addActionListener(e -> {
            infoFrame.dispose();
        });
        infoFrame.pack();
        infoFrame.setVisible(true);
    }

    public void infoClientInterface(Client client) {
        JDialog infoFrame = new JDialog(Frame, "About Client");
        infoFrame.setResizable(false);
        infoFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        String projectsOfClient = new String();
        for (Project project : client.getProjects()) {
            projectsOfClient = projectsOfClient + "Project: " + project.getName() + "\n";
        }

        JLabel nameLabel = new JLabel("Name: ");
        JLabel addressLabel = new JLabel("Address: ");
        JLabel typeLabel = new JLabel("Type of company: ");
        JLabel projectsLabel = new JLabel("Projects: ");

        JLabel nameClientLabel = new JLabel(client.getName());
        JLabel addressClientLabel = new JLabel(client.getAddress());
        JLabel typeClientLabel = new JLabel(client.getType());

        JTextArea projectsClientArea = new JTextArea(projectsOfClient,100, 35);
        projectsClientArea.setLineWrap(true);
        projectsClientArea.setWrapStyleWord(true);
        projectsClientArea.setEditable(false);
        JScrollPane projectsClientPanel = new JScrollPane(projectsClientArea);

        JButton okInfoClient = new JButton("OK");

        GroupLayout layout = new GroupLayout(infoFrame.getContentPane());
        infoFrame.getContentPane().setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(nameLabel, 110, 110, 110)
                        .addComponent(nameClientLabel, 200, 200, 200)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(addressLabel, 110, 110, 110)
                        .addComponent(addressClientLabel, 200, 200, 200)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(typeLabel, 110, 110, 110)
                        .addComponent(typeClientLabel, 200, 200, 200)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(projectsLabel, 100, 100, 100)
                        .addComponent(projectsClientPanel, 300, 300, 300)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(165)
                        .addComponent(okInfoClient, 80, 80, 80)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel, 30, 30, 30)
                        .addComponent(nameClientLabel, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addressLabel, 30, 30, 30)
                        .addComponent(addressClientLabel, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(typeLabel, 30, 30, 30)
                        .addComponent(typeClientLabel, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(projectsLabel, 30, 30, 30)
                        .addComponent(projectsClientPanel, 90, 90, 90)
                )
                .addGap(10)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(okInfoClient, 30, 30, 30)
                )
                .addGap(10)
        );

        okInfoClient.addActionListener(e -> {
            infoFrame.dispose();
        });
        infoFrame.pack();
        infoFrame.setVisible(true);
    }

    public void infoTaskInterface(Task task) {
        JDialog infoFrame = new JDialog(Frame, "About Task");
        infoFrame.setResizable(false);
        infoFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        JLabel nameLabel = new JLabel("Name: ");
        JLabel statusLabel = new JLabel("Status: ");
        JLabel projectLabel = new JLabel("Project: ");
        JLabel workerLabel = new JLabel("Worker: ");
        JLabel additionalLabel = new JLabel("Additional: ");

        JLabel nameTaskLabel = new JLabel(task.getName());
        JLabel statusTaskLabel = new JLabel(task.getStatus());
        JLabel projectTaskLabel = new JLabel(task.getProject().getName());
        JLabel workerTaskLabel = new JLabel(task.getWorker().getName() + " " + task.getWorker().getSurname());

        JTextArea additionalTaskArea = new JTextArea(task.getAdditional(),20, 35);
        additionalTaskArea.setLineWrap(true);
        additionalTaskArea.setWrapStyleWord(true);
        additionalTaskArea.setEditable(false);
        JScrollPane additionalTaskPanel = new JScrollPane(additionalTaskArea);

        JButton okInfoTask = new JButton("OK");

        GroupLayout layout = new GroupLayout(infoFrame.getContentPane());
        infoFrame.getContentPane().setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(nameLabel, 110, 110, 110)
                        .addComponent(nameTaskLabel, 200, 200, 200)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(statusLabel, 110, 110, 110)
                        .addComponent(statusTaskLabel, 200, 200, 200)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(projectLabel, 110, 110, 110)
                        .addComponent(projectTaskLabel, 200, 200, 200)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(workerLabel, 110, 110, 110)
                        .addComponent(workerTaskLabel, 200, 200, 200)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(additionalLabel, 100, 100, 100)
                        .addComponent(additionalTaskPanel, 300, 300, 300)
                        .addGap(5)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(165)
                        .addComponent(okInfoTask, 80, 80, 80)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel, 30, 30, 30)
                        .addComponent(nameTaskLabel, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(statusLabel, 30, 30, 30)
                        .addComponent(statusTaskLabel, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(projectLabel, 30, 30, 30)
                        .addComponent(projectTaskLabel, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(workerLabel, 30, 30, 30)
                        .addComponent(workerTaskLabel, 30, 30, 30)
                )
                .addGap(5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(additionalLabel, 30, 30, 30)
                        .addComponent(additionalTaskPanel, 90, 90, 90)
                )
                .addGap(10)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(okInfoTask, 30, 30, 30)
                )
                .addGap(10)
        );

        okInfoTask.addActionListener(e -> {
            infoFrame.dispose();
        });
        infoFrame.pack();
        infoFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new MainInterface().show();
    }
}
