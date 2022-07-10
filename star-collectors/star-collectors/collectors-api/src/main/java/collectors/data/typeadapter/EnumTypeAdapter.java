package collectors.data.typeadapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
    public static final TypeAdapterFactory ENUM_FACTORY = newEnumTypeHierarchyFactory();
    private final Map<String, T> nameToConstant = new HashMap();
    private final Map<T, String> constantToName = new HashMap();

    public EnumTypeAdapter(Class<T> classOfT) {
        try {
            Enum[] var2 = (Enum[])classOfT.getEnumConstants();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                T constant = (T) var2[var4];
                String name = constant.name();
                SerializedName annotation = (SerializedName)classOfT.getField(name).getAnnotation(SerializedName.class);
                if (annotation != null) {
                    name = annotation.value();
                }

                this.nameToConstant.put(name, constant);
                this.constantToName.put(constant, name);
            }
        } catch (NoSuchFieldException var8) {
        }

    }

    public static <TT> TypeAdapterFactory newEnumTypeHierarchyFactory() {
        return new TypeAdapterFactory() {
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                Class<? super T> rawType = typeToken.getRawType();
                if (Enum.class.isAssignableFrom(rawType) && rawType != Enum.class) {
                    if (!rawType.isEnum()) {
                        rawType = rawType.getSuperclass();
                    }

                    return new EnumTypeAdapter(rawType);
                } else {
                    return null;
                }
            }
        };
    }

    public T read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            return (T) this.nameToConstant.get(in.nextString());
        }
    }

    public void write(JsonWriter out, T value) throws IOException {
        out.value(value == null ? null : (String)this.constantToName.get(value));
    }
}
