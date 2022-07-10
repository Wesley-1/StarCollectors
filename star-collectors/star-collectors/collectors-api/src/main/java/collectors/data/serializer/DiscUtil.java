package collectors.data.serializer;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DiscUtil {
    private static final String UTF8 = "UTF-8";
    private static HashMap<String, Lock> locks = new HashMap();

    public DiscUtil() {
    }

    public static byte[] readBytes(File file) throws IOException {
        int length = (int)file.length();
        byte[] output = new byte[length];
        InputStream in = new FileInputStream(file);

        for(int offset = 0; offset < length; offset += in.read(output, offset, length - offset)) {
        }

        in.close();
        return output;
    }

    public static void writeBytes(File file, byte[] bytes) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        out.write(bytes);
        out.close();
    }

    public static void write(File file, String content) throws IOException {
        writeBytes(file, utf8(content));
    }

    public static String read(File file) throws IOException {
        return utf8(readBytes(file));
    }

    public static boolean writeCatch(File file, String content, boolean sync) {
        String name = file.getName();
        Lock lock;
        if (locks.containsKey(name)) {
            lock = (Lock)locks.get(name);
        } else {
            ReadWriteLock rwl = new ReentrantReadWriteLock();
            lock = rwl.writeLock();
            locks.put(name, lock);
        }

        if (sync) {
            lock.lock();

            try {
                file.createNewFile();
                Files.write(content.getBytes(), file);
            } catch (IOException var9) {
            } finally {
                lock.unlock();
            }
        } else {
            CompletableFuture.runAsync(() -> {
                lock.lock();

                try {
                    write(file, content);
                } catch (IOException var7) {
                } finally {
                    lock.unlock();
                }

            });
        }

        return true;
    }

    public static String readCatch(File file) {
        try {
            return read(file);
        } catch (IOException var2) {
            return null;
        }
    }

    public static byte[] utf8(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }

    public static String utf8(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
