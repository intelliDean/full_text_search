package com.db.migrations.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByFirstName(String firstName);

    List<User> findAllByLastName(String lastName);

//    @Query(value = """
//            SELECT first_name, last_name, story from users
//            WHERE search_with_weight @@ plainto_tsquery(?1)
//            ORDER BY ts_rank(search_with_weight, plainto_tsquery(?1))
//            """)


//    @Query("""
//            SELECT user.firstName, user.lastName, user.story FROM User user
//            where
//            WHERE FUNCTION('tsvector_matches', FUNCTION('plainto_tsquery', : keyword),
//            user.searchWithWeight) = true
//            ORDER BY FUNCTION('ts_rank', user.searchWithWeight, FUNCTION('plainto_tsquery', :keyword))
//            """)
    
    
    @Query(value = """
            select * from users
            where search_with_weight @@ plainto_tsquery(?1)
            order by ts_rank(search_with_weight, plainto_tsquery(?1))
            """, nativeQuery = true)
    List<User> fullTextSearch(String keyword);


    //  List<User> findAllByEmail(String email);

//    @Query(value = """
//            SELECT user FROM User user
//            WHERE user.firstName = :keyword or user.lastName = :keyword or user.email = :keyword
//            """)
    // List<User> fullTextSearch(String keyword);

//    @Query(value = """
//            select * from users where
//            match(first_name, last_name, email)
//            against(?1)
//            """, nativeQuery = true)
//    List<User> search(String keyword);
}

/*
 SELECT t.*
FROM dean.users t
WHERE CAST(created_at AS CHAR) LIKE '%%'
OR email LIKE '%%'
OR first_name LIKE '%%'
OR is_active LIKE '%%'
OR last_name LIKE '%%'
OR password LIKE '%%'
LIMIT 10;


* */