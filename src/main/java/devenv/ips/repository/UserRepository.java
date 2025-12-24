package devenv.ips.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import devenv.ips.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
