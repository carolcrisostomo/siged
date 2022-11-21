package br.com.siged.dao.pagination;

import javax.servlet.ServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

/**
 * Instancia os parametros necessários para fazer a paginação na consulta e na {@link PaginatedListAdapter}
 * @author rafael.castro
 *
 */
public class DisplayTagPageable implements Pageable{
	
	private String order = "asc";
	private String sortProperty = "";
	private int page = 1;
	private int pageSize = 10;
	
	public DisplayTagPageable(ServletRequest request) {
		String sort = request.getParameter("sort");
		String order = request.getParameter("dir");
		String page = request.getParameter("page");
		
		if(sort != null)
			this.sortProperty = sort;
		
		if(order != null)
			this.order = order;
		
		if(page != null)
			this.page = Integer.parseInt(page);
	}
	
	public DisplayTagPageable(ServletRequest request, int pageSize) {
		this(request);
		this.pageSize = pageSize;
	}
	
	@Override
	public int getOffset() {
		return (this.page - 1) * this.pageSize;
	}

	@Override
	public int getPageNumber() {
		return this.page;
	}

	@Override
	public int getPageSize() {
		return this.pageSize;
	}

	@Override
	public Sort getSort() {
		Order order = null;
		if(!this.sortProperty.equals("")) {
			Direction dir = Direction.ASC;
			if(this.order.equals("desc"))
				dir = Direction.DESC;
			
			order = new Order(dir, this.sortProperty);
			return new Sort(order);
		}
		return null;
	}
	
}
