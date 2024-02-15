package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Profile;

public interface ProfileDao extends JpaRepository<Profile, Integer> {

}
