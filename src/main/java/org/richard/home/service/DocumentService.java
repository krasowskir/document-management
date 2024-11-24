package org.richard.home.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface DocumentService {

    String uploadDocument(InputStream in, String fileName) throws JsonProcessingException;

    byte[] getDocument(String fileName) throws IOException;

    byte[] getDocumentByObjectId(String objectId);
}
