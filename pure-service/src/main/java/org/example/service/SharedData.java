package org.example.service;

import org.springframework.security.core.context.SecurityContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/5/29
 **/
public class SharedData {
    public static final Map<String, SecurityContext> map = new ConcurrentHashMap<>();
}
