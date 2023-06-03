package department.hibernate.services;

import department.hibernate.Client;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
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

    public void deleteClient(Client client) {
        Transaction transaction = null;
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
