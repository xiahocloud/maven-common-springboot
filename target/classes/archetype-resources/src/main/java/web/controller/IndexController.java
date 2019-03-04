package ${package}.web.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Andy
 * @version 1.0
 * @date 02/28/2019 10:46
 * @description 用于分发页面跳转请求
 */

@Controller
public class IndexController {
	@GetMapping(value = {"/demo"})
	public String index() {
		return "modules/demo/demo";
	}
}
