package hu.ak_akademia.group.repository;

import hu.ak_akademia.group.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Integer> {

    Optional<Shop> findByName(String name); // Spring Data

    @Query("SELECT s FROM Shop s LEFT JOIN FETCH s.items WHERE s.name = :name") // JPQL
    Optional<Shop> findByNameWithItems(String name);
}
