package io.github.erp.repository;

import io.github.erp.domain.DeliveryNote;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DeliveryNote entity.
 */
@Repository
public interface DeliveryNoteRepository extends JpaRepository<DeliveryNote, Long>, JpaSpecificationExecutor<DeliveryNote> {
    @Query(
        value = "select distinct deliveryNote from DeliveryNote deliveryNote left join fetch deliveryNote.placeholders left join fetch deliveryNote.deliveryStamps left join fetch deliveryNote.signatories",
        countQuery = "select count(distinct deliveryNote) from DeliveryNote deliveryNote"
    )
    Page<DeliveryNote> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct deliveryNote from DeliveryNote deliveryNote left join fetch deliveryNote.placeholders left join fetch deliveryNote.deliveryStamps left join fetch deliveryNote.signatories"
    )
    List<DeliveryNote> findAllWithEagerRelationships();

    @Query(
        "select deliveryNote from DeliveryNote deliveryNote left join fetch deliveryNote.placeholders left join fetch deliveryNote.deliveryStamps left join fetch deliveryNote.signatories where deliveryNote.id =:id"
    )
    Optional<DeliveryNote> findOneWithEagerRelationships(@Param("id") Long id);
}
