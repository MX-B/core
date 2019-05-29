package io.gr1d.core.datasource.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.gr1d.auth.keycloak.LoggedUser;
import io.gr1d.core.datasource.model.BaseModel;
import io.gr1d.core.datasource.repository.AuditEntityRepository;
import io.gr1d.core.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
public class AuditListener {

    @PostPersist
    private void postInsert(BaseModel entity) {
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setCreatedAt(LocalDateTime.now());
        auditEntity.setOperation(AuditEntity.INSERT_OPERATION);
        auditEntity.setRegisterContent(extractJson(entity));
        auditEntity.setRegisterName(entity.getClass().getSimpleName());
        auditEntity.setRegisterId(entity.getId());
        auditEntity.setRegisterUuid(entity.getUuid());
        setUserInfo(auditEntity);
        perform(auditEntity);
    }

    @PostUpdate
    private void postUpdate(BaseModel entity) {
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setCreatedAt(LocalDateTime.now());
        auditEntity.setOperation(AuditEntity.UPDATE_OPERATION);
        auditEntity.setRegisterContent(extractJson(entity));
        auditEntity.setRegisterName(entity.getClass().getSimpleName());
        auditEntity.setRegisterId(entity.getId());
        auditEntity.setRegisterUuid(entity.getUuid());
        setUserInfo(auditEntity);
        perform(auditEntity);
    }


    @PostRemove
    private void postDelete(BaseModel entity) {
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setCreatedAt(LocalDateTime.now());
        auditEntity.setOperation(AuditEntity.DELETE_OPERATION);
        auditEntity.setRegisterContent(extractJson(entity));
        auditEntity.setRegisterName(entity.getClass().getSimpleName());
        auditEntity.setRegisterId(entity.getId());
        auditEntity.setRegisterUuid(entity.getUuid());
        setUserInfo(auditEntity);
        perform(auditEntity);
    }


    @Transactional(propagation = Propagation.MANDATORY)
    private void perform(AuditEntity entity) {
        AuditEntityRepository repository = BeanUtil.getBean(AuditEntityRepository.class);
        repository.save(entity);
    }

    private String extractFieldValues (Object entity) {
        StringBuilder builder = new StringBuilder();
        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field: fields) {
            field.setAccessible(true);
            try {
                builder.append(field.getName() + ": ");
                builder.append(field.get(entity) != null ? field.get(entity).toString():"");
                builder.append(";");
            } catch (IllegalAccessException exception) {
                log.info("Tried to access illegal field "+ field + " while auditing.");
            }
        }
        return builder.toString();
    }

    private String extractJson (Object entity) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
        try {
            return mapper.writeValueAsString(entity);
        } catch (JsonProcessingException exception) {
            log.info("Failed while serializing object for auditing.");
            return extractFieldValues(entity);
        }
    }

    private void setUserInfo(AuditEntity audit) {
        if (checkActiveProfileTest()) {
            setTestInfo(audit);
            return;
        }
        LoggedUser loggeduser = BeanUtil.getBean(LoggedUser.class);
        audit.setUserInfo(loggeduser.getEmail());
        audit.setUserKeycloakId(loggeduser.getId());

        Instant instant = Instant.ofEpochSecond(loggeduser.getIssuedAt());
        LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        audit.setTokenIssuedAt(date);
    }

    private boolean checkActiveProfileTest() {
        Environment environment = BeanUtil.getBean(Environment.class);
        for (String env: environment.getActiveProfiles()) {
            if (env.equals("test")) return true;
        }
        return false;
    }

    private void setTestInfo(AuditEntity audit) {
        audit.setUserInfo("user@test.com");
        audit.setUserKeycloakId("123");
        audit.setTokenIssuedAt(LocalDateTime.now());
    }
}
