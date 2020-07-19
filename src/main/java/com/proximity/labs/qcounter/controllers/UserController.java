package com.proximity.labs.qcounter.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.activation.MimetypesFileTypeMap;
import javax.xml.bind.DatatypeConverter;

import com.proximity.labs.qcounter.data.models.user.UserEntity;
import com.proximity.labs.qcounter.data.models.user.UserRepository;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @GetMapping("/test")
  public String test(@RequestParam final String name) {
    return "";
  }

  public static String hashData(final String data) throws NoSuchAlgorithmException, IOException {
    final MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(data.getBytes());
    final byte[] b = md.digest();
    final StringBuffer sb = new StringBuffer();
    for (final byte b1 : b) {
      sb.append(Integer.toHexString(b1 & 0xff).toString());
    }
    return sb.toString();
  }

  @PatchMapping("/ava")
  public String ava(@NonNull @RequestParam("file") final MultipartFile file, @RequestParam final String email,
      @RequestParam final String password) throws IllegalStateException, IOException, NoSuchAlgorithmException {
    // final Path root = Paths.get("uploads");

    // final Path root = Paths.get("uploads");

    // try {
    // Files.createDirectory(root);

    // } catch (IOException e) {
    // throw new RuntimeException("Could not initialize folder for upload!");
    // }

    final String baseDir = "D:/Project/Springboot/uploads/";
    final UserEntity user = userRepository.findFirstByEmailAndPassword(email, password);
    final String extension = FilenameUtils.getExtension(file.getOriginalFilename());
    final long size = file.getSize() / 1024;
    final long maxSize = 500;

    File f = new File(file.getOriginalFilename());
    String mimetype = new MimetypesFileTypeMap().getContentType(f);
    String type = mimetype.split("/")[0];
    // cek file yang di upload gambar atau bukan
    if (type.equals("image")) {
      // cek ukuran file
      if (size <= maxSize) {
        file.transferTo(new File(baseDir + hashData(user.getId() + user.getName()) + "." + extension));
      } else {
        System.out.println("Image size cannot exceed 500kb");
      }
    } else {
      System.out.println("It's NOT an image");
    }
    return null;
  }

}