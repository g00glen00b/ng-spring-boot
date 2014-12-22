package be.g00glen00b.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import be.g00glen00b.builders.ItemBuilder;
import be.g00glen00b.model.Item;
import be.g00glen00b.repository.ItemRepository;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {
  private static final int CHECKED_ITEM_ID = 1;
  private static final Item CHECKED_ITEM = new ItemBuilder()
    .id(CHECKED_ITEM_ID)
    .checked()
    .build();
  private static final Item UNCHECKED_ITEM = new ItemBuilder()
    .id(2)
    .checked()
    .build();
  private static final Item NEW_ITEM = new ItemBuilder()
    .checked()
    .build();
  @InjectMocks
  private ItemController controller;
  @Mock
  private ItemRepository repository;
  private ArgumentCaptor<Item> anyItem = ArgumentCaptor.forClass(Item.class);
  
  @Test
  public void whenFindingItemsItShouldReturnAllItems() {
    // Given that the repository returns CHECKED_ITEM and UNCHECKED_ITEM
    given(repository.findAll()).willReturn(Arrays.asList(CHECKED_ITEM, UNCHECKED_ITEM));
    // When looking for all items
    assertThat(controller.findItems())
    // Then it should return the CHECKED_ITEM and UNCHECKED_ITEM 
    .containsOnly(CHECKED_ITEM, UNCHECKED_ITEM);
  }

  @Test
  public void whenAddingItemItShouldReturnTheSavedItem() {
    // Given that a NEW_ITEM is saved and flushed, a CHECKED_ITEM is returned
    given(repository.saveAndFlush(NEW_ITEM)).willReturn(CHECKED_ITEM);
    // When adding a NEW_ITEM
    assertThat(controller.addItem(NEW_ITEM))
    // Then it should return the CHECKED_ITEM
    .isSameAs(CHECKED_ITEM);
  }
  
  @Test
  public void whenAddingItemItShouldMakeSureNoIDIsPassed() {
    // Given that a CHECKED_ITEM is added
    controller.addItem(CHECKED_ITEM);
    // Verify that when the item is saved
    verify(repository).saveAndFlush(anyItem.capture());
    // It should have an empty ID
    assertThat(anyItem.getValue().getId()).isNull();
  }
  
  @Test
  public void whenUpdatingItemItShouldReturnTheSavedItem() {
    // Given that CHECKED_ITEM is returned when one is requested with CHECKED_ITEM_ID
    given(repository.getOne(CHECKED_ITEM_ID)).willReturn(CHECKED_ITEM);
    // Given that a CHECKED_ITEM is saved and flushed, a CHECKED_ITEM is returned
    given(repository.saveAndFlush(CHECKED_ITEM)).willReturn(CHECKED_ITEM);
    // When updating a CHECKED_ITEM
    assertThat(controller.updateItem(CHECKED_ITEM, CHECKED_ITEM_ID))
    // Then it should return the CHECKED_ITEM
    .isSameAs(CHECKED_ITEM);
  }
  
  @Test
  public void whenUpdatingItemItShouldUseTheGivenID() {
    // Given that CHECKED_ITEM is returned when one is requested with CHECKED_ITEM_ID
    given(repository.getOne(CHECKED_ITEM_ID)).willReturn(CHECKED_ITEM);
    // Given that a CHECKED_ITEM with CHECKED_ITEM_ID is updated
    controller.updateItem(NEW_ITEM, CHECKED_ITEM_ID);
    // Verify that when the item is saved
    verify(repository).saveAndFlush(anyItem.capture());
    // It should have the given CHECKED_ITEM_ID
    assertThat(anyItem.getValue().getId()).isEqualTo(CHECKED_ITEM_ID);
  }
  
  @Test
  public void whenDeletingAnItemItShouldUseTheRepository() {
    // Given that an item with CHECKED_ITEM_ID is removed
    controller.deleteItem(CHECKED_ITEM_ID);
    // Verify that the repository is used to delete the item
    verify(repository).delete(CHECKED_ITEM_ID);
  }
}
