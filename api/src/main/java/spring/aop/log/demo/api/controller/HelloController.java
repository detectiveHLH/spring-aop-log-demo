package spring.aop.log.demo.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController
 *
 * @author Lunhao Hu
 * @date 2019-01-30 15:52
 **/
@RestController
public class HelloController {
    @GetMapping("test/{id}")
    public String test(@PathVariable(name = "id") String id) {
        return "Hello" + id;
    }
}
