package by.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

//    @NaturalId
    @Column(name = "name")
    private String name;

//    @NaturalId
    @Column(name = "lastn")
    private String lastName;

    @Column(name = "salary")
    private int salary;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "dep_id")
    private Department dep;


    public User() {
    }

    public User(String name) {
        this.name = name;
    }

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Department getDep() {
        return dep;
    }

    public void setDep(Department dep) {
        this.dep = dep;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
//                ", dep=" + dep +
                '}';
    }
}
