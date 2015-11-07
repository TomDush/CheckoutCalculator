package fr.dush.checkout.domain;

import lombok.*;

import java.math.BigDecimal;

/**
 * This define a type of item. It's intended to have them in database shared between all orders. Ie: we have only 1
 * instance per type of item, not 1 instance per item.
 */
@Data
@EqualsAndHashCode(of = "code")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Should be used only by serializers and persistence framework
@RequiredArgsConstructor
public class Item {

    /**
     * Could be a barcode or a normalised name - this is not a generated value for persistence but a business
     * identifier
     */
    @NonNull
    private String code;

    /** Item name */
    @NonNull
    private String name;

    /** Normal price */
    @NonNull
    private BigDecimal price;

}
