package com.aa.custhub.streamingrequestpoc.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController("/")
public class IndexController {
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public void index(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        handle(request, response);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response) throws
            IOException {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            byte[] bytes = new byte[3];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int read = 0;
            while (read > -1) {
                read = request.getInputStream().read(bytes);
                if (read > 0) {
                    bos.write(bytes, 0, read);
                }
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.addHeader("X-Stream", new String(bos.toByteArray()).substring(0, 100) + "...");
        } else { // this handler is to handle POST request
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}

