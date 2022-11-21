package br.com.siged.dao.pagination;

import java.util.ArrayList;
import java.util.List;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

/**
 * Classe responsável por fazer a paginação da DisplayTag
 * Basta instanciar a {@link Page} e passar para a displaytag na JSP
 * @author rafael.castro
 *
 */
public class PaginatedListAdapter implements PaginatedList{
	
	private Page<?> page = new PageImpl<>(new ArrayList<>());
	
	public PaginatedListAdapter(Page<?> page) {
		this.page = page;
	}

	@Override
	public List<?> getList() {
		return page.getContent();
	}

	@Override
	public int getPageNumber() {
		return page.getNumber();
	}

	@Override
	public int getObjectsPerPage() {
		return page.getSize();
	}

	@Override
	public int getFullListSize() {
		return (int) page.getTotalElements();
	}

	@Override
	public String getSortCriterion() {
		Sort sort = page.getSort();
		if(sort != null) {
			Sort.Order order = sort.iterator().next();
			return order.getProperty();
		}
		return null;
	}

	@Override
	public SortOrderEnum getSortDirection() {
		Sort sort = page.getSort();
		if(sort != null) {
			Sort.Order order = sort.iterator().next();
			return order.isAscending() ? SortOrderEnum.ASCENDING : SortOrderEnum.DESCENDING;
		}
		return null;
	}

	@Override
	public String getSearchId() {
		return null;
	}

}
