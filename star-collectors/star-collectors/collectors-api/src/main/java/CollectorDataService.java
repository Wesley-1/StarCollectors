import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

public class CollectorDataService {

    private final RocksDB db;

    static {
       RocksDB.loadLibrary();
    }

    public CollectorDataService(String path) {
        try (final Options options = new Options().setCreateIfMissing(true)) {
            try (final RocksDB db = RocksDB.open(options, path)) {
                this.db = db;
            } catch (RocksDBException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public CollectorDataService setPath() {

        return this;
    }

    public CollectorDataService put(byte[] val0, byte[] val1) {
        try {
            db.put(val0, val1);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public boolean contains(byte[] val0) {
        return db.keyMayExist(val0, new StringBuilder());
    }

    public CollectorDataService delete(byte[] val0) {
        try {
            db.delete(val0);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

}
