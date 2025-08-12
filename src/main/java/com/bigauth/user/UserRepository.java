package com.bigauth.user;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public User findByGoogleId(String googleId) {
        return find("googleId", googleId).firstResult();
    }

    public User save(User user) {
        if (user.getId() == null) {
            persist(user);
        } else {
            getEntityManager().merge(user);
        }
        return user;
    }
}
