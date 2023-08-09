package SCM.SmartContactManager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import SCM.SmartContactManager.UserRepository;
import SCM.SmartContactManager.Entity.User;
import SCM.SmartContactManager.Helper.Message;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController 
{
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private HttpSession session;
	
	@GetMapping("/")
	public String HomePage(Model model)
	{
		model.addAttribute("title", "Home Page");
		return "Home";
	}
	
	@GetMapping("/signIn")
	public String LoginPage(Model model)
	{
		model.addAttribute("title", "Login Page");
		
		return "SignIn";
	}
	
	@GetMapping("/signUp")
	public String SignUpPage(Model model)
	{
		model.addAttribute("title", "SignUp Page");
		model.addAttribute("user", new User());
		return "SignUp";
	}
	
	@GetMapping("/about")
	public String AboutPage(Model model)
	{
		model.addAttribute("title", "About Page");
		return "About";
	}
	
	@PostMapping("/doSignUp")
	public String doRegister(@ModelAttribute("user") User user, Model model, @RequestParam(value = "agreement", defaultValue="false") boolean agreement)
	{
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setImageUrl("Default.png");
		userRepository.save(user);
		session.setAttribute("message", new Message("Succesfully Register !!", "alert-success"));
		return "redirect:/signIn";
	}
}