package be.g00glen00b.repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;

import be.g00glen00b.model.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

  @Query("SELECT i FROM Item i WHERE i.checked=true")
  List<Item> findChecked();
}
