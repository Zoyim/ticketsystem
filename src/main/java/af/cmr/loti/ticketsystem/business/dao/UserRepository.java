package af.cmr.loti.ticketsystem.business.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import af.cmr.loti.ticketsystem.business.entity.User;
import af.cmr.loti.ticketsystem.business.utils.ConstsValues;

@Repository(value = ConstsValues.ConstsDAO.USER_DAO_KEY)
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByEmail(String email);
	User findByUsername(String username);
}
