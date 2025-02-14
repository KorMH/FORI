package com.foodri.foodreview.receipt.repository;

import com.foodri.foodreview.receipt.entity.Receipts;
import com.foodri.foodreview.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipts, Long> {
  Page<Receipts> findByUser(User user, Pageable pageable);
}
