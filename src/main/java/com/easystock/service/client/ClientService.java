package com.easystock.service.client;

import com.easystock.dto.client.ClientRequestDto;
import com.easystock.dto.client.ClientResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {
    ClientResponseDto create(ClientRequestDto dto);
    ClientResponseDto findById(Long id);
    Page<ClientResponseDto> findAll(Pageable pageable);
    ClientResponseDto update(Long id, ClientRequestDto dto);
    void delete(Long id);
}