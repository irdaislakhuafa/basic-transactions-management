package com.basic.transactions.management.model.repositories;

import com.basic.transactions.management.model.entities.Rekening;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RekeningRepository extends JpaRepository<Rekening, String> {
    public Page<Rekening> findAll(Pageable pageable);

    public Page<Rekening> findByNameContains(String name, Pageable pageable);

    public Rekening findByNomorRekening(String nomorRekening);
}
