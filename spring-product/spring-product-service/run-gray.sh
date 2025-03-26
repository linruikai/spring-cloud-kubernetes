export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
mvn install -DskipTests --settings /Users/colin/.m2/default.xml
docker build -t spring-product-gray .
kubectl delete -f ../../config/deploy/gray/spring-product.yaml
minikube image load spring-product-gray -p multinode
kubectl apply -f ../../config/deploy/gray/spring-product.yaml