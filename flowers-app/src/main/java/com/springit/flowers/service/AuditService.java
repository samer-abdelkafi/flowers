package com.springit.flowers.service;


import com.springit.flowers.entity.AuditEntity;
import com.springit.flowers.repo.AuditRepo;
import lombok.AllArgsConstructor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;


@Service
@AllArgsConstructor
public class AuditService {

    private final AuditRepo auditRepo;


    public Object audit(Object payload, MessageHeaders headers, AuditEntity.AuditEntityStatus auditEntityStatus) {
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.getAuditEntityId().setFlowId(
                Optional.ofNullable(headers.get("FLOW_ID", Integer.class))
                        .map(Integer::longValue)
                        .orElse(0L)
        );
        auditEntity.getAuditEntityId().setJmsMessageId(
                Optional.ofNullable(headers.get("jms_messageId", String.class))
                        .orElse("NO_MESSAGE_ID_FOUND")
        );
        auditEntity.getAuditEntityId().setDestinationId(
                Optional.ofNullable(headers.get("DESTINATION_ID", Integer.class))
                        .orElse(0)
        );
        auditEntity.setInputDate(new Date());
        auditEntity.setUpdateDate(new Date());
        auditEntity.setTrno(Optional.ofNullable(headers.get("trno", String.class))
                .orElse("No TRNO provided"));
        auditEntity.setFilePath(Optional.ofNullable(headers.get(StorageService.FOLDER_MESSAGE_PATH, String.class))
                .orElse("NO_MESSAFE_FILE_PROVIDED"));

        auditEntity.setServer(Optional.ofNullable(headers.get("SUBSCRIBER_SERVER", String.class))
                .orElse("NO_SERVER_PROVIDED"));

        auditEntity.setStatus(auditEntityStatus);

        auditRepo.save(auditEntity);

        return payload;
    }

}
