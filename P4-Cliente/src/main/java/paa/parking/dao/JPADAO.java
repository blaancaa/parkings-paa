package paa.parking.dao;

import java.util.List;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class JPADAO<T, K> implements DAO<T, K>{
    protected EntityManager em;
    protected Class<T> clazz;
    
    public JPADAO(EntityManager em, Class<T> entityClass) {
		this.clazz = entityClass;
		this.em = em;
	}
    
	@Override
	public T find(K id) {
		return em.find(clazz, id);
	}
	@Override
	public List<T> findAll() {
        ///////////////////////////////////////////////////////////////////////
        // Necesitará hacer consultas a la base de datos mediante una        //
        // TypedQUery, bien empleando una sentencia JPQL o una CriteriaQuery //
        ///////////////////////////////////////////////////////////////////////
		TypedQuery<T> q = em.createQuery("SELECT t FROM " + clazz.getName() + " t", clazz); // JPQL
		List<T> resultList = q.getResultList();
		return resultList;
	}
	@Override
	public T create(T t) {
		try {
            em.persist(t);
            em.flush();
            em.refresh(t);
            return t;
        } catch (EntityExistsException excepcion) {
            throw new DAOException("La entidad ya existe", excepcion);
        }
	}
	@Override
	public T update(T t) {
		return (T) em.merge(t);
	}
	@Override
	public void delete(T t) {
		t = em.merge(t);
		em.remove(t);
	}

}
