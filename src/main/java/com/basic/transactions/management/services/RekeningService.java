package com.basic.transactions.management.services;

import java.util.List;

import javax.transaction.Transactional;

import com.basic.transactions.management.model.entities.Rekening;
import com.basic.transactions.management.model.repositories.RekeningRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RekeningService {
    @Autowired
    private RekeningRepository rekeningRepository;

    public List<Rekening> findAll() {
        return rekeningRepository.findAll();
    }

    public Iterable<Rekening> findAll(Pageable pageable) {
        return rekeningRepository.findAll(pageable);
    }

    public Rekening save(Rekening rekening) {
        return rekeningRepository.save(rekening);
    }

    public void removeById(String id) {
        rekeningRepository.deleteById(id);
    }

    public Rekening findByNomorRekening(String nomorRekening) {
        return rekeningRepository.findByNomorRekening(nomorRekening);
    }

    public Iterable<Rekening> findByName(String name, Integer page, Integer size) {
        return rekeningRepository.findByNameContains(name, PageRequest.of(page, size));
    }
}
