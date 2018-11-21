package io.github.minhaz1217.PP_SPRING_IMAGE_UPLOAD;

import org.apache.tomcat.util.http.fileupload.UploadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageService {


    private static String UPLOAD_ROOT = "upload-dir";
    private final ImageRepository imageRepository;
    private final ResourceLoader resourceLoader;

    @Autowired
    public ImageService(ImageRepository repository, ResourceLoader resourceLoader){
        this.imageRepository = repository;
        this.resourceLoader = resourceLoader;
    }

    public Page<Image> findPage(Pageable pageable){
        return imageRepository.findAll(pageable);
    }

    public Resource findOneImage(String filename){
        return resourceLoader.getResource("file:" + UPLOAD_ROOT +"/" + filename);
    }
    public void createImage(MultipartFile file) throws IOException {

        if(!file.isEmpty()){
            Files.copy(file.getInputStream(), Paths.get(UPLOAD_ROOT, file.getOriginalFilename()));
            imageRepository.save(new Image(file.getOriginalFilename()));
        }

    }
    public void deleteImage(String fileName) throws IOException {
        final Image byName = imageRepository.findByName(fileName);
        imageRepository.delete(byName);
        Files.delete(Paths.get(UPLOAD_ROOT, fileName));
    }

    @Bean
    CommandLineRunner setUp(ImageRepository repository) throws IOException {

        return (args) -> {
            FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));
            Files.createDirectory(Paths.get(UPLOAD_ROOT));
            FileCopyUtils.copy("Text File1", new FileWriter(UPLOAD_ROOT + "/test"));
            repository.save(new Image("test"));
            FileCopyUtils.copy("Text File2", new FileWriter(UPLOAD_ROOT + "/test2"));
            repository.save(new Image("test2"));


            FileCopyUtils.copy("Text File3", new FileWriter(UPLOAD_ROOT + "/test3"));
            repository.save(new Image("test3"));
        };



    }



}
