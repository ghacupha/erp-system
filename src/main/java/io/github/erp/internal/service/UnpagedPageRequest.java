package io.github.erp.internal.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * After many trials we saw that hibernate will keep injecting its own SORT BY
 * clauses into the native queries ruining everything because the id generated
 * is usually generated with a guess as to the table from which we are drawing
 * data and that will lead to issues with the FROM clause, because some of the native
 * queries are complex enough for the FROM clause to not be an obvious guess. The windows
 * and CTE revolution in postgresql have seen to that. Am sure this has been
 * fixed in a later driver or hibernate version, but I being a humble single-contributor
 * coder do not have time and resources to do a full scale upgrade of hibernate and the entire spring
 * framework. The whole Jakarta thing just make the whole process a resource-intensive gig.
 * So for now, patch jobs like this will have to do.
 */
public class UnpagedPageRequest extends PageRequest {

    private static final long serialVersionUID = 1L;

    public UnpagedPageRequest(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    @Override
    public Sort getSort() {
        return Sort.unsorted();
    }

    @Override
    public boolean isPaged() {
        return false;
    }

    @Override
    public boolean isUnpaged() {
        return true;
    }

    public static UnpagedPageRequest of(int page, int size, @NotNull Sort sort) {
        return new UnpagedPageRequest(page, size, sort);
    }
}
