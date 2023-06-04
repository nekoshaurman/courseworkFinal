package department.hibernate.services;

import department.hibernate.Project;
import department.hibernate.Task;
import department.hibernate.Worker;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

public class WorkerService {
    public Boolean addWorker(Worker worker) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(worker);
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

    public void updateWorker(Worker worker) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(worker);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    public void deleteWorker(Worker worker) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            //for (Project project : worker.getProjects()) project.removeWorker(worker.getId());

            for (Task task : worker.getTasks()) worker.removeTask(task.getId());

            session.delete(worker);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    public Worker getWorker(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Worker worker = session.find(Worker.class, id);
            //Hibernate.initialize(worker.getProjects());
            Hibernate.initialize(worker.getTasks());
            return worker;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Worker> getWorkers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Worker> workers = session.createQuery("from Worker", Worker.class).list();
            for(Worker worker : workers){
                //Hibernate.initialize(worker.getProjects());
                Hibernate.initialize(worker.getTasks());
            }
            return workers;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}
