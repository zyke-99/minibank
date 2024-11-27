package com.zyke.minibank.auditing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Entity
@RevisionEntity(MinibankRevisionListener.class)
@Table(name = "minibank_rev")
@Getter
@Setter
public class MinibankRevisionEntity extends DefaultRevisionEntity {

    @Column(name = "revised_by")
    private String revisedBy;
}
