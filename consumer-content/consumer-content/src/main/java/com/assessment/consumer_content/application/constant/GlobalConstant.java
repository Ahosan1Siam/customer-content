package com.assessment.consumer_content.application.constant;

import java.time.Duration;

public interface GlobalConstant {
    Integer BATCH_SIZE = 200;
    Integer DIVISOR_SIZE= 500;
    Duration TIMEOUT_DURATION = Duration.ofSeconds(1);
}
