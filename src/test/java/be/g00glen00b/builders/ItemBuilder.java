package be.g00glen00b.builders;

import be.g00glen00b.model.Item;

public class ItemBuilder {
  private Item item = new Item();
  
  public ItemBuilder id(int id) {
    item.setId(id);
    return this;
  }
  
  public ItemBuilder description(String description) {
    item.setDescription(description);
    return this;
  }
  
  public ItemBuilder checked() {
    item.setChecked(true);
    return this;
  }
  
  public Item build() {
    return item;
  }
}
