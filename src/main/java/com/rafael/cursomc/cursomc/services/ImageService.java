package com.rafael.cursomc.cursomc.services;

import com.rafael.cursomc.cursomc.services.exception.FileException;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageService {

    /**
     * Esse método pega uma imagem e verifica a extensão, caso seja png ele usa o método pngToJpg() para converter
     * se não, ele apenas retorna a imagem do tipo BufferedImage
     * @param uploadedFile imagem que foi feita upload
     * @return retorna imagem do tipo BufferedImage
     */
    public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile){
        String ext = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());

        if (!"png".equals(ext)&&!"jpg".equals(ext)){
            throw new FileException("Somente imagens jpg e png são permitidas");
        }

        try {
            BufferedImage img = ImageIO.read(uploadedFile.getInputStream());
            if ("png".equals(ext)){
                img = pngToJpg(img);
            }
            return img;
        } catch (IOException e) {
            throw new FileException("Erro ao ler arquivo");
        }

    }

    /**
     * Esse método converter imagens png para jpeg, para padronizar todas as imagens gravadas com extensão jpeg
     * @param img a imagem a ser convertida
     * @return retorna a imagem convertida para jpeg
     */
    public BufferedImage pngToJpg(BufferedImage img) {
        BufferedImage jpgImage = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);

        jpgImage.createGraphics().drawImage(img,0,0, Color.WHITE,null);
        return jpgImage;
    }

    public InputStream getInputStream(BufferedImage img, String extension){
        try{
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(img,extension,os);
            return new ByteArrayInputStream(os.toByteArray());
        }catch (IOException e){
            throw new FileException("Erro ao ler arquivo");
        }
    }

    /**
     * Esse método corta a imagem, primeiro ele pega verifica qual é menor, a altura e a largura e guarda o
     * valor em pixels.
     * @param sourceImg imagem a ser cortada
     * @return retorna a imagem cortada
     */
    public BufferedImage cropSquare(BufferedImage sourceImg){

        int min = (sourceImg.getHeight() <= sourceImg.getWidth())? sourceImg.getHeight() : sourceImg.getWidth();

        return Scalr.crop(
                sourceImg,
                (sourceImg.getWidth()/2) - (min/2),
                (sourceImg.getHeight()/2) - (min/2),
                min,
                min);
    }

    /**
     * Esse método redimensiona a imagem baseada no valor de size.
     * @param sourceImg imagem a ser redimensionada
     * @param size valor em pixels para redimensionar a altura e a largura, esse valor é pego na chave "img.profile.size"
     * @return retorna a imagem redimensionada
     */
    public BufferedImage resize(BufferedImage sourceImg, int size){
        return Scalr.resize(sourceImg,Scalr.Method.ULTRA_QUALITY,size);
    }

}
