package be.g00glen00b.functional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.fluentlenium.adapter.FluentTest;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import be.g00glen00b.Application;
import be.g00glen00b.builders.ItemBuilder;
import be.g00glen00b.model.Item;
import be.g00glen00b.repository.ItemRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ApplicationIT extends FluentTest {
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
  
  @Autowired
  private ItemRepository repository;
  @Value("${local.server.port}")
  private int serverPort;
  private WebDriver webDriver = new PhantomJSDriver();
  
  @Before
  public void setUp() {
    repository.deleteAll();
    repository.save(Arrays.asList(FIRST_ITEM, SECOND_ITEM));
    repository.flush();
  }
  
  private String getUrl() {
    return "http://localhost:" + serverPort;
  }
  
  @Test
  public void hasPageTitle() {
    goTo(getUrl());
    assertThat(find(".page-header").getText()).isEqualTo("A checklist");
  }
  
  @Test
  public void hasTwoItems() {
    goTo(getUrl());
    await().atMost(5, TimeUnit.SECONDS).until(".checkbox").hasSize(2);
    assertThat(find(".checkbox").getTexts()).containsOnly(FIRST_ITEM_DESCRIPTION, SECOND_ITEM_DESCRIPTION);
    assertThat(find(".checkbox").first().find(":checked")).isNotEmpty();
    assertThat(find(".checkbox").get(1).find(":checked")).isEmpty();
  }
  
  @Test
  public void hasOneItemAfterDeleting() {
    goTo(getUrl());
    await().atMost(5, TimeUnit.SECONDS).until(".checkbox").hasSize(2);
    find(".form-group").first().find("button").click();
    await().atMost(5, TimeUnit.SECONDS).until(".checkbox").hasSize(1);
    assertThat(find(".checkbox").getTexts()).containsOnly(SECOND_ITEM_DESCRIPTION);
    assertThat(repository.findAll()).hasSize(1);
  }
  
  @Test
  public void hasTwoCheckedItemsAfterCheckingBoth() {
    goTo(getUrl());
    await().atMost(5, TimeUnit.SECONDS).until(".checkbox").hasSize(2);
    find(".checkbox").get(1).find("input[type=checkbox]").click();
    assertThat(find(".form-group :checked")).hasSize(2);
    assertThat(repository.findChecked()).hasSize(2);
  }
  
  @Test
  public void hasThreeItemsAfterAddingOne() {
    goTo(getUrl());
    await().atMost(5, TimeUnit.SECONDS).until(".checkbox").hasSize(2);
    fill(".input-group input[type=text]").with(THIRD_ITEM_DESCRIPTION);
    submit("form");
    await().atMost(5, TimeUnit.SECONDS).until(".checkbox").hasSize(3);
    assertThat(find(".checkbox").getTexts())
      .containsOnly(FIRST_ITEM_DESCRIPTION, SECOND_ITEM_DESCRIPTION, THIRD_ITEM_DESCRIPTION);
    assertThat(repository.findAll()).hasSize(3);
  }

  @Override
  public WebDriver getDefaultDriver() {
      return webDriver;
  }
}
