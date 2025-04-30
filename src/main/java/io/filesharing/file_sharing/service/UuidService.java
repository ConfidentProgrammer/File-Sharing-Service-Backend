package io.filesharing.file_sharing.service;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

@Service
public class UuidService {
    private final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final int noOfRandomCharacter = 6;

    public long generateTimeStampId() {
        return Instant.now().toEpochMilli();
    }

    public String generateRandomId() {
        int len = LETTERS.length();
        String res = "";
        for (int i = 0; i < noOfRandomCharacter; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(0, len - 1);
            res += LETTERS.charAt(randomIndex);
        }
        return res;
    }
}
