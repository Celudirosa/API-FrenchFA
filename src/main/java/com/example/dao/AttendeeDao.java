package com.example.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entities.Attendee;

@Repository
public interface AttendeeDao extends JpaRepository<Attendee, Integer> {

    @Query(value = "SELECT p from Attendee p left join fetch p.profile", 
        countQuery = "select count(p) from Attendee p left join p.profile")
    public Page<Attendee> findAll(Pageable pageable);

}
