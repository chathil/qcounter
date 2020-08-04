package com.proximity.labs.qcounter.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QrCodeServiceTest {

    @Autowired
    QrCodeService qrCodeService;

    @Test
    void generateQRCodeImage() throws Exception {
        BufferedImage qrCode = qrCodeService.generateQRCodeImage("Hello World");
        assertEquals(200, qrCode.getWidth());
        assertEquals(200, qrCode.getHeight());
    }
}