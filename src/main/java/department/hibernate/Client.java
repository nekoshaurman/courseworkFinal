package department.hibernate;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Client")
public class Client {
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

    @Column(name="address")
    private String address;
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name="type")
    private String type;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    @OneToMany(mappedBy = "client")//, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Project> projects = new HashSet<>();
    public Set<Project> getProjects() {
        return projects;
    }
    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    @Transactional
    public String projectsToString() {
        String projectsOfClient = "";
        Set<Project> projects = getProjects();;
        for (Project test : projects) {
            projectsOfClient = projectsOfClient + test.getName() + " ";
        }
        return projectsOfClient;
    }

    public Client() {

    }

    public Client(String name, String address, String type) {
        this.name = name;
        this.address = address;
        this.type = type;
    }
}
