package com.example.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entities.Attendee;

@Repository
public interface AttendeeDao extends JpaRepository<Attendee, Integer> {

    // metodo para recuperar attendee paginados
    @Query(value = "SELECT p from Attendee p left join fetch p.profile", 
        countQuery = "select count(p) from Attendee p left join p.profile")
    public Page<Attendee> findAll(Pageable pageable);

    // recuperar attendees ordenados sin paginacion
    @Query(value = "SELECT p from Attendee p left join fetch p.profile")
    public List<Attendee> findAll(Sort sort);

    // recuperar attendees por Id
    @Query(value = "SELECT p from Attendee p left join fetch p.profile where p.id = :id")
    public Attendee findById(int id);

    @Query(value = "SELECT a from Attendee a left join fetch a.profile where a.globalId = :globalId")
    public Attendee findByGlobalId(int globalId);

}
