package br.com.siged.util;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

public class HibernateUtil {

	@SuppressWarnings("unchecked")
	public static <T> T unproxy(T entity) {
		if (entity == null) {
			return null;
		}

		if (entity instanceof HibernateProxy) {
			Hibernate.initialize(entity);
			entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
		}

		return entity;
	}
}
