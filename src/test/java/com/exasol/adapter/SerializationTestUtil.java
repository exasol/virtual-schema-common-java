package com.exasol.adapter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.*;

public class SerializationTestUtil {

    public static <T> void assertSerializable(final T object) throws IOException, ClassNotFoundException {
        final byte[] serialized = serialize(object);
        final Object deserialized = deserialize(serialized, object.getClass());
        assertThat(deserialized, not(nullValue()));
    }

    private static <T> byte[] serialize(final T object) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return outputStream.toByteArray();
    }

    private static <T> T deserialize(final byte[] serializedObject, final Class<T> clazz)
            throws IOException, ClassNotFoundException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedObject);
        final ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        final Object result = objectInputStream.readObject();
        return clazz.cast(result);
    }
}
