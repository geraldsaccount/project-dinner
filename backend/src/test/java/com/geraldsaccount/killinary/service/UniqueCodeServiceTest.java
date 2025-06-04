package com.geraldsaccount.killinary.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.context.ActiveProfiles;

import com.geraldsaccount.killinary.repository.CodedRepository;

@ActiveProfiles("test")
@SuppressWarnings({ "unchecked", "unused" })
class UniqueCodeServiceTest {
    private CodedRepository<Object> repo;
    private UniqueCodeService<Object> service;

    @BeforeEach
    void setUp() {
        repo = mock(CodedRepository.class);
        service = new UniqueCodeService<>(6, repo);
    }

    @Test
    void generateCode_returnsCodeOfCorrectLength() {
        when(repo.existsByCode(anyString())).thenReturn(false);

        String code = service.generateCode();

        assertThat(code).hasSize(6);
    }

    @Test
    void generateCode_returnsAlphanumericCode() {
        when(repo.existsByCode(anyString())).thenReturn(false);

        String code = service.generateCode();

        assertThat(code).matches("[A-Z0-9]{6}");
    }

    @Test
    void generateCode_retriesIfCodeExists() {
        when(repo.existsByCode(anyString()))
                .thenReturn(true)
                .thenReturn(false);

        String code = service.generateCode();

        assertThat(code).hasSize(6);
        verify(repo, times(2)).existsByCode(anyString());
    }
}
