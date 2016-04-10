package by.model.shop;

import org.hibernate.validator.constraints.CreditCardNumber;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "order")
public class ShopOrder extends SuperModel {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

//    @AssertFalse
    @Column
    private boolean status;

//    @Valid
//    @CreditCardNumber
    @OneToMany(mappedBy = "shopOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OrderItem> items;

    public ShopOrder() { }

    public ShopOrder(long id) { super(id); }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

}
