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
package io.github.resilience4j.circuitbreaker.internal;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

final class ClosedState extends CircuitBreakerState {

    // 关闭状态的度量指标
    private final CircuitBreakerMetrics circuitBreakerMetrics;
    // 请求调用的失败率阈值，在配置类CircuitBreakerConfig的实例中已设置
    private final float failureRateThreshold;

    ClosedState(CircuitBreakerStateMachine stateMachine) {
        this(stateMachine, null);
    }

    ClosedState(CircuitBreakerStateMachine stateMachine, CircuitBreakerMetrics circuitBreakerMetrics) {
        super(stateMachine);
        CircuitBreakerConfig circuitBreakerConfig = stateMachine.getCircuitBreakerConfig();
        if(circuitBreakerMetrics == null){
            this.circuitBreakerMetrics = new CircuitBreakerMetrics(
                circuitBreakerConfig.getRingBufferSizeInClosedState());
        }else{
            this.circuitBreakerMetrics = circuitBreakerMetrics.copy(circuitBreakerConfig.getRingBufferSizeInClosedState());
        }
        this.failureRateThreshold = stateMachine.getCircuitBreakerConfig().getFailureRateThreshold();
    }

    /**
     * 在关闭状态下，总是允许请求调用后端接口
     * Returns always true, because the CircuitBreaker is closed.
     * @return always true, because the CircuitBreaker is closed.
     */
    @Override
    boolean isCallPermitted() {
        return true;
    }

    /**
     * 调用度量指标circuitBreakerMetrics.onError()方法记录请求调用失败并返回失败百分比
     * 调用checkFailureRate方法检查是否达到阈值
     * @param throwable
     */
    @Override
    void onError(Throwable throwable) {
        // CircuitBreakerMetrics is thread-safe
        checkFailureRate(circuitBreakerMetrics.onError());
    }

    /**
     * 调用度量指标circuitBreakerMetrics.onError()方法记录请求调用成功并返回失败百分比
     * 调用checkFailureRate方法检查是否达到阈值
     */
    @Override
    void onSuccess() {
        // CircuitBreakerMetrics is thread-safe
        checkFailureRate(circuitBreakerMetrics.onSuccess());
    }

    /**
     * 如果失败率大于等于阈值，则触发状态机，从关闭状态转换到打开状态
     * Checks if the current failure rate is above the threshold.
     * If the failure rate is above the threshold, transitions the state machine to OPEN state.
     * @param currentFailureRate the current failure rate
     */
    private void checkFailureRate(float currentFailureRate) {
        if (currentFailureRate >= failureRateThreshold) {
            // Transition the state machine to OPEN state, because the failure rate is above the threshold
            stateMachine.transitionToOpenState();
        }
    }

    /**
     * Get the state of the CircuitBreaker
     */
    @Override
    CircuitBreaker.State getState() {
        return CircuitBreaker.State.CLOSED;
    }
    /**
     *
     * Get metrics of the CircuitBreaker
     */
    @Override
    CircuitBreakerMetrics getMetrics() {
        return circuitBreakerMetrics;
    }
}
