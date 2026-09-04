package paa.parking.dao;

import java.util.List;

public interface DAO<T, K> {
    /**
     * Returns the element with the given id.
     *
     * @parm id The unique identifier of the element.
     * @return the element with the id or null if it does not exists.
     */
    T find (K id);

    /**
     * Returns a list with all elements of kind T stored.
     * 
     * @return the list of elements
     */
    List<T> findAll ();

    /**
     * Add a new element to the data base and return the updated version
     *
     * @param t. The element to be stored.
     * @return The elelemt with its unique identifier updated.
     * @throws DAOException if the operation cannot be completed.
     */
    T create (T t); 

    /**
     * Update an stored element in the database.
     *
     * @param t the element to be updated.
     * @return the element after been updated.
     * @throws DAOException if the operation cannot be completed.
     */
    T update(T t);

    /**
     * Delete the given element from the database
     * 
     * @param t. The element to be delete.
     * @throws DAOException if the operation cannot be completed.
    */
    void delete(T t);

}
