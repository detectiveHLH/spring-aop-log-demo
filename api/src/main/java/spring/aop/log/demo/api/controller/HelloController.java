package spring.aop.log.demo.api.controller;

import org.springframework.web.bind.annotation.*;
import spring.aop.log.demo.api.util.Log;
import spring.aop.log.demo.api.util.Result;
import spring.aop.log.demo.api.util.TestDTO;

/**
 * HelloController
 *
 * @author Lunhao Hu
 * @date 2019-01-30 15:52
 **/
@RestController
public class HelloController {
    @Log(type = "WARNING")
    @PostMapping("test")
    public Result test(
            @RequestParam(name = "workOrderNumber", required = false) String workOrderNumber,
            @RequestParam(name = "userId") String userId,
            @RequestBody TestDTO testDTO
    ) {
        Result result = new Result();
        result.setId(1);
        result.setAge(testDTO.getAge());
        result.setName(testDTO.getName());
        result.setEmail(testDTO.getEmail());
        return result;
    }
}
