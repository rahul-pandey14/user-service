package com.mphasis.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Handle all HTTP request
@RequestMapping("/api/users") // serve request for this path only
public class UserController {

	// in memory database
	// I will read MAP in great detail
	private static Map<String, User> database = new HashMap<>();

	// HTTP GET
	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(new ArrayList<>(database.values()));
	}

	// HTTP POST
	@PostMapping
	public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
		UUID uuid = UUID.randomUUID();
		user.setId(uuid.toString());
		database.put(uuid.toString(), user);

		// return without password
		user.setPassword(null);

		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}

	// HTTP PUT
	@PutMapping
	public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {

		// get id from it
		String id = user.getId();

		// get user for given id
		User u = database.get(id);
		Assert.notNull(u, "user not found");// user nhi mila bhai raise
											// exception

		// as of now we can only update username
		if (user.getUsername() != null
				|| user.getUsername().trim().length() > 0) {
			u.setUsername(user.getUsername());
		}

		return ResponseEntity.ok(user);
	}

	// HTTP DELETE
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable String id) {
		database.remove(id);
	}

}

class User {
	private String id;
	private String username;
	private String password;
	@DateTimeFormat(style = "yyyy-MM-dd")
	private LocalDate birthDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", birthDate="
				+ birthDate + "]";
	}
}
