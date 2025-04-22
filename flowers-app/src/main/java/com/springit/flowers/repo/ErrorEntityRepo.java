package com.springit.flowers.repo;

import com.springit.flowers.entity.ErrorEntity;
import com.springit.flowers.entity.ErrorEntityId;
import com.springit.flowers.util.ErrorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

//@Repository
public interface ErrorEntityRepo extends JpaRepository<ErrorEntity, ErrorEntityId> {

//    @Transactional
//    @Modifying
//    @Query("update ErrorEntity ee set ee.status = :status, ee.okDate = :date " +
//            "where ee.errorEntityId.flowId = :flowId " +
//            "and ee.errorEntityId.destinationId = :destinationId " +
//            "and ee.errorEntityId.jmsMessageId = :jmsMessageId")
//    int updateStateById(@Param("flowId") int flowId,
//                         @Param("destinationId") int destinationId,
//                         @Param("jmsMessageId") String jmsMessageId,
//                         @Param("status") ErrorStatus status,
//                         @Param("date")  Date date);


}
