/*
 *
 *  Copyright 2016 Robert Winkler
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */
package io.github.resilience4j.circuitbreaker;


import io.github.resilience4j.circuitbreaker.internal.InMemoryCircuitBreakerRegistry;
import io.vavr.collection.Seq;

import java.util.function.Supplier;

/**
 * The {@link CircuitBreakerRegistry} is a factory to create CircuitBreaker instances which stores all CircuitBreaker instances in a registry.
 */
public interface CircuitBreakerRegistry {

    /**
     * 返回所有的CircuitBreaker实例
     *
     * Returns all managed {@link CircuitBreaker} instances.
     * @return all managed {@link CircuitBreaker} instances.
     */
    Seq<CircuitBreaker> getAllCircuitBreakers();

    /**
     * 根据名称返回CircuitBreaker实例，如果不存在则根据默认配置，创建CircuitBreaker实例并返回
     *
     * Returns a managed {@link CircuitBreaker} or creates a new one with the default CircuitBreaker configuration.
     * @param name the name of the CircuitBreaker
     * @return The {@link CircuitBreaker}
     */
    CircuitBreaker circuitBreaker(String name);

    /**
     * 根据名称返回CircuitBreaker实例，如果不存在则 根据传入的配置实例，创建CircuitBreaker实例并返回
     *
     * Returns a managed {@link CircuitBreaker} or creates a new one with a custom CircuitBreaker configuration.
     * @param name      the name of the CircuitBreaker
     * @param circuitBreakerConfig  a custom CircuitBreaker configuration
     * @return The {@link CircuitBreaker}
     */
    CircuitBreaker circuitBreaker(String name, CircuitBreakerConfig circuitBreakerConfig);

    /**
     * Returns a managed {@link CircuitBreaker} or creates a new one with a custom CircuitBreaker configuration.
     *
     * @param name      the name of the CircuitBreaker
     * @param circuitBreakerConfigSupplier a supplier of a custom CircuitBreaker configuration
     * @return The {@link CircuitBreaker}
     */
    CircuitBreaker circuitBreaker(String name, Supplier<CircuitBreakerConfig> circuitBreakerConfigSupplier);

    /**
     * 根据自定义的配置，创建CircuitBreakerRegistry实例。线程安全的单例。
     *
     * Creates a CircuitBreakerRegistry with a custom CircuitBreaker configuration.
     * @param circuitBreakerConfig a custom CircuitBreaker configuration
     * @return a CircuitBreakerRegistry with a custom CircuitBreaker configuration.
     */
    static CircuitBreakerRegistry of(CircuitBreakerConfig circuitBreakerConfig){
        return new InMemoryCircuitBreakerRegistry(circuitBreakerConfig);
    }

    /**
     * 使用默认配置，创建CircuitBreakerRegistry实例。线程安全的单例。
     *
     * Creates a CircuitBreakerRegistry with a default CircuitBreaker configuration.
     * @return a CircuitBreakerRegistry with a default CircuitBreaker configuration.
     */
    static CircuitBreakerRegistry ofDefaults(){
        return new InMemoryCircuitBreakerRegistry();
    }
}
