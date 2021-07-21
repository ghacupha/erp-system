package io.github.erp.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import io.github.erp.ErpUaaApp;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = ErpUaaApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
