/**
 * JBoss, Home of Professional Open Source.
 * Copyright 2019-2022 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.pnc.cleaner.auth.keycloakutil.util;

import io.micrometer.core.instrument.MeterRegistry;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @author <a href="mailto:mstrukel@redhat.com">Marko Strukelj</a>
 */
public class Headers implements Iterable<Header> {

    private static final String className = Headers.class.getName();

    private LinkedHashMap<String, Header> headers = new LinkedHashMap<>();

    @Inject
    MeterRegistry registry;

    @PostConstruct
    void initMetrics() {
        registry.gauge(className + ".headers.map.size", this, Headers::getHeadersMapSize);
    }

    private int getHeadersMapSize() {
        return headers.size();
    }

    public void add(String header, String value) {
        headers.put(header.toLowerCase(), new Header(header, value));
    }

    public boolean addIfMissing(String header, String value) {
        String key = header.toLowerCase();
        if (!headers.containsKey(key)) {
            headers.put(key, new Header(header, value));
            return true;
        }
        return false;
    }

    public boolean contains(String header) {
        String key = header.toLowerCase();
        return headers.containsKey(key);
    }

    public Header get(String header) {
        return headers.get(header.toLowerCase());
    }

    @Override
    public Iterator<Header> iterator() {
        return headers.values().iterator();
    }
}
