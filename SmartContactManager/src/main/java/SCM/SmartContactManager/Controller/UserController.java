package SCM.SmartContactManager.Controller;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import SCM.SmartContactManager.Entity.User;
import SCM.SmartContactManager.Helper.Message;
import SCM.SmartContactManager.Repository.ContactRepository;
import SCM.SmartContactManager.Repository.UserRepository;
import SCM.SmartContactManager.config.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Normal")
public class UserController {
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private HttpSession session;
	
			
	@ModelAttribute
	public void commonData(Model model, @AuthenticationPrincipal UserDetails user)
	{
		model.addAttribute("userName", user.getUsername());
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
		model.addAttribute("title", "Add-Contact");
		return "normal/AddContact";
	}
	
	@GetMapping("/viewContact/{page}")
	public String showContactWithPagination(Model model, @PathVariable("page") int page, @AuthenticationPrincipal CustomUserDetails user)
	{
		int contactPerPage =2;
		Pageable pagable = PageRequest.of(page, contactPerPage); //page == current page number, 2 == contacts per page
		
		Page<Contact> contact = contactRepository.findAllByUserId(user.getId(), pagable);
		model.addAttribute("contacts", contact);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contact.getTotalPages());
		model.addAttribute("contactPerPage", contactPerPage);
		model.addAttribute("title", "View-Contact");
		return "normal/view-contact";
	}
	
	@PostMapping("/saveContact")
	public String saveContactInfo(@ModelAttribute("contact") Contact contact, @RequestParam("profileimage") MultipartFile file,  @AuthenticationPrincipal CustomUserDetails user)
	{
		//procession file
		if(file.isEmpty()) 
		{
			contact.setImage("contact.png");
		}
		else
		{
			try {
			contact.setImage(file.getOriginalFilename());
			InputStream is= file.getInputStream();
			File saveFile = new ClassPathResource("./static/images/Contacts").getFile();
			
			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
			Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		contact.setUser(userRepository.findById(user.getId()).get());
		contactRepository.save(contact);
		return "redirect:viewContact/0";
	}
	
	@GetMapping("/deleteContact/{cid}")
	public String deleteContactById(@PathVariable("cid") int cid)
	{
		contactRepository.deleteById(cid);
		return "redirect:/Normal/viewContact/0";
	}
	
	
	@GetMapping("/contactDetailPage/{cid}")
	public String getcontactDetailPage(@PathVariable("cid") int cid, Model model)
	{
		Contact contact= contactRepository.findById(cid).get();
		model.addAttribute("contact", contact);
		model.addAttribute("title", "Contact-Detail");
		return "normal/contact-detail";
	}
	
	@GetMapping("/contactUpdatePage/{cid}")
	public String updateContactPage(@ModelAttribute("cid") int cid, Model model)
	{
		Contact contact  = contactRepository.findById(cid).get();
		model.addAttribute("contact", contact);
		model.addAttribute("title", "Update-Contact");
		return "normal/update-contact";
	}

	@PostMapping("/updateContact")
	public String updateContact(@ModelAttribute Contact contact, @RequestParam("profileimage") MultipartFile file) 
	{
		
		if(!file.isEmpty()) 
		{
			System.out.println("in a block");
			try {
			contact.setImage(file.getOriginalFilename());
			InputStream is= file.getInputStream();
			File saveFile = new ClassPathResource("./static/images/Contacts").getFile();
			
			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
			Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		

		contactRepository.save(contact);
		
		return "redirect:contactDetailPage/"+contact.getCid();
	}
	
	@GetMapping("/setting")
	public String settingPage(Model model) {
		model.addAttribute("title", "Setting");
		return "normal/setting";
	}
	
	@GetMapping("/myProfile")
	public String getprofilePage(Model model)
	{
		User user = userRepository.findById(1).get();
		model.addAttribute("user", user);
		return "normal/profile";
	}
	
	@PostMapping("/changePassword")
	public String changePassword(HttpServletRequest req,  @AuthenticationPrincipal CustomUserDetails userDetail) {
		String oldPass = req.getParameter("oldPassword");
		String newPass = req.getParameter("newPassword");
		
		
			if(encoder.matches(oldPass, userDetail.getPassword())) {
				User user=userRepository.findById(userDetail.getId()).get();
				user.setPassword(encoder.encode(newPass));
				userRepository.save(user);
				session.setAttribute("message",  new Message("Password change succefully" , "alert-sucess"));
				System.out.println("change");
				return "redirect:setting";
			}
			else {
				session.setAttribute("message",  new Message("Please enter currect old password", "alert-danger"));
				System.out.println("not change");
				return "redirect:setting";
			}
			
		
		
	}
}
