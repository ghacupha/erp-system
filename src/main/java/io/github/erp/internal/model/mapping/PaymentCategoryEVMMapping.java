package io.github.erp.internal.model.mapping;

import io.github.erp.domain.enumeration.CategoryTypes;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.PaymentCategoryEVM;
import io.github.erp.service.dto.PaymentCategoryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentCategoryEVMMapping extends Mapping<PaymentCategoryEVM, PaymentCategoryDTO> {

    @org.mapstruct.Mapping(target = "categoryType", source = "categoryType")
    default CategoryTypes categoryStringToCategoryType(String categoryString) {
        return  PaymentCategoryMappingUtility.categoryStringToCategoryType(categoryString);
    }

    @org.mapstruct.Mapping(target = "categoryType", source = "categoryType")
    default String categoryTypeToCategoryString(CategoryTypes categoryType) {
        return PaymentCategoryMappingUtility.categoryTypeToCategoryString(categoryType);
    }
}
