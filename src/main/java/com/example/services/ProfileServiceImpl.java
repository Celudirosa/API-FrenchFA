package com.example.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dao.ProfileDao;
import com.example.entities.Profile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    
    private final ProfileDao profileDao;
    
    @Override
    public List<Profile> findAll() {
       return profileDao.findAll();
    }

    @Override
    public Profile findById(int id) {
        return profileDao.findById(id).get();
    }

    @Override
    public void save(Profile profile) {
        profileDao.save(profile);
    }

    @Override
    public void delete(Profile profile) {
        profileDao.delete(profile);
    }

}
