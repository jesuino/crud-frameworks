package org.jugvale.crudframeworks.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jugvale.crudframeworks.model.Framework;

/**
 * Service to handle the Java Framework model class
 * 
 * @author william
 * 
 */
@Stateless
@Default
public class FrameworkService {

	@Inject
	EntityManager em;

	public void save(Framework javaFramework) {
		em.persist(javaFramework);
	}

	public void update(Framework javaFramework) {
		em.merge(javaFramework);
	}

	public void remove(int id) {
		Framework f = retrieve(id);
		em.remove(f);
	}

	public Framework retrieve(int id) {
		return em.find(Framework.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Framework> retrieveAll() {
		return em.createQuery("SELECT f FROM Framework f").getResultList();
	}
}