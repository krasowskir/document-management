package org.richard.home.domain;

import jakarta.annotation.Nonnull;

import java.time.*;
import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class DocumentUploadedEvent {

    private UUID id;
    private String fileName;
    private String objectId;
    private LocalDateTime creationDateTime;

    private DocumentUploadedEvent() {
    }

    private DocumentUploadedEvent(String fileName, String objectId) {
        this.id = UUID.randomUUID();
        this.creationDateTime = LocalDateTime.now();
        this.fileName = fileName;
        this.objectId = objectId;
    }

    public static DocumentUploadedEvent create(@Nonnull final String fileName, @Nonnull final String objectId) {
        if (requireNonNull(fileName).isEmpty() || requireNonNull(objectId).isEmpty()) {
            throw new IllegalArgumentException("objectId or fileName cannot be null or empty");
        }
        return new DocumentUploadedEvent(fileName, objectId);
    }

    public UUID getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getObjectId() {
        return objectId;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DocumentUploadedEvent that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DocumentUploadedEvent{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", objectId='" + objectId + '\'' +
                ", creationDateTime=" + creationDateTime +
                '}';
    }
}
