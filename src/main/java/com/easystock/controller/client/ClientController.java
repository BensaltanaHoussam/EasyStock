package com.easystock.controller.client;

import com.easystock.config.auth.Auth;
import com.easystock.dto.client.ClientRequestDto;
import com.easystock.dto.client.ClientResponseDto;
import com.easystock.entity.enums.UserRole;
import com.easystock.service.client.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<ClientResponseDto> createClient(@RequestBody @Valid ClientRequestDto dto) {
        return new ResponseEntity<>(clientService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<ClientResponseDto> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @GetMapping
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<Page<ClientResponseDto>> getAllClients(Pageable pageable) {
        return ResponseEntity.ok(clientService.findAll(pageable));
    }

    @PutMapping("/{id}")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<ClientResponseDto> updateClient(@PathVariable Long id, @RequestBody @Valid ClientRequestDto dto) {
        return ResponseEntity.ok(clientService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}