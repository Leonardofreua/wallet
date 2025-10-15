package com.service.wallet.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.service.wallet.api.bean.TransferBean;
import com.service.wallet.api.dto.api.request.TransferRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TransferMapper {
    
    @Mapping(target = "sourceWalletId", source = "walletId")
    TransferBean toTransferBean(@Positive long walletId, @Valid @NotNull TransferRequest request);
}
