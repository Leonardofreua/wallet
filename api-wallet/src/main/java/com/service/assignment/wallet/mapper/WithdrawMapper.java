package com.service.assignment.wallet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.service.assignment.wallet.bean.WithdrawBean;
import com.service.assignment.wallet.dto.api.request.WithdrawRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface WithdrawMapper {

    WithdrawBean toWithdrawBean(@Positive long walletId, @Valid @NotNull WithdrawRequest request);
}
