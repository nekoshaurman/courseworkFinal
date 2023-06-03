package department.hibernate;

import department.hibernate.services.TaskService;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Worker")
public class Worker {
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

    @Column(name="surname")
    private String surname;
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Column(name="dateOfBirth")
    private String dateOfBirth;
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Column(name="additional")
    private String additional;
    public String getAdditional() {
        return additional;
    }
    public void setAdditional(String additional) {
        this.additional = additional;
    }

    @ManyToMany(cascade = {CascadeType.ALL},//{CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
                fetch = FetchType.EAGER)
    @JoinTable(name = "Workers_has_Projects",
            joinColumns = @JoinColumn(name = "worker_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    private Set<Project> projects = new HashSet<>();
    public Set<Project> getProjects() {
        return projects;
    }
    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks = new HashSet<>();
    public void addTask(Task task) {
        this.tasks.add(task);
        task.setWorker(this);
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

    @Override
    public String toString () {
        return String.format("%d %s %s %s %s",
                this.id, this.name, this.surname, this.dateOfBirth, this.additional);
    }

    public String projectsToString() {
        String projectsOfWorker = "";
        Set<Project> projects;
        projects = getProjects();
        for (Project test : projects) {
            projectsOfWorker = projectsOfWorker + test.getName() + " ";
        }
        return projectsOfWorker;
    }

    public Worker() {

    }

    public Worker(String name, String surname, String dateOfBirth, String additional) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.additional = additional;
    }
}
