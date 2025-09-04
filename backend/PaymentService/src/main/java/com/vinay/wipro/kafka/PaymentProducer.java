package com.vinay.wipro.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.vinay.wipro.entities.EmailDTO;

@Service
@RequiredArgsConstructor
public class PaymentProducer {

    private final KafkaTemplate<String, EmailDTO> kafkaTemplate;

    public void sendPayment(EmailDTO emailDTO) {
        kafkaTemplate.send("payment-topic", emailDTO);
    }
}