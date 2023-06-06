package department.hibernate.services;

import department.hibernate.Task;
import department.hibernate.Worker;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for working with Worker table in the database
 */
public class WorkerService {
    /**
     * Adds a worker to the database
     * @param worker
     * @return
     */
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

    /**
     * Updates a worker in database
     * @param worker
     */
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

    /**
     * Deletes a worker from database
     * @param worker
     */
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

    /**
     * Get worker from database by id
     * @param id
     * @return
     */
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

    /**
     * Gets all workers from database
     * @return
     */
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
