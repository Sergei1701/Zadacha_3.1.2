package ru.kata.spring.boot_security.demo.repository;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> getAllRoles() {
        return entityManager.createQuery("select a from Role a", Role.class).getResultList();
    }

    @Override
    public List<Role> findByIdRoles(List<Long> id) {
        return entityManager.createQuery("select a from Role a where a.id in :id", Role.class)
                .setParameter("id", id).getResultList();
    }
}
