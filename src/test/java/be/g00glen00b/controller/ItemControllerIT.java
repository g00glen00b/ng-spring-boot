package be.g00glen00b.controller;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.apache.http.HttpStatus;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import be.g00glen00b.Application;
import be.g00glen00b.builders.ItemBuilder;
import be.g00glen00b.model.Item;
import be.g00glen00b.repository.ItemRepository;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ItemControllerIT {   
  private static final String CHECKED_FIELD = "checked";
  private static final String DESCRIPTION_FIELD = "description";
  private static final String ITEMS_RESOURCE = "/items";
  private static final String ITEM_RESOURCE = "/items/{id}";
  private static final int NON_EXISTING_ID = 999;
  private static final String FIRST_ITEM_DESCRIPTION = "First item";
  private static final String SECOND_ITEM_DESCRIPTION = "Second item";
  private static final String THIRD_ITEM_DESCRIPTION = "Third item";
  private static final Item FIRST_ITEM = new ItemBuilder()
    .description(FIRST_ITEM_DESCRIPTION)
    .checked()
    .build();
  private static final Item SECOND_ITEM = new ItemBuilder()
    .description(SECOND_ITEM_DESCRIPTION)
    .build();
  private static final Item THIRD_ITEM = new ItemBuilder()
    .description(THIRD_ITEM_DESCRIPTION)
    .build();
  @Autowired
  private ItemRepository repository;
  @Value("${local.server.port}")
  private int serverPort;
  private Item firstItem;
  private Item secondItem;
  
  @Before
  public void setUp() {
    repository.deleteAll();
    firstItem = repository.save(FIRST_ITEM);
    secondItem = repository.save(SECOND_ITEM);
    RestAssured.port = serverPort;
  }

  @Test
  public void getItemsShouldReturnBothItems() {
    when()
      .get(ITEMS_RESOURCE)
    .then()
      .statusCode(HttpStatus.SC_OK)
      .body(DESCRIPTION_FIELD, hasItems(FIRST_ITEM_DESCRIPTION, SECOND_ITEM_DESCRIPTION))
      .body(CHECKED_FIELD, hasItems(true, false));
  }
  
  @Test
  public void addItemShouldReturnSavedItem() {
    given()
      .body(THIRD_ITEM)
      .contentType(ContentType.JSON)
    .when()
      .post(ITEMS_RESOURCE)
    .then()
      .statusCode(HttpStatus.SC_OK)
      .body(DESCRIPTION_FIELD, is(THIRD_ITEM_DESCRIPTION))
      .body(CHECKED_FIELD, is(false));
  }
  
  @Test
  public void addItemShouldReturnBadRequestWithoutBody() {
    when()
      .post(ITEMS_RESOURCE)
    .then()
      .statusCode(HttpStatus.SC_BAD_REQUEST);
  }
  
  @Test
  public void addItemShouldReturnNotSupportedMediaTypeIfNonJSON() {
    given()
      .body(THIRD_ITEM)
    .when()
      .post(ITEMS_RESOURCE)
    .then()
      .statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);
  }
  
  @Test
  public void updateItemShouldReturnUpdatedItem() {
    // Given an unchecked first item
    Item item = new ItemBuilder()
      .description(FIRST_ITEM_DESCRIPTION)
      .build();
    given()
      .body(item)
      .contentType(ContentType.JSON)
    .when()
      .put(ITEM_RESOURCE, firstItem.getId())
    .then()
      .statusCode(HttpStatus.SC_OK)
      .body(DESCRIPTION_FIELD, is(FIRST_ITEM_DESCRIPTION))
      .body(CHECKED_FIELD, is(false));
  }
  
  @Test
  public void updateItemShouldReturnBadRequestWithoutBody() {
    when()
      .put(ITEM_RESOURCE, firstItem.getId())
    .then()
      .statusCode(HttpStatus.SC_BAD_REQUEST);
  }
  
  @Test
  public void updateItemShouldReturnNotSupportedMediaTypeIfNonJSON() {
    given()
      .body(FIRST_ITEM)
    .when()
      .put(ITEM_RESOURCE, firstItem.getId())
    .then()
      .statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);
  }
  
  @Test
  public void updateItemShouldBeBadRequestIfNonExistingID() {
    given()
      .body(FIRST_ITEM)
      .contentType(ContentType.JSON)
    .when()
      .put(ITEM_RESOURCE, NON_EXISTING_ID)
    .then()
      .statusCode(HttpStatus.SC_BAD_REQUEST);
  }
  
  @Test
  public void deleteItemShouldReturnNoContent() {
    when()
      .delete(ITEM_RESOURCE, secondItem.getId())
    .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);
  }
  
  @Test
  public void deleteItemShouldBeBadRequestIfNonExistingID() {
    when()
      .delete(ITEM_RESOURCE, NON_EXISTING_ID)
    .then()
      .statusCode(HttpStatus.SC_BAD_REQUEST);
  }
}
