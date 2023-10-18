package com.db.migrations.user.repository;

import com.db.migrations.user.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {



  @Query(value = """
            SELECT * FROM address
            WHERE address_with_weight @@ plainto_tsquery(?1)
            ORDER BY ts_rank(address_with_weight, plainto_tsquery(?1)) DESC
            """, nativeQuery = true)
    List<Address> fullTextSearchAddress(String keyword);
}
