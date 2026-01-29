package org.example.securityservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;    // nazwaPliku
    private String fileType;    // typPliku (np. image/jpeg, video/mp4, application/pdf)
    private String url;         // url do pliku na serwerze lub w chmurze (S3/Azure Storage)

    @ManyToOne
    @JoinColumn(name = "log_entry_id")
    private LogEntry logEntry;  // powiązanie z konkretnym wpisem w dzienniku
}