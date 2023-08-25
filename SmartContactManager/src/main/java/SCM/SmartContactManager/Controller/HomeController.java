package SCM.SmartContactManager.Controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import SCM.SmartContactManager.Entity.User;
import SCM.SmartContactManager.Helper.Message;
import SCM.SmartContactManager.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController 
{
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@ModelAttribute
	public void commonData(Model model, @AuthenticationPrincipal UserDetails user)
	{
		model.addAttribute("userName", user);
	}
	
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
	public String doRegister(@Valid @ModelAttribute("user") User user, Model model, @RequestParam(value = "agreement", defaultValue="false") boolean agreement,
			BindingResult result, @RequestParam("profileimage") MultipartFile file) throws IOException
	{
		if(result.hasErrors() || file==null) {
			session.setAttribute("message", new Message("Something is missing !!", "alert-danger"));

			return "redirect:/SignUp";
		}
		
		InputStream is=file.getInputStream();
		File saveFile = new ClassPathResource("./static/images/user/").getFile();
		Path path=Paths.get(saveFile.getAbsoluteFile()+File.separator+file.getOriginalFilename());
		Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
		
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setImageUrl(file.getOriginalFilename());
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);
		session.setAttribute("message", new Message("Succesfully Register !!", "alert-success"));
		return "redirect:/signIn";
	}
}
