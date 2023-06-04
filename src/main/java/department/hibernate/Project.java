package department.hibernate;

import department.hibernate.services.TaskService;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Project")
public class Project {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Column(name="name")
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name="deadline")
    private String deadline;
    public String getDeadline() {
        return deadline;
    }
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @Column(name="status")
    private String status;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name="additional")
    private String additional;
    public String getAdditional() {
        return additional;
    }
    public void setAdditional(String additional) {
        this.additional = additional;
    }

//    @ManyToMany(mappedBy = "projects", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
//    private Set<Worker> workers = new HashSet<>();
//    public void addWorker(Worker worker) {
//        this.workers.add(worker);
//        worker.getProjects().add(this);
//    }
//    public Set<Worker> getWorkers() {
//        return workers;
//    }
//    public void removeWorker(long tagId) {
//        Worker person = this.workers.stream().filter(t -> t.getId() == tagId).findFirst().orElse(null);
//        if (person != null) {
//            this.workers.remove(person);
//            person.getProjects().remove(this);
//        }
//    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)//, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks = new HashSet<>();
    public void addTask(Task task) {
        this.tasks.add(task);
        task.setProject(this);
    }
    public Set<Task> getTasks() {
        return tasks;
    }
    public void removeTask(long Id) {
        Task task = this.tasks.stream().filter(t -> t.getId() == Id).findFirst().orElse(null);
        if (task != null) {
            this.tasks.remove(task);
            new TaskService().deleteTask(task);
        }
    }

    public void updateStatus() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        LocalDate dead = LocalDate.parse(deadline, formatter);
        LocalDate today = LocalDate.now();

        if (dead.isBefore(today)) {
            this.status = "Expired";
        } else if (dead.isAfter(today)) {
            this.status = "In work";
        } else {
            this.status = "In work";
        }
    }

    public Project() {

    }

    public Project(String nameOfProject, String deadLine, String additional) {
        this.name = nameOfProject;
        this.deadline = deadLine;
        this.additional = additional;
        updateStatus();
    }
}
