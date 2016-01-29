package by.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "deps")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dep_id")
    private long id;

    @NaturalId
    @Column(name = "dep_name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dep", fetch = FetchType.LAZY)
    @OrderBy(value = "name")
    private Set<User> users;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
                "id=" + id +
                ", name='" + name + '\'' +
//                ", users=" + users.size() +
                '}';
    }
}
