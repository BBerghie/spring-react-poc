package me.poc.spring.contactApi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.poc.spring.contactApi.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {

	Optional<Contact> findById(UUID id);
}
