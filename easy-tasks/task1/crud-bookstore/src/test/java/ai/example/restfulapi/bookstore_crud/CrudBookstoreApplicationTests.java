package ai.example.restfulapi.bookstore_crud;

import ai.example.restfulapi.bookstore_crud.controller.BookController;
import ai.example.restfulapi.bookstore_crud.hateos.BookModelAssembler;
import ai.example.restfulapi.bookstore_crud.services.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CrudBookstoreApplicationTests {


	//@Test
	void contextLoads() {
	}

}
