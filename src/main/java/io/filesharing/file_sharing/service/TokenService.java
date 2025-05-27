package io.filesharing.file_sharing.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.filesharing.file_sharing.exceptions.TokenNotFoundException;
import io.filesharing.file_sharing.exceptions.TokenStorageException;
import io.filesharing.file_sharing.model.File;
import io.filesharing.file_sharing.model.Token;
import io.filesharing.file_sharing.repository.TokenRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final Long TOKEN_EXPIRY = (long) 7;
    private final TokenRepository tokenRepository;
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    public Token createTokenRecord(File file) throws TokenStorageException {
        try {
            Token token = new Token();
            String id = getTokenID(file.getId());
            token.setId(id);
            token.setFile(file);
            token.setExpiresAt(LocalDateTime.now().plusDays(TOKEN_EXPIRY));
            tokenRepository.save(token);
            return token;
        } catch (Exception e) {
            logger.error("DB error while saving token in the DB", e);
            throw new TokenStorageException("Error while creating token for download link");
        }
    }

    public String getFileIdFromTokenId(String tokenId) throws TokenNotFoundException {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new TokenNotFoundException("cannot find file from the link"));
        return token.getFile().getId();
    }

    public String getTokenID(String fileId) {
        String[] chunks = fileId.split("_");
        return chunks[1];
    }

}
