package com.cliente.hexagonal.application.core.usecase;

import com.cliente.hexagonal.adapters.out.FindCustomerByIdAdapter;
import com.cliente.hexagonal.adapters.out.repository.CustomerRepository;
import com.cliente.hexagonal.adapters.out.repository.entity.CustomerEntity;
import com.cliente.hexagonal.adapters.out.repository.mapper.CustomerEntityMapper;
import com.cliente.hexagonal.application.core.domain.Customer;
import com.cliente.hexagonal.application.ports.out.FindCustomerByIdOutputPort;
import com.cliente.hexagonal.config.CacheConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {CacheConfig.class, FindCustomerByIdCacheTest.Config.class})
class FindCustomerByIdCacheTest {

    @Configuration
    static class Config {

        @Bean
        public CustomerRepository customerRepository() {
            return mock(CustomerRepository.class);
        }

        @Bean
        public CustomerEntityMapper customerEntityMapper() {
            return mock(CustomerEntityMapper.class);
        }

        @Bean
        public FindCustomerByIdAdapter findCustomerByIdAdapter(CustomerRepository repository,
                                                               CustomerEntityMapper mapper) {
            FindCustomerByIdAdapter adapter = new FindCustomerByIdAdapter();
            ReflectionTestUtils.setField(adapter, "customerRepository", repository);
            ReflectionTestUtils.setField(adapter, "customerEntityMapper", mapper);
            return adapter;
        }
    }

    @Autowired
    private FindCustomerByIdOutputPort adapter;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerEntityMapper customerEntityMapper;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void clearCache() {
        cacheManager.getCache("customers").clear();
        reset(customerRepository, customerEntityMapper);
    }

    @Test
    void shouldHitDatabaseOnlyOnceForSameId() {
        CustomerEntity entity = new CustomerEntity();
        when(customerRepository.findById("id-1")).thenReturn(Optional.of(entity));
        when(customerEntityMapper.toCustomer(entity)).thenReturn(new Customer());

        adapter.find("id-1");
        adapter.find("id-1");
        adapter.find("id-1");

        verify(customerRepository, times(1)).findById("id-1");
    }

    @Test
    void shouldHitDatabaseForDifferentIds() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());

        adapter.find("id-1");
        adapter.find("id-2");

        verify(customerRepository, times(1)).findById("id-1");
        verify(customerRepository, times(1)).findById("id-2");
    }
}
