package demo.demo_rest.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;
    

    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private int price;
    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "scorta")
    private int scorta;
    @Column(name = "type")
    private String type;
    private int sort;

    @Version
    @Column(name = "version")
    private long version; 

}

