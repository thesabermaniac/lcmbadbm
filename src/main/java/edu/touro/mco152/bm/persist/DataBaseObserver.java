package edu.touro.mco152.bm.persist;

import edu.touro.mco152.bm.Observer;

import javax.persistence.EntityManager;

/**
 * Observer class for the database persistence.
 * This class should be registered to Subject
 * classes that should be notifying the database
 * when appropriate.
 *
 * @author Shalom Vanderhoof
 */
public class DataBaseObserver implements Observer {

    /**
     * This is called when an instance of this
     * class is registered to a Subject class
     * which subsequently calls its NotifyObservers(DiskRun run)
     * method. We then take that run data and update the db.
     *
     * @param run
     */
    @Override
    public void update(DiskRun run) {
        EntityManager em = EM.getEntityManager();
        em.getTransaction().begin();
        em.persist(run);
        em.getTransaction().commit();
    }

}
