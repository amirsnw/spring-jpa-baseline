package com.baseline.entity.one.to.many.uni;

import com.baseline.config.AppConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = AppConstants.TABLE_PREFIX + "customer")
@NamedEntityGraph(name = "Customer.orders",
        attributeNodes = @NamedAttributeNode(value = "orders"))
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true)
    @ReadOnlyProperty
    // @ReadOnlyProperty: It will be loaded from the database when fetching the entity
    // but will not be updated when saving or updating the entity.
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    // OneToMany default: FetchType.LAZY
    @JoinColumn(name = "customer_id")
    private Set<OrderEntity> orders = new HashSet<>();
}
