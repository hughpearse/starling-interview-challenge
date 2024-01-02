package com.starling.challenge.config;

import java.net.URI;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "basehttpclient")
@StaticInitSafe
public interface MicroProfileConfig {
    public URI domain();
    public String useragent();
    public String authorization();
}
