package br.com.siged.dao.pagination;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtil {

	public void preparar(Criteria criteria, Pageable pageable) {
		criteria.setFirstResult(pageable.getOffset());
		criteria.setMaxResults(pageable.getPageSize());
		
		Sort sort = pageable.getSort();
		if(sort != null) {
			Sort.Order order = sort.iterator().next();
			String property = order.getProperty();
			criteria.addOrder(order.isAscending() ? Order.asc(property) : Order.desc(property));
		}
	}
}
