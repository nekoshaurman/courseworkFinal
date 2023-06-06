package department.hibernate.services;

import department.hibernate.Client;
import department.hibernate.Project;
import department.hibernate.Task;
import department.hibernate.Worker;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class TableService {
    private PDDocument document;
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

    void create_pdf_table(DefaultTableModel model, String table_name) throws IOException {

        PDPage page = new PDPage();
        document.addPage(page);

        int pageHeight = (int) page.getTrimBox().getHeight() - 1;
        int pageWidth = (int) page.getTrimBox().getHeight() - 1;
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setStrokingColor(Color.BLACK);
        contentStream.setLineWidth(1);

        int initX = 15;
        int initY = pageHeight;
        int cellHeight = 20;
        int cellWidth = 90;

        contentStream.beginText();
        contentStream.newLineAtOffset(pageWidth / 2 - 130, initY - cellHeight + 10);
        contentStream.setFont(PDType1Font.TIMES_BOLD, 15);
        contentStream.showText(table_name);
        contentStream.endText();
        initY -= cellHeight;
        initY -= cellHeight;

        for (int i = -1; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                if (j == 0) contentStream.addRect(initX, initY, 20, -cellHeight);
                else if (j == 1) contentStream.addRect(initX, initY, 180, -cellHeight);
                else if (j == 2) contentStream.addRect(initX, initY, 60, -cellHeight);
                else if (j == 3) contentStream.addRect(initX, initY, 180, -cellHeight);
                else contentStream.addRect(initX, initY, 120, -cellHeight);

                contentStream.beginText();
                if (j == 0) contentStream.newLineAtOffset(initX + 2, initY - cellHeight + 8);
                else if (j == 1) contentStream.newLineAtOffset(initX + 2, initY - cellHeight + 8);
                else if (j == 2) contentStream.newLineAtOffset(initX + 2, initY - cellHeight + 8);
                else if (j == 3) contentStream.newLineAtOffset(initX + 2, initY - cellHeight + 8);
                else contentStream.newLineAtOffset(initX + 2, initY - cellHeight + 8);

                if (i == -1) {
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.showText(model.getColumnName(j));

                } else {
                    contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                    contentStream.showText(model.getValueAt(i, j).toString());
                }
                contentStream.endText();
                if (j == 0) initX += 20;
                else if (j == 1) initX += 180;
                else if (j == 2) initX += 60;
                else if (j == 3) initX += 180;
                else initX += 0;
            }
            initX = 15;
            initY -= cellHeight;
            if (initY == 9) {
                contentStream.stroke();
                contentStream.close();

                PDPage second_page = new PDPage();
                document.addPage(second_page);
                initY = pageHeight;
                contentStream = new PDPageContentStream(document, second_page);
                contentStream.setStrokingColor(Color.BLACK);
                contentStream.setLineWidth(1);
            }
        }

        contentStream.stroke();
        contentStream.close();
    }


    public void create_pdf_file(DefaultTableModel projectsModel,
                                DefaultTableModel tasksModel) throws IOException {

        document = new PDDocument();

        create_pdf_table(projectsModel, "Projects");
        create_pdf_table(tasksModel, "Tasks");

        document.save("Report.pdf");
        document.close();
    }
}
