package io.github.erp.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import io.github.erp.FinanceErpApp;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = FinanceErpApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
