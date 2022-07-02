package com.desafio.tecnico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desafio.tecnico.model.entity.Transfer;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long>  {

}
