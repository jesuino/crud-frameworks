package org.jugvale.crudframeworks.util;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

// source: https://github.com/jboss-jdf/jboss-as-quickstart/blob/jdf-2.1.1.Final/kitchensink/src/main/java/org/jboss/as/quickstarts/kitchensink/util/Resources.java
public class Resources {
	@SuppressWarnings("unused")
	@Produces
	@PersistenceContext(unitName="crud-frameworks-persistence-unit")
	private EntityManager em;
}
