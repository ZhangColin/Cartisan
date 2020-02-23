package com.cartisan.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.lang.reflect.Field;
import java.util.*;

import static java.util.Arrays.asList;

/**
 * @author colin
 */
@Slf4j
public final class ConditionSpecifications {
    private ConditionSpecifications() {
    }

    public static <T, S> Specification<T> querySpecification(S searchCondition) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (Objects.isNull(searchCondition)) {
                return null;
            }

            final List<Predicate> predicates = new ArrayList<>();
            final List<Field> allFields = getAllFields(searchCondition.getClass());
            try {
                for (Field field : allFields) {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);

                    final Condition condition = field.getAnnotation(Condition.class);
                    if (!Objects.isNull(condition)) {
                        String propName = condition.propName();
                        String attributeName = StringUtils.isEmpty(propName) ? field.getName() : propName;
                        final Class<?> fieldType = field.getType();

                        final Object val = field.get(searchCondition);

                        if (Objects.isNull(val) || "".equals(val)) {
                            continue;
                        }

                        String blurry = condition.blurry();
                        if (!StringUtils.isEmpty(blurry)) {
                            final Predicate[] orPredicates = Arrays.stream(blurry.split(","))
                                    .map(b -> criteriaBuilder.like(root.get(b).as(String.class), "%" + val.toString() + "%"))
                                    .toArray(Predicate[]::new);
                            predicates.add(criteriaBuilder.or(orPredicates));
                            continue;
                        }
                        predicates.add(handlerOf(condition.type()).toPredicate(root, criteriaBuilder, fieldType, attributeName, val));
                    }

                    field.setAccessible(accessible);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (predicates.isEmpty()) {
                return null;
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static ConditionSpecificationHandler handlerOf(Condition.Type queryType) {
        return handlers.get(queryType);
    }

    private static Map<Condition.Type, ConditionSpecificationHandler> handlers =
            new HashMap<Condition.Type, ConditionSpecificationHandler>() {{
                put(Condition.Type.EQUAL, (root, criteriaBuilder, fieldType, attributeName, val) -> criteriaBuilder.equal(root.get(attributeName).as(fieldType), val));

                put(Condition.Type.NOT_EQUAL, (root, criteriaBuilder, fieldType, attributeName, val) ->
                        criteriaBuilder.notEqual(root.get(attributeName).as(fieldType), val));

                put(Condition.Type.GREATER_EQUAL, (root, criteriaBuilder, fieldType, attributeName, val) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get(attributeName).as((Class<? extends Comparable>) fieldType), (Comparable) val));

                put(Condition.Type.GREATER, (root, criteriaBuilder, fieldType, attributeName, val) ->
                        criteriaBuilder.greaterThan(root.get(attributeName).as((Class<? extends Comparable>) fieldType), (Comparable) val));

                put(Condition.Type.LESS_EQUAL, (root, criteriaBuilder, fieldType, attributeName, val) ->
                        criteriaBuilder.lessThanOrEqualTo(root.get(attributeName).as((Class<? extends Comparable>) fieldType), (Comparable) val));

                put(Condition.Type.LESS, (root, criteriaBuilder, fieldType, attributeName, val) ->
                        criteriaBuilder.lessThan(root.get(attributeName).as((Class<? extends Comparable>) fieldType), (Comparable) val));

                put(Condition.Type.INNER_LIKE, (root, criteriaBuilder, fieldType, attributeName, val) ->
                        criteriaBuilder.like(root.get(attributeName).as(String.class), "%" + val.toString() + "%"));

                put(Condition.Type.LEFT_LIKE, (root, criteriaBuilder, fieldType, attributeName, val) ->
                        criteriaBuilder.like(root.get(attributeName).as(String.class), "%" + val.toString()));

                put(Condition.Type.RIGHT_LIKE, (root, criteriaBuilder, fieldType, attributeName, val) ->
                        criteriaBuilder.like(root.get(attributeName).as(String.class), val.toString() + "%"));

                put(Condition.Type.IN, (root, criteriaBuilder, fieldType, attributeName, val) -> {
                    final Collection<Object> ins = (Collection<Object>) val;
                    if (!ins.isEmpty()) {
                        return root.get(attributeName).in(ins);
                    }
                    return null;
                });

                put(Condition.Type.BETWEEN, (root, criteriaBuilder, fieldType, attributeName, val) -> {
                    final List<Object> between = (List<Object>) val;
                    return criteriaBuilder.between(root.get(attributeName).as((Class<? extends Comparable>) between.get(0).getClass()), (Comparable) between.get(0), (Comparable) between.get(1));
                });
            }};

    private static List<Field> getAllFields(Class<?> clazz) {
        if (Objects.isNull(clazz)) {
            return Collections.emptyList();
        }

        final List<Field> fields = asList(clazz.getDeclaredFields());
        fields.addAll(getAllFields(clazz.getSuperclass()));

        return fields;
    }
}