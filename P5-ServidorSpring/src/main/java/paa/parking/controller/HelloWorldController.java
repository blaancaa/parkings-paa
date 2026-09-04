package paa.parking.controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorldController 
{
	 @GetMapping("/hello")
	 public String sayHello() 
	 {
		 return "Hello World!" ;
	 }
	 
	 @GetMapping("/hello2")
	 public String sayHello2() 
	 {
		 String s = null;
		 return s.toLowerCase(); // null.toLowerCase() throws NullPointerException
	 }
}
