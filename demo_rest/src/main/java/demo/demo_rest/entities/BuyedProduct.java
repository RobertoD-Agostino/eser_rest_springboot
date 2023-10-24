package demo.demo_rest.entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_buyed")
public class BuyedProduct {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    // @ManyToOne
    // @JoinColumn(name = "product")
    // private Product product;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private int price;
    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "quantity")
    private int quantity;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "lista_comprati")
    private BuyedList buyedList;

    @CreationTimestamp     
    @Temporal(TemporalType.TIMESTAMP)      
    @Column(name = "purchaseDate")     
    private Date purchaseDate;
    
}
