package department.hibernate.services;

import department.hibernate.Client;
import department.hibernate.Project;
import department.hibernate.Task;
import department.hibernate.Worker;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class ProjectService {
    public Boolean addProject(Project project) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(project);
            transaction.commit();
            return transaction.getStatus() == TransactionStatus.COMMITTED;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
        return false;
    }

    public void updateProject(Project project) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(project);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    public void deleteProject(Project project) {
        Transaction transaction = null;
        for (Task task : project.getTasks()) new TaskService().deleteTask(task);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

//            for (Worker worker : project.getWorkers()) {
//                worker.getProjects().remove(project);
//                session.saveOrUpdate(worker);
//            }

            //for (Task task : project.getTasks()) new TaskService().deleteTask(task);

            session.delete(project);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    @Transactional
    public Project getProject(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Project project = session.find(Project.class, id);
            //Hibernate.initialize(project.getWorkers());
            Hibernate.initialize(project.getTasks());
            return project;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Project> getProjects() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Project> projects = session.createQuery("from Project", Project.class).list();
            for(Project project : projects){
                //Hibernate.initialize(project.getWorkers());
                Hibernate.initialize(project.getTasks());
            }
            return projects;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}
