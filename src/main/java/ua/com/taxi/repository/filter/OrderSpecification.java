package ua.com.taxi.repository.filter;

import org.springframework.data.jpa.domain.Specification;
import ua.com.taxi.domain.Order;
import ua.com.taxi.domain.Order_;
import ua.com.taxi.domain.User;
import ua.com.taxi.domain.User_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecification implements Specification<Order> {

    private static final long serialVersionUID = 8089427019528112580L;

    private OrderFilters filters;

    public OrderSpecification(OrderFilters filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();

        if (filters.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(Order_.creationDate), filters.getStartDate().atStartOfDay()));
        }
        if (filters.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get(Order_.creationDate), filters.getEndDate().atStartOfDay()));
        }
        if (filters.getSelectedPassenger() != null) {
            Join<Order, User> userJoin = root.join(Order_.user, JoinType.INNER);
            predicates.add(cb.equal(userJoin.get(User_.id), filters.getSelectedPassenger()));
        }
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
