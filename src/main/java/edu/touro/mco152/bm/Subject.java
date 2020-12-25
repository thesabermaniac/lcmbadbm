package edu.touro.mco152.bm;

/**
 * Interface for Subject objects that will register various
 * Observer objects and then notify them when appropriate.
 */
public interface Subject {
    public void register(Observer o);
    public void unregister(Observer o);
    public void notifyObservers();
}
