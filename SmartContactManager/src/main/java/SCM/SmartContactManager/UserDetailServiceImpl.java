package SCM.SmartContactManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import SCM.SmartContactManager.Entity.User;
import SCM.SmartContactManager.Repository.UserRepository;
import SCM.SmartContactManager.config.CustomUserDetails;




public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepository.getUserByEmail(username);
		if(user==null) {
			throw new UsernameNotFoundException("User Not found");
		} 
		
		CustomUserDetails cud=new CustomUserDetails(user);
		
		return cud;
	}

}
