package com.cartisan.common.domains;

/**
 * @author colin
 */
public interface Entity<TEntity, TId> {
    boolean sameIdentityAs(TEntity other);

    TId getId();

    default void setId(TId tId) {

    }

    default boolean isNew() {
        if (getId() == null) {
            return true;
        }
        return false;
    }
}