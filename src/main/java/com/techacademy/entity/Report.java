package com.techacademy.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "report")
public class Report {

    /** 主キー。自動生成 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /* 登録日時 */
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportDate;

    /** 名前。20桁。null不許可 */
    @Column(nullable = false)
    private String title;

    /** フィールド名[削除フラグ]. */
    @Column(nullable = false)
    @Type(type = "text")
    private String content;

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    /* 登録日時 */
    @Column(name = "created_at")
    private LocalDateTime created_at;

    /* 更新日時 */
    @Column(name = "updated_at")
    private LocalDateTime updated_at;

}
