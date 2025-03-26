package org.example.gateway.loadbalancer;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.kubernetes.commons.discovery.DefaultKubernetesServiceInstance;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CustomLoadBalancerConfiguration implements ReactorServiceInstanceLoadBalancer {
    final String serviceId;
    ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    final DiscoveryClient discoveryClient;
    final LoadBalancerClient loadBalancerClient;

    public CustomLoadBalancerConfiguration(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                                           String serviceId, DiscoveryClient discoveryClient,LoadBalancerClient loadBalancerClient) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.discoveryClient = discoveryClient;
        this.loadBalancerClient = loadBalancerClient;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(serviceInstances -> getInstanceResponse(request));
    }
    private Response<ServiceInstance> getInstanceResponse(Request request) {
        DefaultRequestContext requestContext = (DefaultRequestContext) request.getContext();
        RequestData clientRequest = (RequestData) requestContext.getClientRequest();
        HttpHeaders headers = clientRequest.getHeaders();
        String header = headers.getFirst("version");
        try {
            List<DefaultKubernetesServiceInstance> allNameSpaceInstances = discoveryClient.getInstances(serviceId)
                    .stream()
                    .filter(instance -> instance instanceof DefaultKubernetesServiceInstance)
                    .map(instance -> (DefaultKubernetesServiceInstance) instance).toList();

            List<DefaultKubernetesServiceInstance> instances = allNameSpaceInstances.stream()
                    .filter(instance -> instance.getNamespace().equals(header)).toList();
            if (CollectionUtils.isEmpty(instances)) {
                instances = allNameSpaceInstances.stream()
                        .filter(instance -> instance.getNamespace().equals("default")).toList();
            }
            return new DefaultResponse(instances.get(ThreadLocalRandom.current().nextInt(instances.size())));  // todo 这里应该使用默认的负载均衡策略，待完善
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

