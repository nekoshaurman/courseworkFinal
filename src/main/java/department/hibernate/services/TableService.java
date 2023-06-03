package department.hibernate.services;

import department.hibernate.Client;
import department.hibernate.Project;
import department.hibernate.Task;
import department.hibernate.Worker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TableService {
    private JTable projectsTable;
    private JTable workersTable;
    private JTable clientsTable;
    private JTable tasksTable;
    private WorkerService workerService = new WorkerService();
    private ClientService clientService = new ClientService();
    private TaskService taskService = new TaskService();
    private ProjectService projectService = new ProjectService();

    public JTable getWorkersTable() {
        workersTable = new JTable();

        List<Worker> workers = workerService.getWorkers();

        Object [][] workersArray = new Object[workers.size()][4];
        for(int i = 0; i < workers.size(); i++) {
            workersArray[i][0] = workers.get(i).getId();
            workersArray[i][1] = workers.get(i).getName() + " " + workers.get(i).getSurname();
            workersArray[i][2] = workers.get(i).getDateOfBirth();
            workersArray[i][3] = workers.get(i).projectsToString();
        }

        workersTable.setModel(new DefaultTableModel(
                workersArray,
                new String [] {
                        "ID", "Name", "Date of birth", ""
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });

        return workersTable;
    }

    public JTable getProjectsTable() {
        projectsTable = new JTable();

        List<Project> projects = projectService.getProjects();

        Object [][] projectsArray = new Object[projects.size()][5];
        for(int i = 0; i < projects.size(); i++) {
            projects.get(i).updateStatus();
            projectsArray[i][0] = projects.get(i).getId();
            projectsArray[i][1] = projects.get(i).getName();
            projectsArray[i][2] = projects.get(i).getDeadline();
            projectsArray[i][3] = projects.get(i).getStatus();
            projectsArray[i][4] = projects.get(i).getClient().getName();
        }

        projectsTable.setModel(new DefaultTableModel(
                projectsArray,
                new String [] {
                        "ID", "Name", "Deadline", "Status", "Client"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        return projectsTable;
    }

    public JTable getClientsTable() {
        clientsTable = new JTable();

        List<Client> clients = clientService.getClients();

        Object [][] clientsArray = new Object[clients.size()][5];
        for(int i = 0; i < clients.size(); i++) {
            clientsArray[i][0] = clients.get(i).getId();
            clientsArray[i][1] = clients.get(i).getName();
            clientsArray[i][2] = clients.get(i).getAddress();
            clientsArray[i][3] = clients.get(i).getType();
            clientsArray[i][4] = clients.get(i).projectsToString();
        }

        clientsTable.setModel(new DefaultTableModel(
                clientsArray,
                new String [] {
                        "ID", "Name", "Address", "Type", ""
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });

        return clientsTable;
    }

    public JTable getTasksTable() {
        tasksTable = new JTable();

        List<Task> tasks = taskService.getTasks();

        Object [][] tasksArray = new Object[tasks.size()][5];
        for(int i = 0; i < tasks.size(); i++) {
            tasksArray[i][0] = tasks.get(i).getId();
            tasksArray[i][1] = tasks.get(i).getName();
            tasksArray[i][2] = tasks.get(i).getStatus();
            tasksArray[i][3] = tasks.get(i).getProject().getName();
            if(tasks.get(i).getWorker() != null){
                tasksArray[i][4] = tasks.get(i).getWorker().getName() + " " + tasks.get(i).getWorker().getSurname();
            }
            else tasksArray[i][4] = "No worker";
        }

        tasksTable.setModel(new DefaultTableModel(
                tasksArray,
                new String [] {
                        "ID", "Name", "Status", "Project", "Worker"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });

        return tasksTable;
    }

    public DefaultTableModel getWorkersModel() {
        return (DefaultTableModel) getWorkersTable().getModel();
    }

    public DefaultTableModel getProjectsModel() {
        return (DefaultTableModel) getProjectsTable().getModel();
    }

    public DefaultTableModel getClientsModel() {
        return (DefaultTableModel) getClientsTable().getModel();
    }

    public DefaultTableModel getTasksModel() {
        return (DefaultTableModel) getTasksTable().getModel();
    }
}
