package spring.aop.log.demo.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.aop.log.demo.api.util.Log;

/**
 * HelloController
 *
 * @author Lunhao Hu
 * @date 2019-01-30 15:52
 **/
@RestController
public class HelloController {
    @Log(type = "WARNING")
    @GetMapping("test/{id}")
    public String test(
            @PathVariable(name = "id") Integer id,
            @RequestParam(name = "workOrderNumber", required = false) String workOrderNumber,
            @RequestParam(name = "userId") String userId,
            @RequestParam(name = "name") String name
    ) {
        return "Hello" + id;
    }
}
