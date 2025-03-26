环境准备
```shell
minikube start --memory=8192mb --cpus=4  --addons=istio,istio-provisioner,ingress,ingress-dns,dashboard --nodes 3 -p multinode
kubectl create namespace addons
telepresence helm install --namespace=addons
sudo telepresence connect
```

创建mysql redis kafka

mysql
```shell
helm install mysql bitnami/mysql -n datasource  -f my-mysql.yaml
```
```yaml
architecture: standalone
auth:
  rootPassword: "root123456"
  createDatabase: true  # 允许创建数据库
primary:
  persistence:
    enabled: true
    size: 128Mi
  podSecurityContext:
    enabled: true
    runAsUser: 1001
    runAsGroup: 1001
    fsGroup: 1001
  containerSecurityContext:
    enabled: true
    runAsUser: 1001
    runAsGroup: 1001
    runAsNonRoot: false
    readOnlyRootFilesystem: false
```

redis
```shell
helm install redis bitnami/redis  -f  my-redis.yaml -n datasource
```
```yaml
architecture: standalone
auth:
  enabled: false
master:
  persistence:
    enabled: true
    size: 128Mi
  containerSecurityContext:
    enabled: true
    runAsUser: 0
    runAsGroup: 0
    runAsNonRoot: false
```
kafka
```shell
helm install kafka  bitnami/kafka -n datasource  -f my-kafka.yaml
```
```yaml
controller:
  replicaCount: 1
  heapOpts: -Xmx256m -Xms256m
  persistence:
    size: 128Mi
  containerSecurityContext:
    enabled: true
    runAsUser: 1000
    runAsGroup: 1000
    runAsNonRoot: true
sasl:
  client:
    users:
      - root
    passwords: "root123456"
```
初始化mysql redis测试数据
```shell
config/datasource/db.sql
config/datasource/redis.sh
```

创建配置文件
```shell
kubectl apply -f config/configmap -R
```
创建用户
```shell
kubectl apply -f deploy/spring-cloud-k8s-sa-cluster.yaml
```

创建istio路由
```shell
kubectl apply -f https://github.com/kubernetes-sigs/gateway-api/releases/download/v1.2.0/standard-install.yaml
kubectl apply -f config/istio
```

gateway服务
```shell
cd spring-gateway
sh run.sh
cd ../
```

user服务
```shell
cd spring-user/spring-user-service
sh run.sh
cd ../../
```
user-gray服务
```shell
cd spring-user/spring-user-service
sh run-gray.sh
cd ../../
```

product服务
```shell
cd spring-product/spring-product-service
sh run.sh
cd ../../
```
product-gray服务
```shell
cd spring-product/spring-product-service
sh run-gray.sh
cd ../../
```

detail服务
```shell
cd spring-detail/spring-detail-service
sh run.sh
cd ../../
```

detail-gray服务
```shell
cd spring-detail/spring-detail-service
sh run-gray.sh
cd ../../
```