package com.service.wallet.api.queue.consumer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public abstract class SqsConsumer<T> {

    public void consume(@NotNull @Valid T message) {

    }
}
