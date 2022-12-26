package ma.enset.billingservice;

import ma.enset.billingservice.dtos.InvoiceRequestDTO;
import ma.enset.billingservice.exceptions.CustomerNotFoundException;
import ma.enset.billingservice.services.InvoiceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(InvoiceService invoiceService){
        return args -> {

            try {
                invoiceService.save(new InvoiceRequestDTO(BigDecimal.valueOf(76000),"C01"));
                invoiceService.save(new InvoiceRequestDTO(BigDecimal.valueOf(34000),"C02"));
                invoiceService.save(new InvoiceRequestDTO(BigDecimal.valueOf(54000),"C03"));
            } catch (CustomerNotFoundException e) {
                e.printStackTrace();
            }


        };
    }
}
