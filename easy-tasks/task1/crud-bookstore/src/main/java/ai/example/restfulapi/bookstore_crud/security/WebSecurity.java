/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */

package ai.example.restfulapi.bookstore_crud.security;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component("webSecurity")
public class WebSecurity {

    public boolean checkHttpMethodDelete(HttpServletRequest request) {
        return "DELETE".equalsIgnoreCase(request.getMethod());
    }
}