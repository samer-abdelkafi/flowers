package com.springit.flowers.repo;



import com.springit.flowers.entity.Flow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface FlowRepository extends JpaRepository<Flow, Integer> {


    @Query(""" 
            select t from Flow t
            left join fetch t.publishers
            left join fetch t.subscribers s
            left join fetch s.destination
            """)
    Collection<Flow> getAll();


}
