package com.proximity.labs.qcounter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@EntityScan(basePackageClasses = { QcounterApplication.class, Jsr310JpaConverters.class })
public class QcounterApplication {

  public static void main(String[] args) {
    SpringApplication.run(QcounterApplication.class, args);
  }
}