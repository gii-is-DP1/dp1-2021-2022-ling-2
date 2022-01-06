package org.springframework.ntfh.entity.audit;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

public class auditService {

    AuditReader reader = AuditReaderFactory.get(entityManager);
}
