package com.foodri.foodreview.user.repository;

import com.foodri.foodreview.user.entity.Token;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
  int deleteByEmail(String email);
}
