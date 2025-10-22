package com.service.wallet.processor.config.sqs;

public interface QueueProperty {

    String getUrl();
    Integer getMaxNumberOfMessages();
}
