package org.richard.home.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface DocumentService {

    String uploadDocument(InputStream in, String fileName) ;

    byte[] getDocument(String fileName) throws IOException;
}
