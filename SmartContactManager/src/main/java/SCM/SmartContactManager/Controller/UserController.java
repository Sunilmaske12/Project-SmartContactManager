package SCM.SmartContactManager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import SCM.SmartContactManager.Entity.Contact;
import SCM.SmartContactManager.Repository.ContactRepository;

@Controller
@RequestMapping("/Normal")
public class UserController {
	
	@Autowired
	private ContactRepository contactRepository;
	
	@ModelAttribute
	public void commonData(Model model)
	{
		model.addAttribute("userName", "User_Name");
	}
	
	@GetMapping("/index")
	public String userDashboard(Model model)
	{
		model.addAttribute("title", "User-Dashboard");
		return "normal/UserDashboard";
	}
	
	@GetMapping("/addContact")
	public String addContact(Model model)
	{
		model.addAttribute("contact", new Contact());
		return "normal/AddContact";
	}
	
	@PostMapping("/saveContact")
	public String saveContactInfo(@ModelAttribute("contact") Contact contact)
	{
		contactRepository.save(contact);
		return "normal/view-contact";
	}
	
}
