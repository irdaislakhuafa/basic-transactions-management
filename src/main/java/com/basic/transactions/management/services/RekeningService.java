package com.basic.transactions.management.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.basic.transactions.management.helpers.ResponseMessage;
import com.basic.transactions.management.model.entities.Rekening;
import com.basic.transactions.management.model.repositories.RekeningRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
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
        return rekeningRepository.findByNoRek(nomorRekening);
    }

    public Iterable<Rekening> findByName(String name, Integer page, Integer size) {
        return rekeningRepository.findByNameContains(name, PageRequest.of(page, size));
    }

    public Iterable<Rekening> saveAll(List<Rekening> listRekening) {
        return rekeningRepository.saveAll(listRekening);
    }

    public Rekening findById(String id) {
        return rekeningRepository.findById(id).get();
    }

    @Transactional
    public ResponseMessage transfer(String noRekSource, String noRekDestination, Double money) {
        Boolean isExists;
        ResponseMessage responseMessage = new ResponseMessage();

        try {
            isExists = rekeningRepository.existsByNoRek(noRekSource);
            if (!isExists) {
                responseMessage.setStatus(false);
                responseMessage.setMessage(String.valueOf("Account with NO REK \'" + noRekSource + "\' not found"));
                return responseMessage;
            }

            isExists = rekeningRepository.existsByNoRek(noRekDestination);
            if (!isExists) {
                responseMessage.setStatus(false);
                responseMessage
                        .setMessage(String.valueOf("Account with NO REK \'" + noRekDestination + "\' not found"));
                return responseMessage;
            }

            Rekening source = rekeningRepository.findByNoRek(noRekSource);
            Rekening destination = rekeningRepository.findByNoRek(noRekDestination);

            source.setSaldo(source.getSaldo() - money);
            destination.setSaldo(destination.getSaldo() + money);

            save(source);
            save(destination);

            Map<String, Object> data = new HashMap<>();
            data.put("transfer", money);
            data.put("source", source);
            data.put("destination", destination);

            responseMessage.setStatus(true);
            responseMessage.setMessage("Success");
            responseMessage.setData(data);
            return responseMessage;

        } catch (Exception e) {
            e.printStackTrace();
            responseMessage.setMessage("Error " + e.getMessage());
            responseMessage.setStatus(true);
            return responseMessage;
        }
    }
}
