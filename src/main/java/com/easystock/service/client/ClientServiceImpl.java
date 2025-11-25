package com.easystock.service.client;

import com.easystock.dto.client.ClientRequestDto;
import com.easystock.dto.client.ClientResponseDto;
import com.easystock.entity.Client;
import com.easystock.mapper.ClientMapper;
import com.easystock.repository.ClientRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientResponseDto create(ClientRequestDto dto) {
        clientRepository.findByEmail(dto.getEmail()).ifPresent(c -> {
            throw new EntityExistsException("Client with email " + dto.getEmail() + " already exists.");
        });

        Client client = clientMapper.toEntity(dto);
        Client savedClient = clientRepository.save(client);
        return clientMapper.toDto(savedClient);
    }

    @Override
    public ClientResponseDto findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));
        return clientMapper.toDto(client);
    }

    @Override
    public Page<ClientResponseDto> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable).map(clientMapper::toDto);
    }

    @Override
    public ClientResponseDto update(Long id, ClientRequestDto dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));

        // Check if the new email is already taken by another client
        clientRepository.findByEmail(dto.getEmail()).ifPresent(existingClient -> {
            if (!existingClient.getId().equals(id)) {
                throw new EntityExistsException("Email " + dto.getEmail() + " is already in use by another client.");
            }
        });

        clientMapper.updateFromDto(dto, client);
        Client updatedClient = clientRepository.save(client);
        return clientMapper.toDto(updatedClient);
    }

    @Override
    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }
}