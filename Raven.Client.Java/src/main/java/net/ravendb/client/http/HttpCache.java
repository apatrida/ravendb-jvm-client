package net.ravendb.client.http;

import net.ravendb.client.primitives.CleanCloseable;
import net.ravendb.client.primitives.Reference;

import java.io.InputStream;

//TODO:
public class HttpCache implements CleanCloseable {

    @Override
    public void close() {

    }

    public void set(String url, String changeVector, InputStream result) {
        //TODO:
    }

    public ReleaseCacheItem get(String url, Reference<String> cachedChangeVector, Reference<Object> cachedValue) {
        return new ReleaseCacheItem();
    }

    public static class ReleaseCacheItem implements CleanCloseable {
        public void notModified() {
            //TODO:
        }

        @Override
        public void close() {
            //TODO:
        }
    }
}