package be.intecbrussel.student.util;

import be.intecbrussel.student.exception.CryptoException;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CryptoFileProcessorTest {

    private final ICryptoFileProcessor cryptoFileProcessor;

    public CryptoFileProcessorTest(ICryptoFileProcessor cryptoFileProcessor) {
        this.cryptoFileProcessor = cryptoFileProcessor;
    }

    private final String key = "Mary has one cat";
    private final File inputFile = new File("document.txt");
    private final File encryptedFile = new File("document.encrypted");
    private final File decryptedFile = new File("document.decrypted");

    @Test
    public void shouldEncrypt() {

        try {
            cryptoFileProcessor.encrypt(key, inputFile, encryptedFile);

        } catch (CryptoException cryptoException) {
            cryptoException.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {

        Files.deleteIfExists(Path.of("document.txt"));
        Files.deleteIfExists(Path.of("document.encrypted"));
        Files.deleteIfExists(Path.of("document.decrypted"));
    }
}