package com.db.migrations.user.repository;

import com.db.migrations.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //Using JPQL, this find user by first name ignoring case
    @Query(value = """
            SELECT user FROM User user WHERE lower(user.firstName) = lower(:firstName)
            """)
    List<User> findAllByFirstName(String firstName);

    //Using PostgreSQL, this find user by last name
    @Query(value = """
            SELECT * FROM users
            WHERE to_tsvector('english', last_name) @@ plainto_tsquery(?1)
            """, nativeQuery = true)
    List<User> findAllByLastName(String lastName);


    //this is PostgreSQL for full text search
    @Query(value = """
            SELECT * FROM users
            WHERE user_with_weight @@ plainto_tsquery(?1)
            ORDER BY ts_rank(user_with_weight, plainto_tsquery(?1)) DESC
            """, nativeQuery = true)
    List<User> fullTextSearch(String keyword);





    List<User> findAllByAddress_Id(Long addressId);
}