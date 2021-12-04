package com.basic.transactions.management.model.entities;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rekening")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rekening {

    // use String UUID as primary key
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    // rename collumn for noRek to no_rek, unique, not null, length 100
    @Column(name = "no_rek", nullable = false, unique = true, length = 100)
    private String noRek;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "bigint(30)", nullable = false)
    private BigInteger saldo;
}
