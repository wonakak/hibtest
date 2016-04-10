package by.model.shop;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends SuperModel {

    @OneToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Person person;

    @JoinTable(name = "users_roles",
            joinColumns = { @JoinColumn (name = "user_id")},
            inverseJoinColumns = {@JoinColumn (name = "role_id")})
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ShopOrder> shopOrders;

    public User() { super(); }
    public User(long id) { super(id); }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<ShopOrder> getShopOrders() {
        return shopOrders;
    }

    public void setShopOrders(Set<ShopOrder> shopOrders) {
        this.shopOrders = shopOrders;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", name='" + getPerson().getFirstName() + '\'' +
                ", lastName='" + getPerson().getLastName() + '\'' +
                ", salary=" + getPerson().getSalary() +
                '}';
    }
}
