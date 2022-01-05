package com.basic.transactions.management.controllers;

import java.util.List;
import java.util.Map;

import com.basic.transactions.management.helpers.ResponseMessage;
import com.basic.transactions.management.helpers.pagehelpers.RequestPage;
import com.basic.transactions.management.model.dto.SimpleTransferContainer;
import com.basic.transactions.management.model.entities.Rekening;
import com.basic.transactions.management.services.RekeningService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rekening")
public class RekeningController {
    @Autowired
    private RekeningService rekeningService;

    private static ResponseEntity<?> responseEntity;;
    private static ResponseMessage responseMessage;;

    // find all data rekening
    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            responseMessage = new ResponseMessage(
                    HttpStatus.OK,
                    rekeningService.findAll(),
                    "Success");
            responseEntity = new ResponseEntity<>(responseMessage, HttpStatus.OK);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            responseMessage = new ResponseMessage(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Can't get all data : " + e.getMessage());
            responseEntity = new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    // find all data rekening with pageable
    @GetMapping(value = "/pageable")
    public ResponseEntity<?> findAll(@RequestBody RequestPage requestPage) {
        try {
            responseMessage = new ResponseMessage(
                    HttpStatus.OK, // status response
                    ((requestPage.getSortBy() == null) ? // is sortBy == null?
                            rekeningService.findAll( // data response
                                    PageRequest.of( // with pageable parameter
                                            requestPage.getPageNumber(), // page number
                                            requestPage.getDataSize()))
                            : rekeningService.findAll(
                                    PageRequest.of(
                                            requestPage.getPageNumber(),
                                            requestPage.getDataSize(),
                                            Sort.by(requestPage.getSortBy()).ascending()))),
                    "Success"); // message
            responseEntity = new ResponseEntity<>(responseMessage, HttpStatus.OK); // instance new ResponseEntity

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            responseMessage = new ResponseMessage(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Can't get all data : " + e.getMessage());
            responseEntity = new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    // save data rekening
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Rekening rekening) {
        try {
            responseMessage = new ResponseMessage(HttpStatus.OK, rekeningService.save(rekening), "Success");
            responseEntity = new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST, null,
                    "Data with NO REK '" + rekening.getNoRek() + "' with NAME '" + rekening.getName()
                            + "' already exists");
            responseEntity = new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST, null,
                    "Failed to save data  : " + e.getMessage());
            responseEntity = new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PostMapping(value = "/saveall")
    public ResponseEntity<?> saveAll(@RequestBody List<Rekening> listRekening) {
        try {
            Iterable<Rekening> listDataRekening = rekeningService.saveAll(listRekening);
            responseMessage = new ResponseMessage(HttpStatus.OK, listDataRekening, "Success");
            responseEntity = new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            responseMessage = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, null,
                    "Failed to save data : " + e.getMessage());
            responseEntity = new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @DeleteMapping("/remove/id")
    public ResponseEntity<?> removeById(@RequestBody Map<String, String> rekening) { // created object withoud class
        try {
            Rekening tempRekening = rekeningService.findById(rekening.get("id"));
            rekeningService.removeById(rekening.get("id"));
            responseMessage = new ResponseMessage(HttpStatus.OK, tempRekening, "Success deleted");
            responseEntity = new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            responseMessage = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, null,
                    "Failed to delete data with ID : '" + rekening.get("id") + "'");
            responseEntity = new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody SimpleTransferContainer simpleTransferContainer) {
        responseMessage = rekeningService.transfer(
                simpleTransferContainer.getNoRekSource(),
                simpleTransferContainer.getNoRekDestination(),
                simpleTransferContainer.getMoney());

        return new ResponseEntity<>(
                responseMessage,
                (responseMessage.getMessage().toString().equalsIgnoreCase("success")) ? (HttpStatus.OK)
                        : (HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
