package com.example.demo.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Transaction_Repo extends JpaRepository<Transaction, Long> {


}
