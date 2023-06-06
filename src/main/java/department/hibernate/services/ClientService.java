package department.hibernate.services;

import department.hibernate.Client;
import department.hibernate.Project;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for working with Client table in the database
 */
public class ClientService {
    /**
     * Adds a client to the database
     * @param client
     * @return
     */
    public Boolean addClient(Client client) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(client);
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
     * Updates a client in database
     * @param client
     */
    public void updateClient(Client client) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(client);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    /**
     * Deletes a client from database
     * @param client
     */
    public void deleteClient(Client client) {
        Transaction transaction = null;
        for (Project project : client.getProjects()) new ProjectService().deleteProject(project);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(client);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    /**
     * Get client from database by id
     * @param id
     * @return
     */
    @Transactional
    public Client getClient(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Client client = session.find(Client.class, id);
            Hibernate.initialize(client.getProjects());
            return client;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Gets all clients from database
     * @return
     */
    public List<Client> getClients() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Client> clients = session.createQuery("from Client", Client.class).list();
            for(Client client : clients){
                Hibernate.initialize(client.getProjects());
            }
            return clients;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}
