package by.model.shop;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import javax.validation.constraints.*;

@Entity
@Table(name = "product")
public class Product extends SuperModel {

    @NotNull(message = "Title should not be NULL")
    @Size(min = 4, max = 250)
    @Column
    private String title;

//    @Email
//    @Pattern(regexp = "^\\w+\\s+\\w+$")
    @NotEmpty
    @Column
    private String description;

    @NotNull
    @Column
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_category_id", referencedColumnName = "id")
    private Category productCategory;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OrderItem> orderItem;

    public Product() { super(); }

    public Product(long id) { super(id); }

    public Product(String title, String description, BigDecimal price, Category productCategory) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.productCategory = productCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Category productCategory) {
        this.productCategory = productCategory;
    }

    public Set<OrderItem> getOrderItem() {
        return orderItem;
    }

    @Override
    public String toString() {
        return "Product{" + getId() +":" + title +" $=" + price +
//                ", description='" + description + '\'' +
//                ", category=" + productCategory.getTitle() +
                '}';
    }
}
