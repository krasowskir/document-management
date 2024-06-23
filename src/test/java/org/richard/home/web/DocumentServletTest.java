package org.richard.home.web;

import jakarta.servlet.http.Part;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DocumentServletTest {


    /*
     POST /api/document HTTP/1.1
     Host: localhost:8080
     User-Agent: curl/7.79.1
     Accept: /
     Content-Length: 211
     Content-Type: multipart/form-data; boundary=------------------------9dd36b0576a69928

     --------------------------9dd36b0576a69928
     Content-Disposition: form-data; name="testFile"; filename="tikataka.txt"
     Content-Type: text/plain

     Richard is best!

     --------------------------9dd36b0576a69928--
      */
    @Test
    void shouldExtractFilenameFromMultipart() {

        // given
        var objectUnderTest = new DocumentServlet();
        var part = mock(Part.class);
        when(part.getHeader("Content-Disposition")).thenReturn("Content-Disposition: form-data; name=\"testFile\"; filename=\"tikataka.txt\"");

        // when
        var result = objectUnderTest.extractFileNameFromHeader.apply(part);
        Assertions.assertThat(result).isEqualTo("tikataka.txt");
    }

//    @Test
//    void shouldUploadDocumentsViaPost() throws ServletException, IOException {
//        //given
//        var objectUnderTest = new DocumentServlet();
//        var documentService = mock(DocumentService.class);
//        var servletContext = mock(ServletContext.class);
//        when(servletContext.getAttribute("documentService")).thenReturn( documentService);
//        when(documentService.uploadDocument(ArgumentMatchers.any(InputStream.class),anyString())).thenReturn("123");
//
//        //and
//        when(objectUnderTest.getServletContext()).thenReturn(servletContext);
//
//        //and
//        var request = mock(HttpServletRequest.class);
//        var mockedPart = mock(Part.class);
//        when(mockedPart.getSize()).thenReturn(100L);
//        when(mockedPart.getInputStream()).thenReturn(mock(InputStream.class));
//        when(mockedPart.getHeader("Content-Disposition"))
//                .thenReturn("Content-Disposition: form-data; name=\"testFile\"; filename=\"tikataka.txt\"");
//        when(request.getParts()).thenReturn(List.of(mockedPart));
//
//        // when
//        objectUnderTest.doPost(request, mock(HttpServletResponse.class));
//
//
//    }

}