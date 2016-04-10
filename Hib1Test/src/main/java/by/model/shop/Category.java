package by.model.shop;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category extends SuperModel {

    @Column
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_category", referencedColumnName = "id")
    private Category parentCategory;

    @OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY)
    private Set<Product> products;

    public Category() { super(); }

    public Category(long id) { super(id); }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    @Override
    public String toString() {
        return "Category{" + getId() +":" + title + "}";
    }
}
