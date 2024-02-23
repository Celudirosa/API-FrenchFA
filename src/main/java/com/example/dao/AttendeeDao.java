package com.example.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entities.Attendee;
import com.example.entities.Status;


@Repository
public interface AttendeeDao extends JpaRepository<Attendee, Integer> {

    // metodo para recuperar attendee paginados
    // @Query(value = "SELECT p from Attendee p left join fetch p.profile", 
    //     countQuery = "select count(p) from Attendee p left join p.profile")
    // public Page<Attendee> findAll(Pageable pageable);

    public List<Attendee> findByStatus(Status status, Pageable pageable);

    public List<Attendee> findByStatus(Status status, Sort sort);

    // recuperar attendees ordenados sin paginacion
    @Query(value = "SELECT p from Attendee p left join fetch p.profile")
    public List<Attendee> findAll(Sort sort);

    // recuperar attendees por Id
    @Query(value = "SELECT p from Attendee p left join fetch p.profile where p.id = :id")
    public Attendee findById(int id);

  
}
