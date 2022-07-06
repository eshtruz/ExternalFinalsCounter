package com.shtruz.externalfinalscounter.instrument.transformer;

import java.security.ProtectionDomain;

public interface Transformer {
    byte[] transform(ClassLoader loader, String name, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes);
}
