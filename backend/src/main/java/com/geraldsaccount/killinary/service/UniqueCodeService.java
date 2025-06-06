package com.geraldsaccount.killinary.service;

import java.security.SecureRandom;

import com.geraldsaccount.killinary.repository.CodedRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueCodeService<T> {
    private static final int MAX_ATTEMPTS = 100;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final SecureRandom random = new SecureRandom();
    private final int codeLength;
    private final CodedRepository<T> repo;

    public String generateCode() {
        String code;
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            StringBuilder sb = new StringBuilder(codeLength);
            for (int i = 0; i < codeLength; i++) {
                int idx = random.nextInt(CHARACTERS.length());
                sb.append(CHARACTERS.charAt(idx));
            }
            code = sb.toString();
            if (!repo.existsByCode(code)) {
                return code;
            }
        }
        throw new RuntimeException("Could not generate unique code.");
    }
}
