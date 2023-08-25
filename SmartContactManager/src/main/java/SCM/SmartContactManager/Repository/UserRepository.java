package SCM.SmartContactManager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import SCM.SmartContactManager.Entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User getUserByEmail(String username);

}
