package ma.enset.billingservice.services;

import lombok.AllArgsConstructor;
import ma.enset.billingservice.dtos.InvoiceRequestDTO;
import ma.enset.billingservice.dtos.InvoiceResponseDTO;
import ma.enset.billingservice.entities.Invoice;
import ma.enset.billingservice.exceptions.CustomerNotFoundException;
import ma.enset.billingservice.mappers.InvoiceMapper;
import ma.enset.billingservice.models.Customer;
import ma.enset.billingservice.openfeign.CustomerRestClient;
import ma.enset.billingservice.repositories.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private InvoiceRepository invoiceRepository;
    private InvoiceMapper invoiceMapper;
    private CustomerRestClient customerRestClient;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper,
                              CustomerRestClient customerRestClient) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.customerRestClient = customerRestClient;
    }

    @Override
    public InvoiceResponseDTO save(InvoiceRequestDTO invoiceRequestDTO) throws CustomerNotFoundException {
        Customer customer=null;
        try {
            customer = customerRestClient.getCustomer(invoiceRequestDTO.getCustomerID());
        }
        catch (Exception e){
            throw new CustomerNotFoundException("Customer not found");
        }
        Invoice invoice=invoiceMapper.fromInvoiceRequestDTO(invoiceRequestDTO);
        invoice.setId(UUID.randomUUID().toString());
        invoice.setDate(new Date());
        Invoice savedInvoice=invoiceRepository.save(invoice);
        savedInvoice.setCustomer(customer);
        return invoiceMapper.fromInvoice(savedInvoice);
    }

    @Override
    public InvoiceResponseDTO getInvoice(String invoiceId)
    {
        Invoice invoice=invoiceRepository.findById(invoiceId).get();
        Customer customer=customerRestClient.getCustomer(invoice.getCustomerID());
        invoice.setCustomer(customer);
        return invoiceMapper.fromInvoice(invoice);
    }

    @Override
    public List<InvoiceResponseDTO> invoicesByCustomerId(String customerId) {
        List<Invoice> invoices=invoiceRepository.findByCustomerID(customerId);
        for (Invoice invoice:invoices) {
            Customer customer=customerRestClient.getCustomer(invoice.getCustomerID());
            invoice.setCustomer(customer);

        }
        return invoices
                .stream()
                .map(invoice -> invoiceMapper.fromInvoice(invoice))
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponseDTO> allInvoices() {
        List<Invoice> invoices=invoiceRepository.findAll();
        for (Invoice invoice:invoices) {
            Customer customer=customerRestClient.getCustomer(invoice.getCustomerID());
            invoice.setCustomer(customer);

        }
        return invoices
                .stream()
                .map(inv -> invoiceMapper.fromInvoice(inv))
                .collect(Collectors.toList());
    }
}
