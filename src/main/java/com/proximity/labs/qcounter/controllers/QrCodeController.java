package com.proximity.labs.qcounter.controllers;

import com.proximity.labs.qcounter.exception.AppException;
import com.proximity.labs.qcounter.service.QrCodeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;

@RestController
@RequestMapping("/qr")
@Api(tags = "QR-Code", value = "Generate a QR-Code. It's not secured by default. ")
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
     * @param queueId who the qr code is for
     * @return a buffered image of qr code
     */
    @ApiOperation(value = "Return a QR-Code PNG image that contains a url to the queue specified by the path variable")
    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE, value = "/{queue_id}")
    public ResponseEntity<BufferedImage> qrCode(@PathVariable("queue_id") String queueId) {
        try {
            return ResponseEntity.ok(qrCodeService.generateQRCodeImage(rootUrl));
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(String.format("Failed to generate qr for queue with id %s", queueId));
        }
    }
}