package com.rafael.cursomc.cursomc.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.rafael.cursomc.cursomc.services.exception.FileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class S3Service {

    private Logger LOG = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private AmazonS3 s3client;

    @Value("${s3.bucket}")
    private String bucketName;

    public URI uploadFiles(MultipartFile multipartFile){

        try {

            String fileName = multipartFile.getOriginalFilename();
            InputStream is = multipartFile.getInputStream();
            String contentType = multipartFile.getContentType();
            return uploadFiles(is, fileName,contentType);

        } catch (IOException e) {
            throw new FileException("Erro de I/O: " + e.getMessage());
        }


    }

    public URI uploadFiles(InputStream is, String fileName, String contentType){

        try {

            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType(contentType);
            LOG.info("Iniciando upload");
            s3client.putObject(bucketName,fileName,is,meta);
            LOG.info(" ------------------------------- ");
            LOG.info("|                               |");
            LOG.info("| Upload realizado com sucesso! |");
            LOG.info("|                               |");
            LOG.info(" ------------------------------- ");

            return s3client.getUrl(bucketName,fileName).toURI();
        } catch (URISyntaxException e) {
            throw new FileException("Erro ao converter URL para URI");
        }

    }

}
