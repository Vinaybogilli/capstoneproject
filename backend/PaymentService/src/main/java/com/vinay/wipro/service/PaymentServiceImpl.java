package com.vinay.wipro.service;

import com.vinay.wipro.entities.AuditDTO;
import com.vinay.wipro.entities.EmailDTO;
import com.vinay.wipro.entities.Payment;
import com.vinay.wipro.entities.PaymentStatus;
import com.vinay.wipro.feign.AccountFeign;
import com.vinay.wipro.feign.AuditFeign;
import com.vinay.wipro.feign.CustomerFeign;
import com.vinay.wipro.kafka.PaymentProducer;
import com.vinay.wipro.repo.PaymentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final AccountFeign accountClient;
    private final AuditFeign auditFeign;
    private final CustomerFeign customerFeign;
    private final PaymentProducer paymentProducer;

    @Transactional
    public Payment processInternalPayment(Payment payment) {
        PaymentStatus status = PaymentStatus.SUCCESS;

        // ---------------- Critical: fetch sender ----------------
        AccountFeign.AccountResponse sender;
        try {
            sender = accountClient.getAccountByNumber(payment.getFromAccountNumber());
            if (sender == null) throw new RuntimeException("Sender account not found");
        } catch (Exception e) {
            throw new RuntimeException("Sender account service unavailable", e);
        }

        // ---------------- Critical: fetch receiver ----------------
        AccountFeign.AccountResponse receiver;
        try {
            receiver = accountClient.getAccountByNumber(payment.getToAccountNumber());
            if (receiver == null) throw new RuntimeException("Receiver account not found");
        } catch (Exception e) {
            throw new RuntimeException("Receiver account service unavailable", e);
        }

        // ---------------- Validate balance ----------------
        if (sender.getBalance() < payment.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        // ---------------- Update balances ----------------
        try {
            sender.setBalance(sender.getBalance() - payment.getAmount());
            accountClient.updateAccountBalance(sender.getAccountNumber(), sender.getBalance());

            receiver.setBalance(receiver.getBalance() + payment.getAmount());
            accountClient.updateAccountBalance(receiver.getAccountNumber(), receiver.getBalance());

            payment.setExternal(false);
            payment.setStatus(PaymentStatus.SUCCESS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update balances", e);
        }

        // ---------------- Save payment ----------------
        Payment savedPayment = paymentRepo.save(payment);

        // ---------------- Optional: Audit Logging ----------------
        try {
            AuditDTO auditDTO = new AuditDTO();
            auditDTO.setFromAccount(payment.getFromAccountNumber());
            auditDTO.setToAccount(payment.getToAccountNumber());
            auditDTO.setAmount(payment.getAmount());
            auditDTO.setStatus(payment.getStatus().name());
            auditDTO.setExternal(payment.isExternal());

            auditFeign.logTransaction(auditDTO);
        } catch (Exception e) {
            e.printStackTrace(); // log but do not fail payment
        }

        // ---------------- Optional: Email Notifications ----------------
        try {
            Long senderId = accountClient.getCustomerId(payment.getFromAccountNumber());
            Long receiverId = accountClient.getCustomerId(payment.getToAccountNumber());

            String senderEmail = customerFeign.getEmail(senderId);
            String receiverEmail = customerFeign.getEmail(receiverId);

            EmailDTO senderEmailDTO = new EmailDTO();
            senderEmailDTO.setEmail(senderEmail);
            senderEmailDTO.setSubject("Transaction Update");
            senderEmailDTO.setBody(payment.getStatus());
            senderEmailDTO.setAmount(payment.getAmount());

            EmailDTO receiverEmailDTO = new EmailDTO();
            receiverEmailDTO.setEmail(receiverEmail);
            receiverEmailDTO.setSubject("Transaction Update");
            receiverEmailDTO.setBody(payment.getStatus());
            receiverEmailDTO.setAmount(payment.getAmount());

            paymentProducer.sendPayment(senderEmailDTO);
            paymentProducer.sendPayment(receiverEmailDTO);
        } catch (Exception e) {
            e.printStackTrace(); // log but do not fail payment
        }

        return savedPayment;
    }

    @Transactional
    public Payment processExternalPayment(Payment payment) {
        PaymentStatus status = PaymentStatus.SUCCESS;

        // ---------------- Critical: fetch sender ----------------
        AccountFeign.AccountResponse sender;
        try {
            sender = accountClient.getAccountByNumber(payment.getFromAccountNumber());
            if (sender == null || sender.getBalance() < payment.getAmount()) {
                throw new RuntimeException("Insufficient balance or sender not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Sender account service unavailable", e);
        }

        // ---------------- Update balance ----------------
        try {
            sender.setBalance(sender.getBalance() - payment.getAmount());
            accountClient.updateAccountBalance(sender.getAccountNumber(), sender.getBalance());

            payment.setExternal(true);
            payment.setStatus(PaymentStatus.SUCCESS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update sender balance", e);
        }

        // ---------------- Save payment ----------------
        Payment savedPayment = paymentRepo.save(payment);

        // ---------------- Optional: Audit Logging ----------------
        try {
            AuditDTO auditDTO = new AuditDTO();
            auditDTO.setFromAccount(payment.getFromAccountNumber());
            auditDTO.setToAccount(payment.getToAccountNumber());
            auditDTO.setAmount(payment.getAmount());
            auditDTO.setStatus(payment.getStatus().name());
            auditDTO.setExternal(payment.isExternal());

            auditFeign.logTransaction(auditDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ---------------- Optional: Email Notifications ----------------
        try {
            Long senderId = accountClient.getCustomerId(payment.getFromAccountNumber());
            Long receiverId = accountClient.getCustomerId(payment.getToAccountNumber());

            String senderEmail = customerFeign.getEmail(senderId);
            String receiverEmail = customerFeign.getEmail(receiverId);

            EmailDTO senderEmailDTO = new EmailDTO();
            senderEmailDTO.setEmail(senderEmail);
            senderEmailDTO.setSubject("Transaction Update");
            senderEmailDTO.setBody(payment.getStatus());
            senderEmailDTO.setAmount(payment.getAmount());

            EmailDTO receiverEmailDTO = new EmailDTO();
            receiverEmailDTO.setEmail(receiverEmail);
            receiverEmailDTO.setSubject("Transaction Update");
            receiverEmailDTO.setBody(payment.getStatus());
            receiverEmailDTO.setAmount(payment.getAmount());

            paymentProducer.sendPayment(senderEmailDTO);
            paymentProducer.sendPayment(receiverEmailDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return savedPayment;
    }
}
