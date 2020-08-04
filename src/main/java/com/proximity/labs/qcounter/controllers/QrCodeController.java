package com.proximity.labs.qcounter.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proximity.labs.qcounter.data.dto.request.QrCodeRequest;
import com.proximity.labs.qcounter.service.QrCodeService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.awt.image.BufferedImage;

@RestController
@RequestMapping("/qr")
public class QrCodeController {
    private final QrCodeService qrCodeService;
    private final String rootUrl;
    private static final Logger logger = Logger.getLogger(UserController.class);
    
    @Autowired
    public QrCodeController(QrCodeService qrCodeService, @Value("${app.root.url}") String rootUrl) {
        this.qrCodeService = qrCodeService;
        this.rootUrl = rootUrl;
    }

    /**
     * Work in progress. currently returning github pages url
     * will return qr code that contains url to join a queue
     *
     * @param qrCodeRequest
     * @return
     * @throws Exception
     */
    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> qrCode(@RequestBody QrCodeRequest qrCodeRequest)
    throws Exception {
        return ResponseEntity.ok(qrCodeService.generateQRCodeImage(rootUrl));
    }

}