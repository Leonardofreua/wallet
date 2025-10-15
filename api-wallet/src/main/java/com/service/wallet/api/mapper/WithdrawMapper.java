package com.service.wallet.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.service.wallet.api.bean.WithdrawBean;
import com.service.wallet.api.dto.api.request.WithdrawRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface WithdrawMapper {

    WithdrawBean toWithdrawBean(@Positive long walletId, @Valid @NotNull WithdrawRequest request);
}
