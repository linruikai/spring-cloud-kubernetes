export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
mvn install -Dmaven.test.skip=true --settings /Users/colin/.m2/default.xml
docker build -t spring-detail-gray .
kubectl delete -f ../../config/deploy/gray/spring-detail.yaml
minikube image load spring-detail-gray -p multinode
kubectl apply -f ../../config/deploy/gray/spring-detail.yaml