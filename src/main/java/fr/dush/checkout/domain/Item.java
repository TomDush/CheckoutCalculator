package fr.dush.checkout.domain;


import java.math.BigDecimal;

/**
 * This define a type of item. It's intended to have them in database shared between all orders. Ie: we have only 1
 * instance per type of item, not 1 instance per item.
 */
// Should be used only by serializers and persistence framework
public class Item {

    /**
     * Could be a barcode or a normalised name - this is not a generated value for persistence but a business
     * identifier
     */
    private String code;

    /** Item name */
    private String name;

    /** Normal price */
    private BigDecimal price;

    public Item(String code, String name, BigDecimal price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    protected Item() {
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String toString() {
        return "fr.dush.checkout.domain.Item(code=" + this.code + ", name=" + this.name + ", price=" + this.price + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        final Item other = (Item) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$code = this.getCode();
        final Object other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $code = this.getCode();
        result = result * PRIME + ($code == null ? 0 : $code.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Item;
    }
}
