package me.poc.spring.contactApi.service;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Reference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.poc.spring.contactApi.constant.Constant;
import me.poc.spring.contactApi.entity.Contact;
import me.poc.spring.contactApi.repository.ContactRepository;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ContactService {

	@Autowired
	private ContactRepository contactRepository;

	public Page<Contact> getAllContacts(int page, int size) {
		return contactRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
	}

	public Contact getContact(UUID id) {
		return contactRepository.getReferenceById(id);
	}

	public Contact createContact(Contact contact) {
		return contactRepository.save(contact);
	}

	public void deleteContact(Contact contact) {
		contactRepository.delete(contact);
	}

	public String uploadPhoto(UUID id, MultipartFile file) {
		log.info("Saving picture for user ID: {}", id);
		Contact contact = getContact(id);
		String photoUrl = photoFunction.apply(id, file);
		contact.setPhotoUrl(photoUrl);
		contactRepository.save(contact);
		return photoUrl;
	}

	private final Function<String, String> fileExtension = filename -> Optional.of(filename)
			.filter(name -> name.contains(".")).map(name -> "." + name.substring(filename.lastIndexOf(".") + 1))
			.orElse(".png");

	private final BiFunction<UUID, MultipartFile, String> photoFunction = (id, image) -> {
		String filename = id + fileExtension.apply(image.getOriginalFilename());
		try {
			Path fileStorageLocation = Paths.get(Constant.PHOTO_DIRECTORY).toAbsolutePath().normalize();
			if (!Files.exists(fileStorageLocation)) {
				Files.createDirectories(fileStorageLocation);
			}
			Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);
			return ServletUriComponentsBuilder.fromCurrentContextPath().path("/contacts/image/" + filename)
					.toUriString();
		} catch (Exception exception) {
			throw new RuntimeException("Unable to save image");
		}
	};
}
