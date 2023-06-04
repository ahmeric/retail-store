package com.ahmeric.store.model.dto;

import java.math.BigDecimal;

/**
 * Record representing a discount result.
 *
 * @param discountAmount      the amount of the discount.
 * @param discountDescription a description of the discount.
 */
public record DiscountResult(BigDecimal discountAmount, String discountDescription) {

}
