package by.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "deps")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dep_id")
    private long id;

    @Column(name = "dep_name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dep", fetch = FetchType.LAZY)
    @OrderBy(value = "name")
    private List<User> users;


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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
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
