package com.example.oyster.repository;

import com.example.oyster.model.UserJourney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJourneyRepository extends JpaRepository<UserJourney, Long> {
}
