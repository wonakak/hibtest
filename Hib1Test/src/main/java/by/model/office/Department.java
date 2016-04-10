package by.model.office;

import by.model.shop.SuperModel;
import by.model.shop.User;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "deps")
public class Department extends SuperModel {

    @NaturalId
    @Column(name = "dep_name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dep", fetch = FetchType.LAZY)
    @OrderBy(value = "name")
    private Set<User> users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
//                ", users=" + users.size() +
                '}';
    }
}
