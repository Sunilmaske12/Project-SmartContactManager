package SCM.SmartContactManager.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
	
	@GetMapping("/viewContact/{page}")
	public String showContactWithPagination(Model model, @PathVariable("page") int page)
	{
		int contactPerPage =2;
		Pageable pagable = PageRequest.of(page, contactPerPage); //page == current page number, 2 == contacts per page
		
		Page<Contact> contact = contactRepository.findAll(pagable);
		model.addAttribute("contacts", contact);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contact.getTotalPages());
		model.addAttribute("contactPerPage", contactPerPage);
		return "normal/view-contact";
	}
	
	@PostMapping("/saveContact")
	public String saveContactInfo(@ModelAttribute("contact") Contact contact, @RequestParam("profileimage") MultipartFile file) throws IOException
	{
		//procession file
		if(file.isEmpty()) 
		{
			contact.setImage("contact.png");
		}
		else
		{
			contact.setImage(file.getOriginalFilename());
			
			File saveFile = new ClassPathResource("static/images/Contacts").getFile();
			
			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
			Files.copy(file.getInputStream(), path);
		}
	//	System.out.println(file.getOriginalFilename());
		contactRepository.save(contact);
		return "redirect:addContact";
	}
	
	@GetMapping("/deleteContact/{cid}")
	public String deleteContactById(@PathVariable("cid") int cid)
	{
		contactRepository.deleteById(cid);
		return "redirect:/Normal/viewContact/0";
	}
	
	@PostMapping("/updateContact/{cid}")
	public String updateContact(@ModelAttribute("cid") int cid)
	{
		Optional<Contact> contactById = contactRepository.findById(cid);
		Contact contact = contactById.get();
		return "redirect:/Normal/viewContact";
	}
	
}
