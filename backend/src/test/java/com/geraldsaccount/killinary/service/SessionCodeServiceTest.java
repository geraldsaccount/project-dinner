package com.geraldsaccount.killinary.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.geraldsaccount.killinary.repository.SessionRepository;

@ActiveProfiles("test")
@SuppressWarnings("unused")
class SessionCodeServiceTest {

    @Mock
    private SessionRepository sessionRepository;
    @InjectMocks
    private SessionCodeService sessionCodeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void constructor_setsCodeLengthTo8() {
        String code = sessionCodeService.generateCode();
        assertThat(code).hasSize(8);
    }

    @Test
    void generateCode_returnsUniqueCode() {
        String code1 = sessionCodeService.generateCode();
        String code2 = sessionCodeService.generateCode();
        assertThat(code1).isNotEqualTo(code2);
    }

    @Test
    void service_isInstanceOfUniqueCodeService() {
        assertThat(sessionCodeService).isInstanceOf(UniqueCodeService.class);
    }
}
