//package com.example.common.vm.query;
//
//import java.io.Serial;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Order;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//
//@Slf4j
//@AllArgsConstructor
//public class SearchSpecification<T> implements Specification<T> {
//
//  @Serial
//  private static final long serialVersionUID = -9153865343320750644L;
//
//  private final transient SearchRequest request;
//
//  public static Pageable getPageable(Integer page, Integer size) {
//    return PageRequest.of(Objects.requireNonNullElse(page, 0),
//        Objects.requireNonNullElse(size, 100));
//  }
//
//  @Override
//  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//    Predicate predicate = cb.equal(cb.literal(Boolean.TRUE), Boolean.TRUE);
//
//    for (FilterRequest filter : this.request.getFilters()) {
//      log.info("Filter: {} {} {}", filter.getKey(), filter.getOperator().toString(),
//          filter.getValue());
//      predicate = filter.getOperator().build(root, cb, filter, predicate);
//    }
//
//    List<Order> orders = new ArrayList<>();
//    for (SortRequest sort : this.request.getSorts()) {
//      orders.add(sort.getDirection().build(root, cb, sort));
//    }
//
//    query.orderBy(orders);
//    return predicate;
//  }
//
//}
