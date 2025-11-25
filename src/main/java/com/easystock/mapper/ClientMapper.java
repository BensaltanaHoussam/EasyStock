package com.easystock.mapper;

import com.easystock.dto.client.ClientRequestDto;
import com.easystock.dto.client.ClientResponseDto;
import com.easystock.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientResponseDto toDto(Client client);
    Client toEntity(ClientRequestDto dto);
    void updateFromDto(ClientRequestDto dto, @MappingTarget Client client);
}