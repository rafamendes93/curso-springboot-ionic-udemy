package com.rafael.cursomc.cursomc.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class S3Service {

    private Logger LOG = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private AmazonS3 s3client;

    @Value("${s3.bucket}")
    private String bucketName;

    public void uploadFiles(String localPath){

        try {

            File file = new File(localPath);
            s3client.putObject(new PutObjectRequest(bucketName, file.getName(), file));
            LOG.info(" ------------------------------- ");
            LOG.info("|                               |");
            LOG.info("| Upload realizado com sucesso! |");
            LOG.info("|                               |");
            LOG.info(" ------------------------------- ");
        }catch (AmazonServiceException e){
            LOG.info("S3Service.java -> AmazonServiceException: " + e.getErrorMessage());
            LOG.info("Status code: " + e.getErrorCode());
            LOG.info(" ------------------------------- ");
            LOG.info("|                               |");
            LOG.info("|         UPLOAD FAIL!!         |");
            LOG.info("|                               |");
            LOG.info(" ------------------------------- ");

        }catch (AmazonClientException e){
            LOG.info("S3Service.java -> AmazonClientException: " + e.getMessage());
            LOG.info(" ------------------------------- ");
            LOG.info("|                               |");
            LOG.info("|         UPLOAD FAIL!!         |");
            LOG.info("|                               |");
            LOG.info(" ------------------------------- ");
        }

    }

}
