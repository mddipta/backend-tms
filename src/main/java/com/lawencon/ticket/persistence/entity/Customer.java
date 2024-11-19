package com.lawencon.ticket.persistence.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("deprecation")
@Entity
@Table(name = "tb_customers",
        uniqueConstraints = {
                @UniqueConstraint(name = "customer_company_bk", columnNames = {"company_id"}),
                @UniqueConstraint(name = "customer_user_bk", columnNames = {"user_id"})})
@Getter
@Setter
@SQLDelete(sql = "UPDATE tb_customers SET deleted_at = NOW() WHERE id = ? AND version = ?")
@Where(clause = "deleted_at IS NULL")
public class Customer extends MasterEntity {

    @OneToOne
    @JoinColumn(name = "company_id", nullable = false, unique = true, referencedColumnName = "id")
    private Company company;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true, referencedColumnName = "id")
    private User user;

    @OneToOne
    @JoinColumn(name = "pic_id", nullable = true, unique = true, referencedColumnName = "id")
    private User picUser;
}
