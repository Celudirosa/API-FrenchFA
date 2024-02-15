package com.example.services;

import java.util.List;

import com.example.entities.Profile;

public interface ProfileService {

    public List<Profile> findAll();
    public Profile findById(int id);
    public void save(Profile profile);
    public void delete(Profile profile);
    
}
