package by.model.shop;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class SuperModel /*implements Serializable */{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    public SuperModel() {  }

    public SuperModel(long id) { this.id = id;  }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
