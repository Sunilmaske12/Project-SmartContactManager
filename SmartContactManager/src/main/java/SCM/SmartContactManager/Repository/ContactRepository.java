package SCM.SmartContactManager.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import SCM.SmartContactManager.Entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	Page<Contact> findAllByUserId(int id, Pageable pagable);
	

}
