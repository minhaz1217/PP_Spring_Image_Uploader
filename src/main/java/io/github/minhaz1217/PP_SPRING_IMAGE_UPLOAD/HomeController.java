package io.github.minhaz1217.PP_SPRING_IMAGE_UPLOAD;


import com.fasterxml.jackson.databind.ser.Serializers;
import com.sun.org.apache.xerces.internal.util.HTTPInputSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.Request;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Controller
public class HomeController {

    private static final String BASE_PATH = "/images";
    private static final String FILENAME = "{filename:.+}";
    private final ImageService imageService;

    @Autowired
    public HomeController(ImageService imageService){
        this.imageService = imageService;
    }

    @RequestMapping(value = "/")
    public String index(Model model, Pageable pageable){
        final Page<Image> page = imageService.findPage(pageable);
        model.addAttribute("page", page);
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, value = BASE_PATH + "/" + FILENAME + "/raw")
    @ResponseBody
    public ResponseEntity<?> oneRawImage(@PathVariable String filename){
        try {
            Resource file = imageService.findOneImage(filename);
            return ResponseEntity.ok()
                    .contentLength(file.contentLength())
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(file.getInputStream()));
        }catch (IOException e){
            return ResponseEntity.badRequest()
                    .body("COULDn't FIND "+ filename + " => " + e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.POST, value = BASE_PATH)
    public String createFile(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes){

        try{
            imageService.createImage(file);
            redirectAttributes.addFlashAttribute("flash.message", "Successfully uploaded " + file.getOriginalFilename());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("flash.message", "failed to upload " + file.getOriginalFilename());
        }
        return "redirect:/";

    }

    @RequestMapping(method = RequestMethod.DELETE, value = BASE_PATH + "/" + FILENAME)
    @ResponseBody
    public ResponseEntity<?> deleteFile(@PathVariable String filename){
        try{
            imageService.deleteImage(filename);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("SUCCESSFULLY DELETED " + filename);
        } catch (IOException e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete " + filename + " => " + e.getMessage() );
        }
    }

}
