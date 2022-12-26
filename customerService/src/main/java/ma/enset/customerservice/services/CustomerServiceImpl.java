package ma.enset.customerservice.services;

import ma.enset.customerservice.dto.CustomerRequestDTO;
import ma.enset.customerservice.dto.CustomerResponseDTO;
import ma.enset.customerservice.entities.Customer;
import ma.enset.customerservice.mappers.CustomerMapper;
import ma.enset.customerservice.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerResponseDTO save(CustomerRequestDTO customerRequestDTO) {
        //mapping objet objet
        /*Customer customer=new Customer();
        customer. setId(customerRequestDTO.getId());
        customer. setName (customerRequestDTO.getName());
        customer. setEmail(customerRequestDTO.getEmail());*/

        //customer.setId(UUID.randomUUID().toString());

        //mapping objet objet
        /*CustomerResponseDTO customerResponseDTO=new CustomerResponseDTO();
        customerResponseDTO.setId(saveCustomer.getId());
        customerResponseDTO. setName (saveCustomer.getName());
        customerResponseDTO. setEmail(saveCustomer.getEmail());*/

        Customer customer=customerMapper .customerRequestDTOCustomer (customerRequestDTO) ;
        Customer saveCustomer=customerRepository.save(customer);
        CustomerResponseDTO customerResponseDTO=customerMapper.customerToCustomerResponseDTO(saveCustomer);
        return customerResponseDTO;

    }

    @Override
    public CustomerResponseDTO getCustomer(String id) {
        Customer customer=customerRepository. findById(id) .get();
        return customerMapper. customerToCustomerResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO update(CustomerRequestDTO customerRequestDTO) {
        Customer customer=customerMapper.customerRequestDTOCustomer (customerRequestDTO) ;
        Customer updatedCustomer = customerRepository.save(customer) ;
        return customerMapper.customerToCustomerResponseDTO(updatedCustomer) ;
    }

    @Override
    public List<CustomerResponseDTO> listCustomers() {
        List<Customer> customers=customerRepository.findAll();
        List<CustomerResponseDTO> customerResponseDTOS=
                customers.stream()
                        .map(cust->customerMapper.customerToCustomerResponseDTO(cust))
                        .collect(Collectors.toList());
        return customerResponseDTOS;
    }

    @Override
    public void deleteCustomer(String id) {
        Customer customer=customerRepository. findById(id) .get();
        customerRepository.delete(customer);
    }
}






