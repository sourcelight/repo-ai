/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.restfulapi.bookstore_crud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {


    @GetMapping("/test-admin-role")
    public String testAdminRole() {
        return "Admin role test successfully passed !";
    }
}
