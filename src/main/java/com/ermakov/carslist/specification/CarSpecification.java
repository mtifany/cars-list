package com.ermakov.carslist.specification;

import com.ermakov.carslist.model.CarsFilter;
import com.ermakov.carslist.model.entity.ModelEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;

public class CarSpecification implements Specification<ModelEntity> {

  private CarsFilter carsFilter;

  public CarSpecification(CarsFilter carsFilter) {
    this.carsFilter = carsFilter;
  }

  @Override
  public Predicate toPredicate(Root<ModelEntity> root, CriteriaQuery<?> query,
                               CriteriaBuilder criteriaBuilder) {
    if (Objects.isNull(carsFilter))
      return null;
    List<Predicate> predicates = new ArrayList<>();
    var modelName = carsFilter.modelName();
    var brandName = carsFilter.brandName();

    if (Objects.nonNull(modelName) && !modelName.isEmpty()) {
      predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("name")),
          modelName.toLowerCase()));
    }
    if (Objects.nonNull(brandName) && !brandName.isEmpty()){
      predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("brandEntity").get("name")),
          brandName.toLowerCase()));
    }
    return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
  }
}
