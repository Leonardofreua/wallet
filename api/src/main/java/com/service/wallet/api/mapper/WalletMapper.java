package com.service.wallet.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.service.wallet.api.domain.Wallet;
import com.service.wallet.api.dto.api.response.WalletResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface WalletMapper {

    WalletResponse fromWalletToResponse(@NotNull @Valid Wallet wallet);
}
