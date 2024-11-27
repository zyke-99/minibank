package com.zyke.minibank.service;

import com.zyke.minibank.entity.Address;
import com.zyke.minibank.entity.Address_;
import com.zyke.minibank.entity.Customer;
import com.zyke.minibank.entity.Customer_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {

    public static Specification<Customer> getSearchTermSpecification(String searchTerm) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> searchPredicates = getSearchPredicates(root, criteriaBuilder, searchTerm, searchTerm, searchTerm, searchTerm, searchTerm);
            return criteriaBuilder.or(searchPredicates.toArray(new Predicate[0]));
        };
    }

    public static List<Predicate> getSearchPredicates(Root<Customer> root, CriteriaBuilder criteriaBuilder,
                                                      String name, String lastName,
                                                      String phoneNumber, String email,
                                                      String city) {

        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotEmpty(name)) {

            predicates.add(
                    getWildcardCaseInsensitiveLikePredicate(criteriaBuilder, root.get(Customer_.NAME), name)
            );
        }

        if (StringUtils.isNotEmpty(lastName)) {

            predicates.add(
                    getWildcardCaseInsensitiveLikePredicate(criteriaBuilder, root.get(Customer_.LAST_NAME), lastName)
            );
        }

        if (StringUtils.isNotEmpty(phoneNumber)) {

            predicates.add(
                    getWildcardCaseInsensitiveLikePredicate(criteriaBuilder, root.get(Customer_.PHONE_NUMBER), phoneNumber)
            );
        }

        if (StringUtils.isNotEmpty(email)) {
            predicates.add(
                    getWildcardCaseInsensitiveLikePredicate(criteriaBuilder, root.get(Customer_.EMAIL), email)
            );
        }

        if (StringUtils.isNotEmpty(city)) {

            Join<Customer, Address> customerAddressJoin = root.join(Customer_.ADDRESSES, JoinType.LEFT);
            predicates.add(
                    getWildcardCaseInsensitiveLikePredicate(criteriaBuilder, customerAddressJoin.get(Address_.CITY), city)
            );
        }

        return predicates;
    }

    private static Predicate getWildcardCaseInsensitiveLikePredicate(CriteriaBuilder criteriaBuilder, Path path, String value) {

        return criteriaBuilder.like(
                criteriaBuilder.lower(path),
                "%" + value.toLowerCase() + "%"
        );
    }
}
