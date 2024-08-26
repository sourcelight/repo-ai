package ai.example.restfulapi.bookstore_crud.config;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "testUser", roles = { "USER", "ADMIN" })
public @interface SecuredTest {
}
