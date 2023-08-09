package SCM.SmartContactManager;

import org.springframework.data.jpa.repository.JpaRepository;

import SCM.SmartContactManager.Entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
