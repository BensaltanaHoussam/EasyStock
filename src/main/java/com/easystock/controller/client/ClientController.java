package com.easystock.controller.client;

import com.easystock.config.auth.Auth;
import com.easystock.dto.client.ClientRequestDto;
import com.easystock.dto.client.ClientResponseDto;
import com.easystock.entity.enums.UserRole;
import com.easystock.service.client.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<ClientResponseDto> createClient(@RequestBody @Valid ClientRequestDto dto) {
        log.info("Request to create client with email: {}", dto.getEmail());
        ClientResponseDto response = clientService.create(dto);
        log.info("Client created successfully with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<ClientResponseDto> getClientById(@PathVariable Long id) {
        log.info("Request to get client by ID: {}", id);
        return ResponseEntity.ok(clientService.findById(id));
    }

    @GetMapping
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<Page<ClientResponseDto>> getAllClients(Pageable pageable) {
        log.info("Request to get all clients for page: {}", pageable.getPageNumber());
        return ResponseEntity.ok(clientService.findAll(pageable));
    }

    @PutMapping("/{id}")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<ClientResponseDto> updateClient(@PathVariable Long id, @RequestBody @Valid ClientRequestDto dto) {
        log.info("Request to update client with ID: {}", id);
        ClientResponseDto response = clientService.update(id, dto);
        log.info("Client updated successfully with ID: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.info("Request to delete client with ID: {}", id);
        clientService.delete(id);
        log.info("Client deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
