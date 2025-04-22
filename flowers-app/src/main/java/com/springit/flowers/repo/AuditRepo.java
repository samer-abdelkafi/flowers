package com.springit.flowers.repo;



import com.springit.flowers.entity.AuditEntity;
import com.springit.flowers.entity.AuditEntity.AuditEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface AuditRepo extends JpaRepository<AuditEntity, AuditEntityId>, JpaSpecificationExecutor<AuditEntity> {


}
