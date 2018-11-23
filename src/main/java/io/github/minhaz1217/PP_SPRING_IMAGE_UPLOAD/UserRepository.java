package io.github.minhaz1217.PP_SPRING_IMAGE_UPLOAD;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>{

    User findByUsername(String username);
}
