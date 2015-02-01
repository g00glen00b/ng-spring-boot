package be.g00glen00b.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.*;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import be.g00glen00b.Application;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.*;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
  TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DatabaseSetup(ItemRepositoryIT.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { ItemRepositoryIT.DATASET })
@DirtiesContext
public class ItemRepositoryIT {
  protected static final String DATASET = "classpath:datasets/it-items.xml";
  private static final String FIRST_ITEM = "Item 1";
  private static final String THIRD_ITEM = "Item 3";
  private static final String DESCRIPTION_FIELD = "description";
  @Autowired
  private ItemRepository repository;

  @Test
  public void test() {
    assertThat(repository.findChecked())
      .hasSize(2)
      .extracting(DESCRIPTION_FIELD)
      .containsOnly(FIRST_ITEM, THIRD_ITEM);
  }

}
